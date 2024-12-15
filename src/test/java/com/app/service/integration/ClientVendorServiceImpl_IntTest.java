package com.app.service.integration;


import com.app.dto.ClientVendorDto;
import com.app.entity.ClientVendor;
import com.app.enums.ClientVendorType;
import com.app.exceptions.ClientVendorNotFoundException;
import com.app.repository.ClientVendorRepository;
import com.app.service.ClientVendorService;
import com.app.service.SecuritySetUpUtil;
import com.app.service.TestDocInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
public class ClientVendorServiceImpl_IntTest {

    @Autowired
    private ClientVendorService clientVendorService;
    @Autowired
    private ClientVendorRepository clientVendorRepository;

    private static final Long ID= 1L;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void test_findById() {
        ClientVendorDto dto = clientVendorService.findById(ID);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(ID);
    }
    @Test
    void test_findById_notFound() {
        Throwable throwable = catchThrowable(() -> clientVendorService.findById(-1L));
        assertThat(throwable).isInstanceOf(ClientVendorNotFoundException.class);
    }

    @Test
    void test_listAllClientVendors() {
        List<ClientVendorDto> result = clientVendorService.listAllClientVendors();
        ClientVendorDto dto = clientVendorService.findById(ID);

        assertNotNull(dto);
        assertFalse(result.isEmpty());
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(dto);
    }

    @Test
    void test_listByClientVendorType() {
        List<ClientVendorDto> result = clientVendorService.listAllByClientVendorType(ClientVendorType.CLIENT);

        assertThat(result).isNotNull();
        assertEquals(result.size(), 2);
        result.forEach(each-> assertThat(each.getClientVendorType()).isEqualTo(ClientVendorType.CLIENT));
    }

    @Test
    void test_saveClientVendor() {
        ClientVendorDto dto = TestDocInitializer.getClientVendorDto(ClientVendorType.CLIENT);
        ClientVendorDto result = clientVendorService.saveClientVendor(dto);

        assertThat(result).usingRecursiveComparison()
                .ignoringFields("address")
                .isEqualTo(dto);
    }

    @Test
    void test_updateClientVendor() {
        ClientVendorDto dto = clientVendorService.findById(ID);
        dto.setClientVendorName("Updated Name");
        dto.setClientVendorType(ClientVendorType.VENDOR);
        ClientVendorDto result = clientVendorService.updateClientVendor(dto);
        assertThat(result).usingRecursiveComparison().isEqualTo(dto);
    }

    @Test
    void test_deleteClientVendor() {
        clientVendorService.deleteClientVendor(ID);
        ClientVendor result = clientVendorRepository.findById(ID).orElseThrow();
        assertTrue(result.getIsDeleted());
        assertTrue(result.getAddress().getIsDeleted());
        assertThat(result.getClientVendorName())
                .isEqualTo("Orange Tech-1");
    }

    @Test
    void test_isClientVendorNameExists() {
        ClientVendorDto dto = clientVendorService.findById(ID);
//        ClientVendorDto dto = new ClientVendorDto();
        dto.setClientVendorName("Photobug Tech");
        boolean result = clientVendorService.isClientVendorNameExists(dto);
        assertTrue(result);
    }
    @Test
    void test_isClientVendorNameExists_ThrowException() {
        ClientVendorDto dto = new ClientVendorDto();
//        ClientVendorDto dto = clientVendorService.findById(1L);
        dto.setClientVendorName("###)&!");
        boolean result = clientVendorService.isClientVendorNameExists(dto);
        assertFalse(result);
    }

}
