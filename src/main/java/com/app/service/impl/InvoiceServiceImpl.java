package com.app.service.impl;

import com.app.dto.InvoiceDto;
import com.app.entity.Invoice;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import com.app.repository.InvoiceRepository;
import com.app.service.ClientVendorService;
import com.app.service.InvoiceService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final MapperUtil mapperUtil;
    private final InvoiceRepository invoiceRepository;
    private final ClientVendorService clientVendorService;


    @Override
    public InvoiceDto findInvoiceById(Long id) {
        Invoice item = invoiceRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return mapperUtil.convert(item, new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> listAllInvoiceByInvoiceTypeOrderByInvoiceNo(InvoiceType invoiceType) {

//            String usernameAuth = SecurityContextHolder.getContext().getAuthentication().getName();
//            User user = userRepository.findByUsername(usernameAuth).get();
//            String title = user.getCompany().getTitle();
        List<Invoice> list = invoiceRepository.findAllByInvoiceType(invoiceType);
        return list.stream().map(each -> mapperUtil.convert(each, new InvoiceDto())).collect(Collectors.toList());
    }

    @Override
    public String getLastInvoiceId(InvoiceType invoiceType) {
        Invoice item = invoiceRepository.findFirstByInvoiceTypeOrderByIdDesc(invoiceType);
        final long id = item.getId();
        final DecimalFormat decimalFormat = new DecimalFormat("000");
        return decimalFormat.format(id + 1);
    }

    @Override
    public void saveSaleInvoice(InvoiceDto invoiceDto) {
//        InvoiceDto invoice = invoiceRepository.findALlByInvoiceTypeOrderByIdDesc(InvoiceType.SALES).stream().map(each -> mapperUtil.convert(each, new InvoiceDto())).findFirst().get();
//        final long i= invoice.getId();
//        final DecimalFormat decimalFormat = new DecimalFormat("000");
//        invoiceDto.setInvoiceNo("S-"+decimalFormat.format(i+1));
        invoiceDto.setInvoiceType(InvoiceType.SALES);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setDate(LocalDate.now());
        invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));
    }

    @Override
    public void savePurchaseInvoice(InvoiceDto invoiceDto) {
//        InvoiceDto invoice = invoiceRepository.findALlByInvoiceTypeOrderByIdDesc(InvoiceType.PURCHASE).stream().map(each -> mapperUtil.convert(each, new InvoiceDto())).findFirst().get();
//        final long i= invoice.getId();
//        final DecimalFormat decimalFormat = new DecimalFormat("000");
//        invoiceDto.setInvoiceNo("P-"+decimalFormat.format(i+1));
        invoiceDto.setInvoiceType(InvoiceType.PURCHASE);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setDate(LocalDate.now());
        invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));
    }

    @Override
    public List<InvoiceDto> listAllInvoiceByClientVendorId(Long id) {
        List<Invoice> list = invoiceRepository.findAllByClientVendor_id(id);
        return list.stream().map(each -> mapperUtil.convert(each, new InvoiceDto())).collect(Collectors.toList());
    }

}
