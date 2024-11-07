package com.app.service.impl;

import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.entity.ClientVendor;
import com.app.entity.Invoice;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import com.app.repository.InvoiceProductRepository;
import com.app.repository.InvoiceRepository;
import com.app.service.InvoiceProductService;
import com.app.service.InvoiceService;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final InvoiceProductService invoiceProductService;

    @Override
    public InvoiceDto listInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> listAllByInvoiceType(InvoiceType invoiceType) {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        return invoiceRepository.findAllByInvoiceType(invoiceType).stream()
                .filter(m->m.getCompany().getId().equals(companyId))
                .sorted(Comparator.comparing(Invoice::getInvoiceNo).reversed())
                .map(each-> {
                    InvoiceDto invoiceDto = mapperUtil.convert(each, new InvoiceDto());
                    calculatePricesAndTaxes(invoiceDto);
                    return invoiceDto;
                })
                .toList();
    }

    @Override
    public List<InvoiceDto> listAllInvoiceByClientVendorId(Long id) {
        List<Invoice> list = invoiceRepository.findAllByClientVendor_id(id);
        return list.stream().map(each -> mapperUtil.convert(each, new InvoiceDto())).collect(Collectors.toList());
    }

    @Override
    public InvoiceDto generateNewInvoiceDto(InvoiceType invoiceType) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setDate(LocalDate.now());
        invoiceDto.setInvoiceNo(generateInvoiceNo(invoiceType));
        invoiceDto.setInvoiceType(invoiceType);
        return invoiceDto;
    }

    @Override
    public InvoiceDto saveInvoice(InvoiceDto invoiceDto, InvoiceType invoiceType) {
        invoiceDto.setInvoiceType(invoiceType);
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        Invoice invoice = invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    @Override
    public void updateInvoice(InvoiceDto dto) {
        ClientVendor clientVendor = mapperUtil.convert(dto.getClientVendor(), new ClientVendor());
        Invoice invoice = invoiceRepository.findById(dto.getId()).orElseThrow(NoSuchElementException::new);
        invoice.setClientVendor(clientVendor);
        invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(NoSuchElementException::new);
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
        invoiceProductService.findInvoiceProductByInvoiceId(id)
                .forEach(each-> invoiceProductService.deleteInvoiceProduct(each.getId()));
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
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        Invoice invoice = invoiceRepository.findFirstByCompanyIdAndInvoiceTypeOrderByInvoiceNoDesc(companyId, invoiceType);

        long number = invoice.getInvoiceNo() == null ? 0 : Long.parseLong(invoice.getInvoiceNo().substring(2)); // ex: S-003
        String formatted = String.format("%03d", number+1);
        if(invoiceType.equals(InvoiceType.PURCHASE)) return "P-"+formatted;
        else return "S-"+formatted; // if invoiceType==Sales
    }

}
