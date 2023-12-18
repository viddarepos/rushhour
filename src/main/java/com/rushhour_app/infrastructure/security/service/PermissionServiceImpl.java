package com.rushhour_app.infrastructure.security.service;

import com.rushhour_app.domain.activity.entity.Activity;
import com.rushhour_app.domain.activity.service.ActivityService;
import com.rushhour_app.domain.appointment.models.AppointmentDTO;
import com.rushhour_app.domain.appointment.services.AppointmentServices;
import com.rushhour_app.domain.client.model.ClientResponseDTO;
import com.rushhour_app.domain.client.service.ClientService;
import com.rushhour_app.domain.employee.entity.Employee;
import com.rushhour_app.domain.employee.service.EmployeeService;
import com.rushhour_app.domain.provider.service.ProviderService;
import com.rushhour_app.domain.role.enums.RoleNames;
import com.rushhour_app.domain.role.service.RoleService;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.security.model.AccountUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    private final ProviderService providerService;
    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final ActivityService activityService;
    private final RoleService roleService;
    private final AppointmentServices appointmentServices;

    public PermissionServiceImpl(ProviderService providerService,
                                 EmployeeService employeeService,
                                 ClientService clientService,
                                 ActivityService activityService,
                                 RoleService roleService, AppointmentServices appointmentServices) {
        this.providerService = providerService;
        this.employeeService = employeeService;
        this.clientService = clientService;
        this.activityService = activityService;
        this.roleService = roleService;
        this.appointmentServices = appointmentServices;
    }

    @Override
    public boolean canAppointmentBeMadeByEmployee(Long employeeId, Long activityId) {
        var authenticateUser = getPrincipal().getAccount().getId();
        var employee = employeeService.getByAccountId(authenticateUser);

        if (employee.isEmpty()) {
            return false;
        }
        var providerAdmin = employee.get();
        var anotherEmployee = employeeService.getEmployeeById(employeeId);
        var activity = activityService.getActivityById(activityId);

        return Objects.equals(
                providerAdmin.getProvider().getId(),
                anotherEmployee.getProvider().getId()
        ) && Objects.equals(
                anotherEmployee.getProvider().getId(),
                activity.providerId()
        );
    }

    @Override
    public boolean canAppointmentBeMadeByClient(Long clientId, Long employeeId, Long activityId) {
        var authenticateUser = getPrincipal().getAccount().getId();
        var client = clientService.getClientById(clientId);
        var employee = employeeService.getEmployeeById(employeeId);
        var activity = activityService.getActivityById(activityId);

        return client.getAccount().getId().equals(authenticateUser) && employee.getProvider().getId().equals(activity.providerId());
    }

    @Override
    public boolean canAppointmentBeUpdated(Long appointmentId) {
        var auth = getPrincipal().getAccount().getId();
        var optionalEmployee = employeeService.getByAccountId(auth);

        if (optionalEmployee.isEmpty()){
            return false;
        }

        var providerAdmin = optionalEmployee.get();
        var appointment = appointmentServices.getAppointmentById(appointmentId);
        return providerAdmin.getProvider().getId().equals(appointment.employee().providerId());
    }

    @Override
    public boolean canClientAccessActivities(Long providerId, Long employeeId, List<Long> activities) {
        var provider = providerService.getProviderById(providerId);
        var employee = employeeService.getEmployeeById(employeeId);

        List<Long> providerActivities = provider.getActivities().stream()
                .map(Activity::getId)
                .toList();

        boolean result = activities.stream()
                .allMatch(providerActivities::contains);

        return result && employee.getProvider().getId().equals(provider.getId());
    }

    @Override
    public boolean canClientAccess(Long clientId) {
        var authenticatedUser = getPrincipal();
        ClientResponseDTO client = clientService.getById(clientId);
        return client.account().id().equals(authenticatedUser.getAccount().getId());
    }

    @Override
    public boolean canProviderAdminAccess(Long providerId) {
        var authenticatedUser = getPrincipal();

        return providerService.getProviderByEmployeeAccountId(authenticatedUser.getAccount().getId())
                .map(provider -> provider.getId().equals(providerId))
                .orElse(false);
    }

    @Override
    public boolean canProviderAdminCreateEmployee(Long providerId, Long employeeRoleId) {
        var authenticatedUser = getPrincipal();
        var roleOptional = roleService.findById(employeeRoleId);

        if (roleOptional.isEmpty()) {
            return false;
        }

        var role = roleOptional.get();

        return providerService.getProviderByEmployeeAccountId(authenticatedUser.getAccount().getId())
                .map(provider -> provider.getId().equals(providerId)
                        && role.getName().equals(RoleNames.EMPLOYEE.name()))
                .orElse(false);
    }

    @Override
    public boolean canEmployeeAccess(Long employeeId) {
        var authenticatedUser = getPrincipal();
        Optional<Employee> employeeOptional = employeeService.getByAccountId(authenticatedUser.getAccount().getId());

        if (employeeOptional.isEmpty()) {
            return false;
        }

        var employee = employeeOptional.get();
        return Objects.equals(employeeId, employee.getId());
    }

    @Override
    public boolean canEmployeeAccessActivity(Long activityId) {
        var authenticatedUser = getPrincipal();
        Optional<Employee> employeeOptional = employeeService.getByAccountId(authenticatedUser.getAccount().getId());
        Optional<Activity> activityOptional = activityService.findById(activityId);

        if (employeeOptional.isEmpty() || activityOptional.isEmpty()) {
            return false;
        }

        var employee = employeeOptional.get();
        var activity = activityOptional.get();

        return Objects.equals(activity.getProvider().getId(), employee.getProvider().getId());
    }

    @Override
    public boolean canProviderAdminAccessEmployeeByAppointment(Long appointmentId) {
        var authenticatedUser = getPrincipal();
        var appointment = appointmentServices.getAppointmentById(appointmentId);
        Optional<Employee> providerAdminOptional = employeeService.getByAccountId(authenticatedUser.getAccount().getId());
        Optional<Employee> employeeOptional = employeeService.findById(appointment.employee().id());

        if (providerAdminOptional.isEmpty() || employeeOptional.isEmpty()) {
            return false;
        }

        var providerAdmin = providerAdminOptional.get();
        var employee = employeeOptional.get();

        return Objects.equals(
                employee.getProvider().getId(),
                providerAdmin.getProvider().getId());
    }

    @Override
    public boolean canProviderAdminAccessEmployee(Long employeeId) {
        var authenticatedUser = getPrincipal();
        Optional<Employee> providerAdminOptional = employeeService.getByAccountId(authenticatedUser.getAccount().getId());
        Optional<Employee> employeeOptional = employeeService.findById(employeeId);

        if (providerAdminOptional.isEmpty() || employeeOptional.isEmpty()) {
            return false;
        }

        var providerAdmin = providerAdminOptional.get();
        var employee = employeeOptional.get();

        return Objects.equals(
                employee.getProvider().getId(),
                providerAdmin.getProvider().getId());
    }

    @Override
    public boolean canProviderAdminAccessActivity(Long id) {
        var authenticatedUser = getPrincipal();
        Optional<Employee> providerAdminOptional = employeeService.getByAccountId(authenticatedUser.getAccount().getId());

        if (providerAdminOptional.isEmpty()) {
            return false;
        }

        var activity = activityService.getActivityById(id);
        var employee = providerAdminOptional.get();

        return employee.getProvider().getId().equals(activity.providerId());
    }

    @Override
    public boolean canCreateAppointmentWithEmployee(Long employeeId, Long colleagueId) {
        var loggedUserId = getPrincipal().getAccount().getId();
        var employee = employeeService.getEmployeeById(employeeId);
        var colleague = employeeService.getEmployeeById(colleagueId);

        return employee.getAccount().getId().equals(loggedUserId) || colleague.getAccount().getId().equals(loggedUserId);
    }

    @Override
    public boolean canClientAccessAppointment(Long id) {
        var authenticatedUserId = getPrincipal().getAccount().getId();
        var appointment = appointmentServices.getAppointmentById(id);

        return appointment.client().account().id().equals(authenticatedUserId);
    }

    @Override
    public boolean canEmployeeAccessAppointment(Long id) {
        var authenticatedUserId = getPrincipal().getAccount().getId();
        var appointment = appointmentServices.getAppointmentById(id);

        return appointment.employee().account().id().equals(authenticatedUserId);
    }

    private static AccountUserDetails getPrincipal() {
        var securityContext = SecurityContextHolder.getContext();

        return (AccountUserDetails) securityContext.getAuthentication().getPrincipal();
    }


}
