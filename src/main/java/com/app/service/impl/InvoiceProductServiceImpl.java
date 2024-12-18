package com.app.service.impl;

import com.app.dto.CompanyDto;
import com.app.dto.InvoiceProductDto;
import com.app.dto.ProductDto;
import com.app.entity.Invoice;
import com.app.entity.InvoiceProduct;
import com.app.entity.Product;
import com.app.enums.InvoiceStatus;
import com.app.exceptions.InvoiceProductNotFoundException;
import com.app.exceptions.ProductNotFoundException;
import com.app.repository.InvoiceProductRepository;
import com.app.service.*;
import com.app.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;
    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final CompanyService companyService;


    public InvoiceProductServiceImpl(@Lazy InvoiceService invoiceService, InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil, ProductService productService, CompanyService companyService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.companyService = companyService;
    }


    @Override
    public InvoiceProductDto findById(Long id) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(id).orElseThrow(InvoiceProductNotFoundException::new);
        return mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> listAllByInvoiceIdAndCalculateTotalPrice(Long id) {
        return invoiceProductRepository.findAllByInvoiceId(id).stream()
                .map(each -> {
                    InvoiceProductDto invoiceProductDto = mapperUtil.convert(each, new InvoiceProductDto());
                    invoiceProductDto.setTotal(getTotalPriceWithTax(each));
                    return invoiceProductDto;
                })
                .toList();
    }

    @Override
    public List<InvoiceProductDto> listInvoiceProductByInvoiceId(Long invoiceId) {
        List<InvoiceProduct> list = invoiceProductRepository.findAllByInvoiceId(invoiceId);
        return list.stream().map(each -> mapperUtil.convert(each, new InvoiceProductDto())).toList();
    }

    @Override
    public InvoiceProductDto add(InvoiceProductDto invoiceProductDto, Long invoiceId) {
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        Invoice invoice = mapperUtil.convert(invoiceService.findById(invoiceId), new Invoice());
        invoiceProduct.setInvoice(invoice);
//        invoiceProduct.setRemainingQuantity(0);
        InvoiceProduct saved = invoiceProductRepository.save(invoiceProduct);
        return mapperUtil.convert(saved, invoiceProductDto);
    }

    @Override
    public List<InvoiceProductDto> listAllApprovedInvoiceProductsOfCompany() {
        CompanyDto companyDto = companyService.getCompanyByLoggedInUser();
        return invoiceProductRepository.retrieveByInvoiceCompanyIdAndAndInvoiceStatus(companyDto.getId(), InvoiceStatus.APPROVED)
                        .stream()
                        .map(each -> mapperUtil.convert(each, new InvoiceProductDto())).toList();

//        invoiceProductRepository.findAll().stream()
//                .filter(m -> m.getInvoice().getCompany().getId().equals(companyDto.getId()))
//                .filter(m -> m.getInvoice().getInvoiceStatus().equals(InvoiceStatus.APPROVED))
//                .map(each -> mapperUtil.convert(each, new InvoiceProductDto())).toList();
    }

    @Override
    public void saveInvoiceProduct(Long invoiceId, InvoiceProductDto dto) {
        Invoice invoice = mapperUtil.convert(invoiceService.findById(invoiceId), new Invoice());
        InvoiceProduct invoiceProduct = mapperUtil.convert(dto, new InvoiceProduct());
        invoiceProduct.setInvoice(invoice);
        mapperUtil.convert(invoiceProductRepository.save(invoiceProduct), new InvoiceProductDto());
    }

    @Override
    public void deleteInvoiceProduct(Long invoiceProductId) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(invoiceProductId).orElseThrow();
        invoiceProduct.setIsDeleted(true);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public void updateRemainingQuantityUponPurchaseApproval(Long id) {
        List<InvoiceProduct> list = invoiceProductRepository.findAllByInvoiceId(id);
        list.forEach(each -> {
            each.setRemainingQuantity(each.getQuantity());
            invoiceProductRepository.save(each);
        });
    }

    @Override
    public void updateQuantityInStockPurchase(Long id) {
        List<Product> list = invoiceProductRepository.listProductsByInvoiceId(id);
        list.forEach(each -> {
            Integer sumQuantityOfProducts = invoiceProductRepository.sumQuantityOfProducts(id, each.getId());
            each.setQuantityInStock(each.getQuantityInStock() + sumQuantityOfProducts);
            productService.saveProduct(mapperUtil.convert(each, new ProductDto()));
        });
    }

    @Override
    public void updateQuantityInStockSale(Long id) {
        List<Product> list = invoiceProductRepository.listProductsByInvoiceId(id);
        list.forEach(each -> {
            final int stock = each.getQuantityInStock() - invoiceProductRepository.sumQuantityOfProducts(id, each.getId());
            if (stock < 0)
                throw new ProductNotFoundException("Stock of " + each.getName() + " is not enough to approve this invoice. Please update the invoice.");
            else each.setQuantityInStock(stock);
            productService.saveProduct(mapperUtil.convert(each, new ProductDto()));
        });
    }

    @Override
    public void calculateProfitOrLoss(Long id) {
        List<InvoiceProduct> list = invoiceProductRepository.findAllByInvoiceId(id);
        list.forEach(each -> {
            Long productId = each.getProduct().getId();
            BigDecimal profitLoss = getTotalPriceWithTax(each).subtract(calculateCost(productId, each.getQuantity()));
            each.setProfitLoss(profitLoss);
            invoiceProductRepository.save(each);
        });
    }

    private BigDecimal calculateCost(Long productId, int salesQuantity) {
        Long companyId = companyService.getCompanyByLoggedInUser().getId();
        List<InvoiceProduct> list = invoiceProductRepository.getApprovedPurchaseInvoiceProducts(companyId, productId);
        BigDecimal totalCost = BigDecimal.ZERO;
        for (InvoiceProduct each : list) {
            int remainingQty = each.getRemainingQuantity() - salesQuantity;
            if (remainingQty <= 0) {
                BigDecimal costWithoutTax = each.getPrice().multiply(BigDecimal.valueOf(each.getRemainingQuantity()));
                BigDecimal taxAmount = costWithoutTax.multiply(BigDecimal.valueOf(each.getTax())).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal costWithTax = costWithoutTax.add(taxAmount);
                salesQuantity = salesQuantity - each.getRemainingQuantity();
                each.setRemainingQuantity(0);
                totalCost = totalCost.add(costWithTax);
                invoiceProductRepository.save(each);
                if (remainingQty == 0) break;
            } else {
                BigDecimal costWithoutTax = each.getPrice().multiply(BigDecimal.valueOf(salesQuantity));
                BigDecimal taxAmount = costWithoutTax.multiply(BigDecimal.valueOf(each.getTax())).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal costWithTax = costWithoutTax.add(taxAmount);
                each.setRemainingQuantity(remainingQty);
                totalCost = totalCost.add(costWithTax);
                invoiceProductRepository.save(each);
                break;
            }
        }

        return totalCost;
    }

    private BigDecimal getTotalPriceWithTax(InvoiceProduct invoiceProduct) {
        BigDecimal totalPrice = invoiceProduct.getPrice().multiply(BigDecimal.valueOf(invoiceProduct.getQuantity()));
        BigDecimal totalTax = totalPrice.multiply(BigDecimal.valueOf(invoiceProduct.getTax() / 100d));
        return totalPrice.add(totalTax);
    }


}
