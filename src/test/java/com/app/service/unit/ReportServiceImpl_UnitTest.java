package com.app.service.unit;


import com.app.service.InvoiceProductService;
import com.app.service.InvoiceService;
import com.app.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class ReportServiceImpl_UnitTest {


    @Mock private InvoiceService invoiceService;
    @Mock private InvoiceProductService invoiceProductService;
    @InjectMocks
    private ReportServiceImpl reportServiceImpl;

    @Test
    void name() {
    }
}
