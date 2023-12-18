package domain.appointment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rushhour_app.domain.account.entity.Account;
import com.rushhour_app.domain.account.model.AccountResponseDTO;
import com.rushhour_app.domain.account.model.ClientAccountDTO;
import com.rushhour_app.domain.account.repository.AccountRepository;
import com.rushhour_app.domain.account.service.AccountServiceImpl;
import com.rushhour_app.domain.activity.entity.Activity;
import com.rushhour_app.domain.activity.models.ActivityResponseDTO;
import com.rushhour_app.domain.activity.repository.ActivityRepository;
import com.rushhour_app.domain.activity.service.ActivityService;
import com.rushhour_app.domain.activity.service.ActivityServiceImpl;
import com.rushhour_app.domain.appointment.entity.Appointment;
import com.rushhour_app.domain.appointment.models.AppointmentClientCreateDTO;
import com.rushhour_app.domain.appointment.models.AppointmentDTO;
import com.rushhour_app.domain.appointment.models.AppointmentResponseDTO;
import com.rushhour_app.domain.appointment.models.AppointmentUpdateDTO;
import com.rushhour_app.domain.appointment.repository.AppointmentRepository;
import com.rushhour_app.domain.appointment.services.AppointmentServiceImpl;
import com.rushhour_app.domain.client.entity.Client;
import com.rushhour_app.domain.client.model.ClientResponseDTO;
import com.rushhour_app.domain.client.repository.ClientRepository;
import com.rushhour_app.domain.client.service.ClientServiceImpl;
import com.rushhour_app.domain.employee.entity.Employee;
import com.rushhour_app.domain.employee.model.EmployeeResponseDTO;
import com.rushhour_app.domain.employee.repository.EmployeeRepository;
import com.rushhour_app.domain.employee.service.EmployeeServiceImpl;
import com.rushhour_app.domain.provider.entity.Provider;
import com.rushhour_app.domain.provider.repository.ProviderRepository;
import com.rushhour_app.domain.provider.service.ProviderService;
import com.rushhour_app.domain.provider.service.ProviderServiceImpl;
import com.rushhour_app.domain.role.entity.Role;
import com.rushhour_app.domain.role.model.RoleDTO;
import com.rushhour_app.domain.role.repository.RoleRepository;
import com.rushhour_app.domain.role.service.RoleServiceImpl;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.mappers.ActivityMapperImpl;
import com.rushhour_app.infrastructure.mappers.AppointmentMapper;
import com.rushhour_app.infrastructure.mappers.AppointmentMapperImpl;
import com.rushhour_app.infrastructure.mappers.ClientMapperImpl;
import com.rushhour_app.infrastructure.mappers.EmployeeMapperImpl;
import com.rushhour_app.infrastructure.mappers.ProviderMapperImpl;
import com.rushhour_app.infrastructure.security.model.AccountUserDetails;
import com.rushhour_app.infrastructure.smtp.MyMailSender;

