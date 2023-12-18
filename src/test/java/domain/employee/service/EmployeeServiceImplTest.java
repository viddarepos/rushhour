package domain.employee.service;

import com.rushhour_app.domain.account.entity.Account;
import com.rushhour_app.domain.account.model.AccountResponseDTO;
import com.rushhour_app.domain.account.model.AccountUpdateDTO;
import com.rushhour_app.domain.account.model.EmployeeAccountDTO;
import com.rushhour_app.domain.account.service.AccountServiceImpl;
import com.rushhour_app.domain.employee.entity.Employee;
import com.rushhour_app.domain.employee.model.EmployeeDTO;
import com.rushhour_app.domain.employee.model.EmployeeResponseDTO;
import com.rushhour_app.domain.employee.model.EmployeeUpdateDTO;
import com.rushhour_app.domain.employee.repository.EmployeeRepository;
import com.rushhour_app.domain.employee.service.EmployeeServiceImpl;
import com.rushhour_app.domain.provider.entity.Provider;
import com.rushhour_app.domain.provider.repository.ProviderRepository;
import com.rushhour_app.domain.provider.service.ProviderServiceImpl;
import com.rushhour_app.domain.role.entity.Role;
import com.rushhour_app.domain.role.model.RoleDTO;
import com.rushhour_app.domain.role.repository.RoleRepository;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.mappers.EmployeeMapper;
import domain.utils.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ContextConfiguration(classes = {EmployeeServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EmployeeServiceImplTest {

    @MockBean
    private AccountServiceImpl accountService;

    @MockBean
    private EmployeeMapper employeeMapper;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeService;

    @MockBean
    private ProviderRepository providerRepository;

    @MockBean
    private ProviderServiceImpl providerService;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    void testCreate() {

        Account account = new Account();
        Util.setAccount(account);
        Provider provider = new Provider();
        Util.setProvider(provider);
        Employee employee = new Employee();
        Util.setEmployee(employee, account, provider);
        LocalDate date = LocalDate.ofEpochDay(1L);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1L, "Title", "123123123", 10.0d, date, 1L,
                new AccountResponseDTO(1L, "david@hotmail.com", "David", new RoleDTO(1L, "Name")));

        when(employeeRepository.save(any())).thenReturn(employee);
        when(employeeMapper.toEmployeeResponseDTO(any())).thenReturn(employeeResponseDTO);
        when(employeeMapper.toEmployee(any())).thenReturn(employee);
        Optional<Provider> ofResult = Optional.of(provider);
        when(providerService.findProviderById(any())).thenReturn(ofResult);
        doNothing().when(providerService).existsByProviderId(any());
        doNothing().when(accountService).assertEmployeeAccountDoesNotExists(any());
        LocalDate date1 = LocalDate.ofEpochDay(1L);
        assertSame(employeeResponseDTO, employeeService.create(new EmployeeDTO(1L, "Title", "1231231", 10.0d, date1,
                123L, new EmployeeAccountDTO(1L, "mejl@hotmail.com", "David", "David123@", 1L))));
        verify(employeeRepository).save(any());
        verify(employeeMapper).toEmployee(any());
        verify(employeeMapper).toEmployeeResponseDTO(any());
        verify(providerService).findProviderById(any());
        verify(providerService).existsByProviderId(any());
        verify(accountService).assertEmployeeAccountDoesNotExists(any());
    }

    @Test
    void testGetById() {
        Account account = new Account();
        Util.setAccount(account);
        Provider provider = new Provider();
        Util.setProvider(provider);
        Employee employee = new Employee();
        Util.setEmployee(employee, account, provider);
        Optional<Employee> ofResult = Optional.of(employee);
        LocalDate date = LocalDate.ofEpochDay(1L);

        RoleDTO roleDTO = new RoleDTO(1L, "CLIENT");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "email@mgial.com", "David", roleDTO);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1L, "Title", "123123123", 120.0d, date, 1L, accountResponseDTO);

        when(employeeRepository.findById(any())).thenReturn(ofResult);
        when(employeeMapper.toEmployeeResponseDTO(any())).thenReturn(employeeResponseDTO);
        assertSame(employeeResponseDTO, employeeService.getById(1L));
        verify(employeeRepository).findById(any());
        verify(employeeMapper).toEmployeeResponseDTO(any());
    }

    @Test
    void testGetById2() {
        when(employeeMapper.toEmployeeResponseDTO(any())).thenThrow(new NotFoundException("Error"));
        assertThrows(NotFoundException.class, () -> employeeService.getById(1L));
    }

    @Test
    void testGetPage() {
        when(employeeRepository.getAllByProviderId( any(),  any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(employeeService.getAll(null, 1L).toList().isEmpty());
        verify(employeeRepository).getAllByProviderId( any(),  any());
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(any());
        when(employeeRepository.existsById(any())).thenReturn(true);
        employeeService.deleteEmployee(1L);
        verify(employeeRepository).existsById(any());
        verify(employeeRepository).deleteById(any());
    }

    @Test
    void testDeleteEmployee2() {
        doThrow(new NotFoundException("Error")).when(employeeRepository).deleteById(any());
        assertThrows(NotFoundException.class, () -> employeeService.deleteEmployee(1L));
    }

    @Test
    void testDeleteEmployee3() {
        when(employeeRepository.existsById(any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> employeeService.deleteEmployee(1L));
        verify(employeeRepository).existsById(any());
    }

    @Test
    void testUpdate() {
        Account account = new Account();
        Util.setAccount(account);
        Provider provider = new Provider();
        Util.setProvider(provider);
        Employee employee = new Employee();
        Util.setEmployee(employee, account, provider);
        Optional<Employee> ofResult = Optional.of(employee);
        LocalDate date = LocalDate.ofEpochDay(1L);

        RoleDTO roleDTO = new RoleDTO(1L, "CLIENT");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "email@mgial.com", "David", roleDTO);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1L, "Title", "12332344534", 11.0d, date, 1L, accountResponseDTO);

        when(employeeRepository.save(any())).thenReturn(employee);
        when(employeeRepository.findById(any())).thenReturn(ofResult);
        when(employeeMapper.toEmployeeResponseDTO(any())).thenReturn(employeeResponseDTO);
        doNothing().when(employeeMapper).updateEmployeeFromDto(any(), any());
        assertSame(employeeResponseDTO, employeeService
                .update(new EmployeeUpdateDTO("Title1", new AccountUpdateDTO("David1", "Morning123@")), 1L));
        verify(employeeRepository).save(any());
        verify(employeeRepository).findById(any());
        verify(employeeMapper).toEmployeeResponseDTO(any());
        verify(employeeMapper).updateEmployeeFromDto(any(), any());
    }

    @Test
    void testUpdate2() {
        doThrow(new NotFoundException("Error")).when(employeeMapper)
                .updateEmployeeFromDto(any(), any());
        assertThrows(NotFoundException.class, () -> employeeService
                .update(new EmployeeUpdateDTO("Title1", new AccountUpdateDTO("David123", "Password1#@")), 1L));
    }
}

