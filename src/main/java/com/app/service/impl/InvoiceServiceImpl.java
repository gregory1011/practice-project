package com.app.service.impl;

import com.app.annotation.ExecutionTime;
import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.entity.ClientVendor;
import com.app.entity.Invoice;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import com.app.exceptions.InvoiceNotFoundException;
import com.app.repository.InvoiceRepository;
import com.app.service.CompanyService;
import com.app.service.InvoiceProductService;
import com.app.service.InvoiceService;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapperUtil;
    private final InvoiceProductService invoiceProductService;
    private final CompanyService companyService;


    @Override
    public InvoiceDto findById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(InvoiceNotFoundException::new);
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> listInvoices(InvoiceType invoiceType) {
        Long companyId = companyService.getCompanyByLoggedInUser().getId();
        return invoiceRepository.findAllByCompanyIdAndInvoiceType(companyId, invoiceType).stream()
                .sorted(Comparator.comparing(Invoice::getInvoiceNo).reversed())
                .map(each-> {
                    InvoiceDto invoiceDto = mapperUtil.convert(each, new InvoiceDto());
                    calculatePricesAndTaxes(invoiceDto);
                    return invoiceDto;
                })
                .toList();
    }

//    @Override
//    public List<InvoiceDto> listAllInvoiceByClientVendorId(Long id) {
//        List<Invoice> list = invoiceRepository.findAllByClientVendor_id(id);
//        return list.stream().map(each -> mapperUtil.convert(each, new InvoiceDto())).collect(Collectors.toList());
//    }

    @Override
    public InvoiceDto generateNewInvoiceDto(InvoiceType invoiceType) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setDate(LocalDate.now());
        invoiceDto.setInvoiceNo(generateInvoiceNo(invoiceType));
        invoiceDto.setInvoiceType(invoiceType);
        return invoiceDto;
    }

    @Override
    public InvoiceDto save(InvoiceDto invoiceDto, InvoiceType invoiceType) {
        invoiceDto.setInvoiceType(invoiceType);
        invoiceDto.setCompany(companyService.getCompanyByLoggedInUser());
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        Invoice entity = invoiceRepository.save(invoice);
        return mapperUtil.convert(entity, new InvoiceDto());
    }

    @ExecutionTime
    @Transactional
    public InvoiceDto approveInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(InvoiceNotFoundException::new);
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        if (invoice.getInvoiceType().equals(InvoiceType.PURCHASE)){
            invoiceProductService.updateRemainingQuantityUponPurchaseApproval(id);
            invoiceProductService.updateQuantityInStockPurchase(id);
        } else if (invoice.getInvoiceType().equals(InvoiceType.SALES)) {
            invoiceProductService.updateQuantityInStockSale(id);
            invoiceProductService.calculateProfitOrLoss(id);
        }
        Invoice saved = invoiceRepository.save(invoice);
        return mapperUtil.convert(saved, new InvoiceDto());
    }

    @Override
    public InvoiceDto updateInvoice(InvoiceDto dto) {
        Invoice invoice = invoiceRepository.findById(dto.getId()).orElseThrow(InvoiceNotFoundException::new);
        ClientVendor clientVendor = mapperUtil.convert(dto.getClientVendor(), new ClientVendor());
        invoice.setClientVendor(clientVendor);
        Invoice saved = invoiceRepository.save(invoice);
        return mapperUtil.convert(saved, new InvoiceDto());
    }

    @Override
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(InvoiceNotFoundException::new);
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
        invoiceProductService.listInvoiceProductByInvoiceId(id)
                .forEach(each-> invoiceProductService.deleteInvoiceProduct(each.getId()));
    }

    @Override
    public List<InvoiceDto> listLast3ApprovedInvoices() {
        Long companyId = companyService.getCompanyByLoggedInUser().getId();
        return invoiceRepository.findByCompanyIdOrderByInsertDateTimeDescLimit3(companyId) // and InvoiceStatus = 'APPROVED'
                .stream().map(each ->{
                    InvoiceDto invoiceDto = mapperUtil.convert(each, new InvoiceDto());
                    calculatePricesAndTaxes(invoiceDto);
                    return invoiceDto;
                }).toList();
    }

    @Override
    public BigDecimal sumTotal(InvoiceType invoiceType) {
        return listInvoices(invoiceType).stream()
                .filter(each->each.getInvoiceStatus().equals(InvoiceStatus.APPROVED))
                .map(InvoiceDto::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumProfitLoss() {
        Long companyId = companyService.getCompanyByLoggedInUser().getId();
        return invoiceRepository.findByCompanyIdAndInvoiceTypeSalesAndInvoiceStatusApproved(companyId)
                .stream().map(each ->
                        invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(each.getId())
                                .stream()
                                .map(InvoiceProductDto::getProfitLoss)
                                .reduce(BigDecimal::add)
                                .orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public InvoiceDto printInvoiceId(Long id) {
        InvoiceDto invoiceDto = findById(id);
        calculatePricesAndTaxes(invoiceDto);
        return invoiceDto;
    }

    private void calculatePricesAndTaxes(InvoiceDto dto) {
        List<InvoiceProductDto> list = invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(dto.getId());
        BigDecimal price = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (InvoiceProductDto each : list) {
            totalPrice= totalPrice.add(each.getTotal());
            price= price.add(each.getPrice().multiply(BigDecimal.valueOf(each.getQuantity())));
        }
        BigDecimal tax = totalPrice.subtract(price);
        dto.setTax(tax);
        dto.setPrice(price);
        dto.setTotal(totalPrice);
    }

    private String generateInvoiceNo(InvoiceType invoiceType) {
        Long companyId = companyService.getCompanyByLoggedInUser().getId();
        Invoice invoice = invoiceRepository.findFirstByCompanyIdAndInvoiceTypeOrderByInvoiceNoDesc(companyId, invoiceType);

        long number = invoice.getInvoiceNo() == null ? 0 : Long.parseLong(invoice.getInvoiceNo().substring(2)); // ex: S-003
        String formatted = String.format("%03d", number+1);
        if(invoiceType.equals(InvoiceType.PURCHASE)) return "P-"+formatted;
        else return "S-"+formatted; // if invoiceType==Sales
    }

}