import java.time.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AppointmentServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AppointmentServiceImplTest {
    @MockBean
    private ActivityService activityService;

    @MockBean
    private AppointmentMapper appointmentMapper;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentServiceImpl appointmentServiceImpl;

    @MockBean
    private ClientServiceImpl clientServiceImpl;

    @MockBean
    private EmployeeServiceImpl employeeServiceImpl;

    @MockBean
    private MyMailSender myMailSender;

    @MockBean
    private ProviderService providerService;

    @Test
    void testFindAllByActivityId() {
        ArrayList<Appointment> appointmentList = new ArrayList<>();
        when(appointmentRepository.findAllByActivityId((Long) any())).thenReturn(appointmentList);
        List<Appointment> actualFindAllByActivityIdResult = appointmentServiceImpl.findAllByActivityId(123L);
        assertSame(appointmentList, actualFindAllByActivityIdResult);
        assertTrue(actualFindAllByActivityIdResult.isEmpty());
        verify(appointmentRepository).findAllByActivityId((Long) any());
    }

    @Test
    void testFindAllByActivityId2() {
        when(appointmentRepository.findAllByActivityId((Long) any()))
                .thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> appointmentServiceImpl.findAllByActivityId(123L));
        verify(appointmentRepository).findAllByActivityId((Long) any());
    }

    @Test
    void testCreate() {
        Provider provider = new Provider();
        provider.setActivities(new ArrayList<>());
        provider.setDomain("Domain");
        provider.setEndTime(LocalTime.of(1, 1));
        provider.setId(123L);
        provider.setName("Name");
        provider.setPhone("4105551212");
        provider.setStartTime(LocalTime.of(1, 1));
        provider.setWebsite("Website");
        provider.setWorkingDays(new ArrayList<>());
        Activity activity = mock(Activity.class);
        when(activity.getDuration()).thenReturn(null);
        doNothing().when(activity).setAppointmentList((List<Appointment>) any());
        doNothing().when(activity).setDuration((Duration) any());
        doNothing().when(activity).setEmployees((List<Employee>) any());
        doNothing().when(activity).setId((Long) any());
        doNothing().when(activity).setName((String) any());
        doNothing().when(activity).setPrice((Double) any());
        doNothing().when(activity).setProvider((Provider) any());
        activity.setAppointmentList(new ArrayList<>());
        activity.setDuration(null);
        activity.setEmployees(new ArrayList<>());
        activity.setId(123L);
        activity.setName("Name");
        activity.setPrice(10.0d);
        activity.setProvider(provider);

        Provider provider1 = new Provider();
        provider1.setActivities(new ArrayList<>());
        provider1.setDomain("Domain");
        provider1.setEndTime(LocalTime.of(1, 1));
        provider1.setId(123L);
        provider1.setName("Name");
        provider1.setPhone("4105551212");
        provider1.setStartTime(LocalTime.of(1, 1));
        provider1.setWebsite("Website");
        provider1.setWorkingDays(new ArrayList<>());

        Activity activity1 = new Activity();
        activity1.setAppointmentList(new ArrayList<>());
        activity1.setDuration(null);
        activity1.setEmployees(new ArrayList<>());
        activity1.setId(123L);
        activity1.setName("Name");
        activity1.setPrice(10.0d);
        activity1.setProvider(provider1);

        Role role = new Role();
        role.setId(123L);
        role.setName("Name");

        Account account = new Account();
        account.setEmail("david@hotmail.com");
        account.setFullName("David");
        account.setId(123L);
        account.setPassword("Password");
        account.setRole(role);

        Client client = new Client();
        client.setAccount(account);
        client.setAddress("42 Main St");
        client.setAppointmentList(new ArrayList<>());
        client.setId(123L);
        client.setPhone("4105551212");

        Role role1 = new Role();
        role1.setId(123L);
        role1.setName("Name");

        Account account1 = new Account();
        account1.setEmail("david@hotmail.com");
        account1.setFullName("David");
        account1.setId(123L);
        account1.setPassword("Password");
        account1.setRole(role1);

        Provider provider2 = new Provider();
        provider2.setActivities(new ArrayList<>());
        provider2.setDomain("Domain");
        provider2.setEndTime(LocalTime.of(1, 1));
        provider2.setId(123L);
        provider2.setName("Name");
        provider2.setPhone("4105551212");
        provider2.setStartTime(LocalTime.of(1, 1));
        provider2.setWebsite("Website");
        provider2.setWorkingDays(new ArrayList<>());

        Employee employee = new Employee();
        employee.setAccount(account1);
        employee.setActivities(new ArrayList<>());
        employee.setAppointmentList(new ArrayList<>());
        employee.setDate(LocalDate.ofEpochDay(1L));
        employee.setId(123L);
        employee.setPhone("4105551212");
        employee.setProvider(provider2);
        employee.setRate(10.0d);
        employee.setTitle("Dr");

        Appointment appointment = new Appointment();
        appointment.setActivity(activity1);
        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        appointment.setId(123L);
        appointment.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
        when(appointmentRepository.save((Appointment) any())).thenReturn(appointment);
        when(appointmentRepository.checkIfEmployeeIsBusy((Long) any(), (LocalDateTime) any(), (LocalDateTime) any()))
                .thenReturn(new ArrayList<>());

        Role role2 = new Role();
        role2.setId(123L);
        role2.setName("Name");

        Account account2 = new Account();
        account2.setEmail("david@hotmail.com");
        account2.setFullName("David");
        account2.setId(123L);
        account2.setPassword("Password");
        account2.setRole(role2);

        Client client1 = new Client();
        client1.setAccount(account2);
        client1.setAddress("42 Main St");
        client1.setAppointmentList(new ArrayList<>());
        client1.setId(123L);
        client1.setPhone("4105551212");
        ClientRepository clientRepository = mock(ClientRepository.class);
        when(clientRepository.findById((Long) any())).thenReturn(Optional.of(client1));
        ClientMapperImpl clientMapper = new ClientMapperImpl();
        RoleServiceImpl roleService = new RoleServiceImpl(mock(RoleRepository.class));
        ClientServiceImpl clientService = new ClientServiceImpl(clientRepository, clientMapper, roleService,
                new AccountServiceImpl(mock(AccountRepository.class)));

        Role role3 = new Role();
        role3.setId(123L);
        role3.setName("Name");

        Account account3 = new Account();
        account3.setEmail("david@hotmail.com");
        account3.setFullName("David");
        account3.setId(123L);
        account3.setPassword("Password");
        account3.setRole(role3);

        Provider provider3 = new Provider();
        provider3.setActivities(new ArrayList<>());
        provider3.setDomain("Domain");
        provider3.setEndTime(LocalTime.of(1, 1));
        provider3.setId(123L);
        provider3.setName("Name");
        provider3.setPhone("4105551212");
        provider3.setStartTime(LocalTime.of(1, 1));
        provider3.setWebsite("Website");
        provider3.setWorkingDays(new ArrayList<>());

        Employee employee1 = new Employee();
        employee1.setAccount(account3);
        employee1.setActivities(new ArrayList<>());
        employee1.setAppointmentList(new ArrayList<>());
        employee1.setDate(LocalDate.ofEpochDay(1L));
        employee1.setId(123L);
        employee1.setPhone("4105551212");
        employee1.setProvider(provider3);
        employee1.setRate(10.0d);
        employee1.setTitle("Dr");
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        when(employeeRepository.findById((Long) any())).thenReturn(Optional.of(employee1));
        EmployeeMapperImpl employeeMapper = new EmployeeMapperImpl();
        ProviderRepository providerRepository = mock(ProviderRepository.class);
        ProviderServiceImpl providerService = new ProviderServiceImpl(providerRepository, new ProviderMapperImpl());

        EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepository, employeeMapper, providerService,
                new AccountServiceImpl(mock(AccountRepository.class)));

        ActivityRepository activityRepository = mock(ActivityRepository.class);
        when(activityRepository.findById((Long) any())).thenReturn(Optional.empty());
        ActivityMapperImpl activityMapper = new ActivityMapperImpl();
        ProviderRepository providerRepository1 = mock(ProviderRepository.class);
        ProviderServiceImpl providerService1 = new ProviderServiceImpl(providerRepository1, new ProviderMapperImpl());

        EmployeeRepository employeeRepository1 = mock(EmployeeRepository.class);
        EmployeeMapperImpl employeeMapper1 = new EmployeeMapperImpl();
        ProviderRepository providerRepository2 = mock(ProviderRepository.class);
        ProviderServiceImpl providerService2 = new ProviderServiceImpl(providerRepository2, new ProviderMapperImpl());

        EmployeeServiceImpl employeeService1 = new EmployeeServiceImpl(employeeRepository1, employeeMapper1,
                providerService2, new AccountServiceImpl(mock(AccountRepository.class)));

        ClientRepository clientRepository1 = mock(ClientRepository.class);
        ClientMapperImpl clientMapper1 = new ClientMapperImpl();
        RoleServiceImpl roleService1 = new RoleServiceImpl(mock(RoleRepository.class));
        ClientServiceImpl clientService1 = new ClientServiceImpl(clientRepository1, clientMapper1, roleService1,
                new AccountServiceImpl(mock(AccountRepository.class)));

        MyMailSender mailSender = new MyMailSender(new JavaMailSenderImpl());
        AppointmentRepository appointmentRepository1 = mock(AppointmentRepository.class);
        MyMailSender sender = new MyMailSender(new JavaMailSenderImpl());
        AppointmentMapperImpl appointmentMapper = new AppointmentMapperImpl();
        ClientRepository clientRepository2 = mock(ClientRepository.class);
        ClientMapperImpl clientMapper2 = new ClientMapperImpl();
        RoleServiceImpl roleService2 = new RoleServiceImpl(mock(RoleRepository.class));
        ClientServiceImpl clientService2 = new ClientServiceImpl(clientRepository2, clientMapper2, roleService2,
                new AccountServiceImpl(mock(AccountRepository.class)));

        EmployeeRepository employeeRepository2 = mock(EmployeeRepository.class);
        EmployeeMapperImpl employeeMapper2 = new EmployeeMapperImpl();
        ProviderRepository providerRepository3 = mock(ProviderRepository.class);
        ProviderServiceImpl providerService3 = new ProviderServiceImpl(providerRepository3, new ProviderMapperImpl());

        EmployeeServiceImpl employeeService2 = new EmployeeServiceImpl(employeeRepository2, employeeMapper2,
                providerService3, new AccountServiceImpl(mock(AccountRepository.class)));

        ProviderRepository providerRepository4 = mock(ProviderRepository.class);
        ProviderServiceImpl providerService4 = new ProviderServiceImpl(providerRepository4, new ProviderMapperImpl());

        ActivityRepository activityRepository1 = mock(ActivityRepository.class);
        ActivityMapperImpl activityMapper1 = new ActivityMapperImpl();
        ProviderRepository providerRepository5 = mock(ProviderRepository.class);
        ProviderServiceImpl providerService5 = new ProviderServiceImpl(providerRepository5, new ProviderMapperImpl());

        EmployeeRepository employeeRepository3 = mock(EmployeeRepository.class);
        EmployeeServiceImpl employeeService3 = new EmployeeServiceImpl(employeeRepository3, new EmployeeMapperImpl(),
                null, null);

        ClientRepository clientRepository3 = mock(ClientRepository.class);
        ClientServiceImpl clientService3 = new ClientServiceImpl(clientRepository3, new ClientMapperImpl(), null, null);

        MyMailSender mailSender1 = new MyMailSender(new JavaMailSenderImpl());
        AppointmentRepository appointmentRepository2 = mock(AppointmentRepository.class);
        ActivityServiceImpl activityService = new ActivityServiceImpl(activityRepository, activityMapper,
                providerService1, employeeService1, clientService1, mailSender,
                new AppointmentServiceImpl(appointmentRepository1, sender, appointmentMapper, clientService2,
                        employeeService2, providerService4,
                        new ActivityServiceImpl(activityRepository1, activityMapper1, providerService5, employeeService3,
                                clientService3, mailSender1, new AppointmentServiceImpl(appointmentRepository2, null,
                                new AppointmentMapperImpl(), null, null, null, null))));

        MyMailSender sender1 = new MyMailSender(new JavaMailSenderImpl());
        AppointmentMapperImpl appointmentMapper1 = new AppointmentMapperImpl();
        ProviderRepository providerRepository6 = mock(ProviderRepository.class);
        AppointmentServiceImpl appointmentServiceImpl = new AppointmentServiceImpl(appointmentRepository, sender1,
                appointmentMapper1, clientService, employeeService,
                new ProviderServiceImpl(providerRepository6, new ProviderMapperImpl()), activityService);
        assertThrows(NotFoundException.class, () -> appointmentServiceImpl
                .create(new AppointmentDTO(123L, LocalDateTime.of(1, 1, 1, 1, 1), 123L, 123L, 123L)));
        verify(clientRepository).findById((Long) any());
        verify(employeeRepository).findById((Long) any());
        verify(activityRepository).findById((Long) any());
        verify(activity).setAppointmentList((List<Appointment>) any());
        verify(activity).setDuration((Duration) any());
        verify(activity).setEmployees((List<Employee>) any());
        verify(activity).setId((Long) any());
        verify(activity).setName((String) any());
        verify(activity).setPrice((Double) any());
        verify(activity).setProvider((Provider) any());
    }

    @Test
    void testGetAppointmentById() {
        Provider provider = new Provider();
        provider.setActivities(new ArrayList<>());
        provider.setDomain("Domain");
        provider.setEndTime(LocalTime.of(1, 1));
        provider.setId(123L);
        provider.setName("Name");
        provider.setPhone("4105551212");
        provider.setStartTime(LocalTime.of(1, 1));
        provider.setWebsite("Website");
        provider.setWorkingDays(new ArrayList<>());

        Activity activity = new Activity();
        activity.setAppointmentList(new ArrayList<>());
        activity.setDuration(null);
        activity.setEmployees(new ArrayList<>());
        activity.setId(123L);
        activity.setName("Name");
        activity.setPrice(10.0d);
        activity.setProvider(provider);

        Role role = new Role();
        role.setId(123L);
        role.setName("Name");

        Account account = new Account();
        account.setEmail("david@hotmail.com");
        account.setFullName("David");
        account.setId(123L);
        account.setPassword("Password");
        account.setRole(role);

        Client client = new Client();
        client.setAccount(account);
        client.setAddress("42 Main St");
        client.setAppointmentList(new ArrayList<>());
        client.setId(123L);
        client.setPhone("4105551212");

        Role role1 = new Role();
        role1.setId(123L);
        role1.setName("Name");

        Account account1 = new Account();
        account1.setEmail("David@hotmail.com");
        account1.setFullName("David");
        account1.setId(123L);
        account1.setPassword("Password");
        account1.setRole(role1);

        Provider provider1 = new Provider();
        provider1.setActivities(new ArrayList<>());
        provider1.setDomain("Domain");
        provider1.setEndTime(LocalTime.of(1, 1));
        provider1.setId(123L);
        provider1.setName("Name");
        provider1.setPhone("4105551212");
        provider1.setStartTime(LocalTime.of(1, 1));
        provider1.setWebsite("Website");
        provider1.setWorkingDays(new ArrayList<>());

        Employee employee = new Employee();
        employee.setAccount(account1);
        employee.setActivities(new ArrayList<>());
        employee.setAppointmentList(new ArrayList<>());
        employee.setDate(LocalDate.ofEpochDay(1L));
        employee.setId(123L);
        employee.setPhone("4105551212");
        employee.setProvider(provider1);
        employee.setRate(10.0d);
        employee.setTitle("Dr");

        Appointment appointment = new Appointment();
        appointment.setActivity(activity);
        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        appointment.setId(123L);
        appointment.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        Optional<Appointment> ofResult = Optional.of(appointment);
        when(appointmentRepository.findById((Long) any())).thenReturn(ofResult);
        LocalDateTime startDate = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDateTime endDate = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDate date = LocalDate.ofEpochDay(1L);
        EmployeeResponseDTO employee1 = new EmployeeResponseDTO(123L, "Dr", "4105551212", 10.0d, date, 123L,
                new AccountResponseDTO(123L, "david@hotmail.com", "David", new RoleDTO(123L, "Name")));

        ClientResponseDTO client1 = new ClientResponseDTO(123L, "4105551212", "42 Main St",
                new ClientAccountDTO(123L, "David@hotmail.com", "David", "Password", 123L));

        AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO(123L, startDate, endDate, employee1,
                client1, new ActivityResponseDTO(123L, "Name", 10.0d, null, 123L, new ArrayList<>()));

        when(appointmentMapper.toAppointmentResponseDTO((Appointment) any())).thenReturn(appointmentResponseDTO);
        assertSame(appointmentResponseDTO, appointmentServiceImpl.getAppointmentById(123L));
        verify(appointmentRepository).findById((Long) any());
        verify(appointmentMapper).toAppointmentResponseDTO((Appointment) any());
    }

    @Test
    void testGetAll() {
        when(appointmentRepository.findAllByClientId((Long) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Role role = new Role();
        role.setId(123L);
        role.setName("Name");

        Account account = new Account();
        account.setEmail("david@hotmail.com");
        account.setFullName("David");
        account.setId(123L);
        account.setPassword("Password");
        account.setRole(role);

        Client client = new Client();
        client.setAccount(account);
        client.setAddress("42 Main St");
        client.setAppointmentList(new ArrayList<>());
        client.setId(123L);
        client.setPhone("4105551212");
        Optional<Client> ofResult = Optional.of(client);
        when(clientServiceImpl.findByAccountId((Long) any())).thenReturn(ofResult);
        Role role1 = mock(Role.class);
        when(role1.getName()).thenReturn("Name");
        doNothing().when(role1).setId((Long) any());
        doNothing().when(role1).setName((String) any());
        role1.setId(123L);
        role1.setName("ADMINISTRATOR");

        Account account1 = new Account();
        account1.setRole(role1);
        assertTrue(appointmentServiceImpl.getAll(null, new AccountUserDetails(account1)).toList().isEmpty());
        verify(appointmentRepository).findAllByClientId((Long) any(), (Pageable) any());
        verify(clientServiceImpl).findByAccountId((Long) any());
        verify(role1, atLeast(1)).getName();
        verify(role1).setId((Long) any());
        verify(role1).setName((String) any());
    }

    @Test
    void testGetAll2() {
        when(appointmentRepository.findAllByClientId((Long) any(), (Pageable) any()))
                .thenThrow(new NotFoundException("An error occurred"));

        Role role = new Role();
        role.setId(123L);
        role.setName("Name");

        Account account = new Account();
        account.setEmail("david@hotmail.com");
        account.setFullName("David");
        account.setId(123L);
        account.setPassword("Password");
        account.setRole(role);

        Client client = new Client();
        client.setAccount(account);
        client.setAddress("42 Main St");
        client.setAppointmentList(new ArrayList<>());
        client.setId(123L);
        client.setPhone("4105551212");
        Optional<Client> ofResult = Optional.of(client);
        when(clientServiceImpl.findByAccountId((Long) any())).thenReturn(ofResult);
        Role role1 = mock(Role.class);
        when(role1.getName()).thenReturn("Name");
        doNothing().when(role1).setId((Long) any());
        doNothing().when(role1).setName((String) any());
        role1.setId(123L);
        role1.setName("ADMINISTRATOR");

        Account account1 = new Account();
        account1.setRole(role1);
        assertThrows(NotFoundException.class,
                () -> appointmentServiceImpl.getAll(null, new AccountUserDetails(account1)));
        verify(appointmentRepository).findAllByClientId((Long) any(), (Pageable) any());
        verify(clientServiceImpl).findByAccountId((Long) any());
        verify(role1, atLeast(1)).getName();
        verify(role1).setId((Long) any());
        verify(role1).setName((String) any());
    }

    @Test
    void testDeleteAppointment() {
        doNothing().when(appointmentRepository).deleteById((Long) any());
        when(appointmentRepository.existsById((Long) any())).thenReturn(true);
        appointmentServiceImpl.deleteAppointment(123L);
        verify(appointmentRepository).existsById((Long) any());
        verify(appointmentRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteAppointment2() {
        doThrow(new NotFoundException("An error occurred")).when(appointmentRepository).deleteById((Long) any());
        when(appointmentRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(NotFoundException.class, () -> appointmentServiceImpl.deleteAppointment(123L));
        verify(appointmentRepository).existsById((Long) any());
        verify(appointmentRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteAppointment3() {
        doNothing().when(appointmentRepository).deleteById((Long) any());
        when(appointmentRepository.existsById((Long) any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> appointmentServiceImpl.deleteAppointment(123L));
        verify(appointmentRepository).existsById((Long) any());
    }

    @Test
    void testUpdateAppointment() {
        when(appointmentRepository.findById((Long) any())).thenReturn(Optional.empty());

        Provider provider = new Provider();
        provider.setActivities(new ArrayList<>());
        provider.setDomain("Domain");
        provider.setEndTime(LocalTime.of(1, 1));
        provider.setId(123L);
        provider.setName("Name");
        provider.setPhone("4105551212");
        provider.setStartTime(LocalTime.of(1, 1));
        provider.setWebsite("Website");
        provider.setWorkingDays(new ArrayList<>());

        Activity activity = new Activity();
        activity.setAppointmentList(new ArrayList<>());
        activity.setDuration(null);
        activity.setEmployees(new ArrayList<>());
        activity.setId(123L);
        activity.setName("Name");
        activity.setPrice(10.0d);
        activity.setProvider(provider);

        Role role = new Role();
        role.setId(123L);
        role.setName("Name");

        Account account = new Account();
        account.setEmail("david@hotmail.com");
        account.setFullName("David");
        account.setId(123L);
        account.setPassword("Password");
        account.setRole(role);

        Client client = new Client();
        client.setAccount(account);
        client.setAddress("42 Main St");
        client.setAppointmentList(new ArrayList<>());
        client.setId(123L);
        client.setPhone("4105551212");

        Role role1 = new Role();
        role1.setId(123L);
        role1.setName("Name");

        Account account1 = new Account();
        account1.setEmail("david@hotmail.com");
        account1.setFullName("David");
        account1.setId(123L);
        account1.setPassword("password123");
        account1.setRole(role1);

        Provider provider1 = new Provider();
        provider1.setActivities(new ArrayList<>());
        provider1.setDomain("Domain");
        provider1.setEndTime(LocalTime.of(1, 1));
        provider1.setId(123L);
        provider1.setName("Name");
        provider1.setPhone("4105551212");
        provider1.setStartTime(LocalTime.of(1, 1));
        provider1.setWebsite("Website");
        provider1.setWorkingDays(new ArrayList<>());

        Employee employee = new Employee();
        employee.setAccount(account1);
        employee.setActivities(new ArrayList<>());
        employee.setAppointmentList(new ArrayList<>());
        employee.setDate(LocalDate.ofEpochDay(1L));
        employee.setId(123L);
        employee.setPhone("4105551212");
        employee.setProvider(provider1);
        employee.setRate(10.0d);
        employee.setTitle("Dr");

        Provider provider2 = new Provider();
        provider2.setActivities(new ArrayList<>());
        provider2.setDomain("Domain");
        provider2.setEndTime(LocalTime.of(1, 1));
        provider2.setId(123L);
        provider2.setName("Name");
        provider2.setPhone("4105551212");
        provider2.setStartTime(LocalTime.of(1, 1));
        provider2.setWebsite("Website");
        provider2.setWorkingDays(new ArrayList<>());

        Activity activity1 = new Activity();
        activity1.setAppointmentList(new ArrayList<>());
        activity1.setDuration(null);
        activity1.setEmployees(new ArrayList<>());
        activity1.setId(123L);
        activity1.setName("Name");
        activity1.setPrice(10.0d);
        activity1.setProvider(provider2);
        Appointment appointment = mock(Appointment.class);
        when(appointment.getActivity()).thenReturn(activity1);
        doNothing().when(appointment).setActivity((Activity) any());
        doNothing().when(appointment).setClient((Client) any());
        doNothing().when(appointment).setEmployee((Employee) any());
        doNothing().when(appointment).setEndDate((LocalDateTime) any());
        doNothing().when(appointment).setId((Long) any());
        doNothing().when(appointment).setStartDate((LocalDateTime) any());
        appointment.setActivity(activity);
        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        appointment.setId(123L);
        appointment.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(appointmentMapper).updateAppointmentFromDto((AppointmentUpdateDTO) any(), (Appointment) any());

        Provider provider3 = new Provider();
        provider3.setActivities(new ArrayList<>());
        provider3.setDomain("Domain");
        provider3.setEndTime(LocalTime.of(1, 1));
        provider3.setId(123L);
        provider3.setName("Name");
        provider3.setPhone("4105551212");
        provider3.setStartTime(LocalTime.of(1, 1));
        provider3.setWebsite("Website");
        provider3.setWorkingDays(new ArrayList<>());

        Activity activity2 = new Activity();
        activity2.setAppointmentList(new ArrayList<>());
        activity2.setDuration(null);
        activity2.setEmployees(new ArrayList<>());
        activity2.setId(123L);
        activity2.setName("Name");
        activity2.setPrice(10.0d);
        activity2.setProvider(provider3);
        when(activityService.getById((Long) any())).thenReturn(activity2);
        assertThrows(NotFoundException.class, () -> appointmentServiceImpl
                .updateAppointment(new AppointmentUpdateDTO(LocalDateTime.of(1, 1, 1, 1, 1)), 123L));
        verify(appointmentRepository).findById((Long) any());
        verify(appointment).setActivity((Activity) any());
        verify(appointment).setClient((Client) any());
        verify(appointment).setEmployee((Employee) any());
        verify(appointment).setEndDate((LocalDateTime) any());
        verify(appointment).setId((Long) any());
        verify(appointment).setStartDate((LocalDateTime) any());
    }

    @Test
    void testCreateForClient() {
        Role role = new Role();
        role.setId(123L);
        role.setName("Name");

        Account account = new Account();
        account.setEmail("david@hotmail.com");
        account.setFullName("David");
        account.setId(123L);
        account.setPassword("password123");
        account.setRole(role);

        Client client = new Client();
        client.setAccount(account);
        client.setAddress("42 Main St");
        client.setAppointmentList(new ArrayList<>());
        client.setId(123L);
        client.setPhone("4105551212");
        Optional<Client> ofResult = Optional.of(client);
        when(clientServiceImpl.findByAccountId((Long) any())).thenReturn(ofResult);
        when(activityService.findAllById((List<Long>) any())).thenReturn(new ArrayList<>());
        LocalDateTime startTime = LocalDateTime.of(1, 1, 1, 1, 1);
        assertEquals(0.0d,
                appointmentServiceImpl
                        .createForClient(new AppointmentClientCreateDTO(startTime, 123L, 123L, new ArrayList<>()), 123L)
                        .totalPrice()
                        .doubleValue());
        verify(clientServiceImpl).findByAccountId((Long) any());
        verify(activityService).findAllById((List<Long>) any());
    }

    @Test
    void testCreateForClient2() {
        when(clientServiceImpl.findByAccountId((Long) any())).thenReturn(Optional.empty());
        when(activityService.findAllById((List<Long>) any())).thenReturn(new ArrayList<>());
        LocalDateTime startTime = LocalDateTime.of(1, 1, 1, 1, 1);
        assertThrows(NotFoundException.class, () -> appointmentServiceImpl
                .createForClient(new AppointmentClientCreateDTO(startTime, 123L, 123L, new ArrayList<>()), 123L));
        verify(clientServiceImpl).findByAccountId((Long) any());
        verify(activityService).findAllById((List<Long>) any());
    }
}

