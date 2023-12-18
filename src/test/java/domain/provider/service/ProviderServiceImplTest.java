package domain.provider.service;

import com.rushhour_app.domain.account.model.EmployeeAccountDTO;
import com.rushhour_app.domain.employee.model.EmployeeDTO;
import com.rushhour_app.domain.provider.entity.Provider;
import com.rushhour_app.domain.provider.model.ProviderDTO;
import com.rushhour_app.domain.provider.model.ProviderResponseDTO;
import com.rushhour_app.domain.provider.model.ProviderUpdateDTO;
import com.rushhour_app.domain.provider.repository.ProviderRepository;
import com.rushhour_app.domain.provider.service.ProviderService;
import com.rushhour_app.domain.provider.service.ProviderServiceImpl;
import com.rushhour_app.domain.role.model.RoleDTO;
import com.rushhour_app.infrastructure.exception.customException.ConflictException;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.mappers.ProviderMapper;
import domain.utils.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ProviderServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ProviderServiceImplTest {

    @MockBean
    private ProviderMapper providerMapper;

    @MockBean
    private ProviderRepository providerRepository;

    @Autowired
    private ProviderService providerService;

    @Test
    void testFindByIdProvider() {
        Provider provider = new Provider();
        Util.setProvider(provider);
        provider.setWorkingDays(new ArrayList<>());
        Optional<Provider> ofResult = Optional.of(provider);
        LocalDate date = LocalDate.ofEpochDay(1L);

        when(providerRepository.findById(any())).thenReturn(ofResult);

        RoleDTO roleDTO = new RoleDTO(3L, "ROLE");
        EmployeeAccountDTO accountDTO = new EmployeeAccountDTO(1L, "mail@gmail.com", "David", "David!123", 1L);
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "title", "123123123", 10.0d, date, 1L, accountDTO);
        Optional<Provider> optionalProvider = providerService.findProviderById(employeeDTO);

        assertSame(ofResult, optionalProvider);
        assertTrue(optionalProvider.isPresent());
        verify(providerRepository).findById(any());
    }

    @Test
    void testFindByIdProvider2() {
        when(providerRepository.findById(any())).thenThrow(new NotFoundException("Error"));
        LocalDate date = LocalDate.ofEpochDay(1L);

        RoleDTO roleDTO = new RoleDTO(3L, "ROLE");
        EmployeeAccountDTO accountDTO = new EmployeeAccountDTO(1L, "mail@gmail.com", "David", "David!123", 1L);
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "Title", "1234123123", 11.0d, date, 1L, accountDTO);

        assertThrows(NotFoundException.class, () -> providerService.findProviderById(employeeDTO));
        verify(providerRepository).findById(any());
    }

    @Test
    void testGetProviderById3() {
        Provider provider = new Provider();
        Util.setProvider(provider);
        Optional<Provider> ofResult = Optional.of(provider);

        when(providerRepository.findById(any())).thenReturn(ofResult);
        assertSame(provider, providerService.getProviderById(1L));
        verify(providerRepository).findById(any());
    }

    @Test
    void testGetProviderById4() {
        when(providerRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> providerService.getProviderById(1L));
        verify(providerRepository).findById(any());
    }

    @Test
    void testGetProviderById5() {
        when(providerRepository.findById(any())).thenThrow(new NotFoundException("Error"));
        assertThrows(NotFoundException.class, () -> providerService.getProviderById(1L));
        verify(providerRepository).findById(any());
    }

    @Test
    void testExistsByProviderId() {
        when(providerRepository.existsById(any())).thenReturn(true);
        LocalDate date = LocalDate.ofEpochDay(1L);

        RoleDTO roleDTO = new RoleDTO(3L, "ROLE");
        EmployeeAccountDTO accountDTO = new EmployeeAccountDTO(1L, "mail@gmail.com", "David", "David!123", 1L);
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "Title", "1234123123", 11.0d, date, 1L, accountDTO);

        providerService.existsByProviderId(employeeDTO);
        verify(providerRepository).existsById(any());
    }

    @Test
    void testExistsByProviderId2() {
        when(providerRepository.existsById(any())).thenReturn(false);
        LocalDate date = LocalDate.ofEpochDay(1L);

        RoleDTO roleDTO = new RoleDTO(3L, "ROLE");
        EmployeeAccountDTO accountDTO = new EmployeeAccountDTO(1L, "mail@gmail.com", "David", "David!123", 1L);
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "Title", "1234123123", 11.0d, date, 1L, accountDTO);

        assertThrows(NotFoundException.class, () -> providerService.existsByProviderId(employeeDTO));
        verify(providerRepository).existsById(any());
    }

    @Test
    void testExistsByProviderId3() {
        when(providerRepository.existsById((Long) any())).thenThrow(new NotFoundException("An error occurred"));
        LocalDate date = LocalDate.ofEpochDay(1L);

        RoleDTO roleDTO = new RoleDTO(3L, "ROLE");
        EmployeeAccountDTO accountDTO = new EmployeeAccountDTO(1L, "mail@gmail.com", "David", "David!123", 1L);
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "Title", "1234123123", 11.0d, date, 1L, accountDTO);

        assertThrows(NotFoundException.class, () -> providerService.existsByProviderId(employeeDTO));
        verify(providerRepository).existsById((Long) any());
    }


    @Test
    void testCreate() {
        Provider provider = new Provider();
        Util.setProvider(provider);
        LocalTime startTime = LocalTime.of(1, 1);
        LocalTime endTime = LocalTime.of(1, 1);
        ProviderDTO providerDTO = new ProviderDTO("Name", "Website", "Domain", "123123123", startTime, endTime, new ArrayList<>());

        when(providerRepository.existsByDomain(any())).thenReturn(true);
        when(providerRepository.existsByName(any())).thenReturn(true);
        when(providerRepository.save(any())).thenReturn(provider);
        assertThrows(ConflictException.class, () -> providerService.create(providerDTO));

        verify(providerRepository).existsByDomain(any());
        verify(providerRepository).existsByName(any());
    }

    @Test
    void testCreate2() {
        Provider provider = new Provider();
        Util.setProvider(provider);
        LocalTime startTime = LocalTime.of(1, 1);
        LocalTime endTime = LocalTime.of(1, 1);
        ProviderDTO providerDTO = new ProviderDTO( "Name", "Website", "Domain", "123123123", startTime, endTime, new ArrayList<>());

        when(providerRepository.existsByDomain(any())).thenReturn(true);

        when(providerRepository.existsByName(any())).thenReturn(false);

        when(providerRepository.save(any())).thenReturn(provider);
        assertThrows(ConflictException.class, () -> providerService.create(providerDTO));

        verify(providerRepository).existsByDomain(any());
        verify(providerRepository).existsByName(any());

    }

    @Test
    void testCreate3() {
        LocalTime startTime = LocalTime.of(1, 1);
        LocalTime endTime = LocalTime.of(1, 1);
        ProviderDTO providerDTO = new ProviderDTO( "Name", "Website", "Domain", "123123123", startTime, endTime, new ArrayList<>());

        when(providerRepository.existsByDomain(any())).thenThrow(new ConflictException("Error"));
        when(providerRepository.existsByName(any())).thenThrow(new ConflictException("Error"));
        when(providerRepository.save(any())).thenThrow(new ConflictException("Error"));

        assertThrows(ConflictException.class, () -> providerService.create(providerDTO));
        verify(providerRepository).existsByName(any());
    }

    @Test
    void testGetById() {
        Provider provider = new Provider();
        Util.setProvider(provider);

        LocalTime startTime = LocalTime.of(1, 1);
        LocalTime endTime = LocalTime.of(1, 1);
        ProviderResponseDTO prd = new ProviderResponseDTO(1L, "Name", "Website", "Domain", "4105551212", startTime, endTime, new ArrayList<>());
        Optional<Provider> ofResult = Optional.of(provider);

        when(providerRepository.findById(any())).thenReturn(ofResult);
        when(providerMapper.toProviderResponseDTO(any())).thenReturn(prd);
        assertSame(prd, providerService.getById(1L));
        verify(providerRepository).findById(any());
        verify(providerMapper).toProviderResponseDTO(any());
    }

    @Test
    void testGetById2() {
        Provider provider = new Provider();
        Util.setProvider(provider);
        Optional<Provider> ofResult = Optional.of(provider);

        when(providerRepository.findById(any())).thenReturn(ofResult);
        when(providerMapper.toProviderResponseDTO(any())).thenThrow(new NotFoundException("Error"));
        assertThrows(NotFoundException.class, () -> providerService.getById(1L));

        verify(providerRepository).findById(any());
        verify(providerMapper).toProviderResponseDTO(any());
    }

    @Test
    void testGetById3() {
        when(providerRepository.findById(any())).thenReturn(Optional.empty());
        LocalTime startTime = LocalTime.of(1, 1);
        LocalTime endTime = LocalTime.of(1, 1);
        ProviderResponseDTO prd = new ProviderResponseDTO(1L, "Name", "Website", "Domain", "4105551212", startTime, endTime, new ArrayList<>());

        when(providerMapper.toProviderResponseDTO(any())).thenReturn(prd);
        assertThrows(NotFoundException.class, () -> providerService.getById(1L));

        verify(providerRepository).findById(any());
    }

    @Test
    void testGetPage() {
        when(providerRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(providerService.getPage(null).toList().isEmpty());
        verify(providerRepository).findAll((Pageable) any());
    }

    @Test
    void testDelete() {
        doNothing().when(providerRepository).deleteById(any());
        when(providerRepository.existsById(any())).thenReturn(true);
        providerService.delete(1L);
        verify(providerRepository).existsById(any());
        verify(providerRepository).deleteById(any());
    }

    @Test
    void testUpdate() {
        Provider provider = new Provider();
        Util.setProvider(provider);
        Optional<Provider> ofResult = Optional.of(provider);
        Provider provider1 = new Provider();
        Util.setProvider(provider1);
        LocalTime startTime = LocalTime.of(1, 1);
        LocalTime endTime = LocalTime.of(1, 1);
        ProviderResponseDTO providerResponseDTO = new ProviderResponseDTO(1L, "Name", "Website", "Domain", "4105551212", startTime, endTime, new ArrayList<>());

        when(providerRepository.save(any())).thenReturn(provider1);
        when(providerRepository.findById(any())).thenReturn(ofResult);
        when(providerMapper.toProviderResponseDTO(any())).thenReturn(providerResponseDTO);
        doNothing().when(providerMapper).updateProviderFromDto(any(), any());
        LocalTime startTime1 = LocalTime.of(1, 1);
        assertSame(providerResponseDTO, providerService.update(new ProviderUpdateDTO("Name1", "123123123", startTime1, LocalTime.of(1, 1)), 1L));

        verify(providerRepository).save(any());
        verify(providerRepository).findById(any());
        verify(providerMapper).toProviderResponseDTO(any());
        verify(providerMapper).updateProviderFromDto(any(), any());
    }

    @Test
    void testUpdate2() {
        Provider provider = new Provider();
        Util.setProvider(provider);
        Optional<Provider> ofResult = Optional.of(provider);
        LocalTime startTime = LocalTime.of(1, 1);

        when(providerRepository.save(any())).thenReturn(provider);
        when(providerRepository.findById(any())).thenReturn(ofResult);
        when(providerMapper.toProviderResponseDTO(any())).thenThrow(new ConflictException("Error"));
        doThrow(new ConflictException("Error")).when(providerMapper).updateProviderFromDto(any(), any());
        assertThrows(ConflictException.class, () -> providerService.update(new ProviderUpdateDTO("Name1", "1231232", startTime, LocalTime.of(1, 1)), 1L));

        verify(providerRepository).findById(any());
        verify(providerMapper).updateProviderFromDto(any(), any());
    }

    @Test
    void testUpdate3() {
        Provider provider = new Provider();
        Util.setProvider(provider);
        LocalTime startTime = LocalTime.of(1, 1);
        LocalTime endTime = LocalTime.of(1, 1);
        LocalTime startTime1 = LocalTime.of(1, 1);

        when(providerRepository.save(any())).thenReturn(provider);
        when(providerRepository.findById(any())).thenReturn(Optional.empty());
        when(providerMapper.toProviderResponseDTO(any())).thenReturn(new ProviderResponseDTO(1L, "Name", "Website", "Domain", "12312332223", startTime, endTime, new ArrayList<>()));
        doNothing().when(providerMapper).updateProviderFromDto(any(), any());
        assertThrows(NotFoundException.class, () -> providerService.update(new ProviderUpdateDTO("Name1", "12312123", startTime1, LocalTime.of(1, 1)), 1L));
        verify(providerRepository).findById(any());
    }
}

