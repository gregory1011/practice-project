package com.app.service.unit;


import com.app.dto.AddressDto;
import com.app.dto.ClientVendorDto;
import com.app.dto.CompanyDto;
import com.app.entity.Address;
import com.app.entity.ClientVendor;
import com.app.entity.Company;
import com.app.entity.Invoice;
import com.app.enums.ClientVendorType;
import com.app.exceptions.ClientVendorNotFoundException;
import com.app.repository.ClientVendorRepository;
import com.app.service.CompanyService;
import com.app.service.TestDocInitializer;
import com.app.service.impl.ClientVendorServiceImpl;
import com.app.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ClientVendorImpl_UnitTest {

    @Mock
    private ClientVendorRepository clientVendorRepository;
    @Spy
    private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @Mock
    private CompanyService companyService;
    @InjectMocks
    private ClientVendorServiceImpl clientVendorService;

    private ClientVendor clientVendor;
    private ClientVendorDto clientVendorDto;
    private CompanyDto companyDto;

    @BeforeEach
    void setUp() {

        AddressDto addressDto = TestDocInitializer.getAddress();
        companyDto = new CompanyDto();
        companyDto.setId(1L);
        companyDto.setTitle("test company");

        clientVendorDto = new ClientVendorDto();
        clientVendorDto.setId(1L);
        clientVendorDto.setClientVendorName("test client vendor");
        clientVendorDto.setCompany(companyDto);
        clientVendorDto.setAddress(addressDto);

        clientVendor = new ClientVendor();
        clientVendor.setId(1L);
        clientVendor.setClientVendorName("test client vendor");
        clientVendor.setCompany(mapperUtil.convert(companyDto, new Company()));
        clientVendor.setInvoices(List.of(new Invoice()));
        clientVendor.setAddress(mapperUtil.convert(addressDto, new Address()));
    }

    @Test
    void findById_shouldReturnClientVendorDto() {
        //when
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(clientVendor));
        ClientVendorDto result = clientVendorService.findById(1L);
        assertThat(result).usingRecursiveComparison().isEqualTo(clientVendorDto);
        verify(clientVendorRepository, times(1)).findById(anyLong());
    }
    @Test
    void findById_shouldThrowException() {
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> clientVendorService.findById(1L));
        assertThat(throwable).isInstanceOf(ClientVendorNotFoundException.class);
    }

    @Test
    void listAlLClientVendors_shouldReturnClientVendorList() {
        clientVendorDto.setHasInvoice(true);
        //when
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDto);
        when(clientVendorRepository.findByCompanyId(anyLong())).thenReturn(Arrays.asList(clientVendor));
        List<ClientVendorDto> result = clientVendorService.listAllClientVendors();
        assertThat(result).isNotNull();
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(clientVendorDto);
    }

    @Test
    void listByClientVendorType_shouldReturnClientVendorList() {
        clientVendorDto.setClientVendorType(ClientVendorType.CLIENT);
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDto);

        clientVendor.setClientVendorType(ClientVendorType.CLIENT);
        when(clientVendorRepository.findAll()).thenReturn(Arrays.asList(clientVendor));

        List<ClientVendorDto> result = clientVendorService.listAllByClientVendorType(ClientVendorType.CLIENT);
        assertThat(result).isNotNull();
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(clientVendorDto);

    }

    @Test
    void saveClientVendor_shouldReturnSavedClientVendor() {
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDto);
        when(clientVendorRepository.save(any(ClientVendor.class))).thenReturn(clientVendor);

        ClientVendorDto result = clientVendorService.saveClientVendor(clientVendorDto);
        assertThat(result).usingRecursiveComparison().isEqualTo(clientVendorDto);
    }

    @Test
    void updateClientVendor_shouldReturnClientVendor() {

        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDto);
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(clientVendor));
        when(clientVendorRepository.save(any(ClientVendor.class))).thenReturn(clientVendor);

        ClientVendorDto result = clientVendorService.updateClientVendor(clientVendorDto);
        assertThat(result).usingRecursiveComparison().isEqualTo(clientVendorDto);
    }
    @Test
    void updateClientVendor_shouldThrowException() {
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> clientVendorService.updateClientVendor(clientVendorDto));
        assertThat(throwable).isInstanceOf(ClientVendorNotFoundException.class);
    }

    @Test
    void deleteClientVendor_shouldMarkClientVendorAsDeleted() {
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(clientVendor));
        when(clientVendorRepository.save(any(ClientVendor.class))).thenReturn(clientVendor);

        clientVendorService.deleteClientVendor(clientVendor.getId());

        assertThat(clientVendor).isNotNull();
        assertTrue(clientVendor.getIsDeleted());
        assertTrue(clientVendor.getAddress().getIsDeleted());
        assertThat(clientVendor.getClientVendorName()).isEqualTo(clientVendorDto.getClientVendorName()+"-"+1);
        verify(clientVendorRepository, times(1)).save(any(ClientVendor.class));
    }

    @Test
    void isClientVendorNameExist_shouldReturnTrueIfClientVendorNameExists() {
        clientVendorDto.setId(1L);
        clientVendor.setId(2L);
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDto);
        when(clientVendorRepository.existsByCompanyIdAndClientVendorName(anyLong(), anyString())).thenReturn(Optional.of(clientVendor));

        boolean result = clientVendorService.isClientVendorNameExists(clientVendorDto.getClientVendorName());
        assertTrue(result);
    }
    @Test
    void isClientVendorNameExist_shouldReturnFalseIfClientVendorNameDoesNotExist() {
        clientVendorDto.setId(1L);
        clientVendor.setId(2L);
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDto);
        when(clientVendorRepository.existsByCompanyIdAndClientVendorName(anyLong(), anyString())).thenReturn(Optional.empty());

        boolean result = clientVendorService.isClientVendorNameExists(clientVendorDto.getClientVendorName());
        assertFalse(result);
    }

}
