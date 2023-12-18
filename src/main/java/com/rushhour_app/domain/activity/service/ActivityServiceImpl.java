package com.rushhour_app.domain.activity.service;

import com.rushhour_app.domain.activity.entity.Activity;
import com.rushhour_app.domain.activity.models.ActivityDTO;
import com.rushhour_app.domain.activity.models.ActivityResponseDTO;
import com.rushhour_app.domain.activity.models.ActivityUpdateDTO;
import com.rushhour_app.domain.activity.repository.ActivityRepository;
import com.rushhour_app.domain.appointment.entity.Appointment;
import com.rushhour_app.domain.appointment.services.AppointmentServices;
import com.rushhour_app.domain.client.service.ClientService;
import com.rushhour_app.domain.employee.entity.Employee;
import com.rushhour_app.domain.employee.service.EmployeeService;
import com.rushhour_app.domain.provider.service.ProviderService;
import com.rushhour_app.domain.provider.service.ProviderServiceImpl;
import com.rushhour_app.infrastructure.exception.customException.ConflictException;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.mappers.ActivityMapper;
import com.rushhour_app.infrastructure.smtp.MyMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final ProviderService providerService;
    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final MyMailSender mailSender;
    private final AppointmentServices appointmentServices;

    @Value("${app.email.default-sender}")
    private String defaultSender;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository, ActivityMapper activityMapper,
                               ProviderServiceImpl providerService,
                               EmployeeService employeeService,
                               ClientService clientService,
                               MyMailSender mailSender,
                               @Lazy AppointmentServices appointmentServices) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
        this.providerService = providerService;
        this.employeeService = employeeService;
        this.clientService = clientService;
        this.mailSender = mailSender;
        this.appointmentServices = appointmentServices;
    }

    public Activity getById(Long id) {
        return activityRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Activity with id " + id + " does not exists")
        );
    }

    @Override
    public Optional<Activity> findById(Long id) {
        return activityRepository.findById(id);
    }

    @Override
    public List<Activity> findAllById(List<Long> ids) {
        return activityRepository.findAllById(ids);
    }

    @Override
    public ActivityResponseDTO createActivity(ActivityDTO activityDTO) {
        var activity = activityMapper.toActivity(activityDTO);
        var provider = providerService.getProviderById(activityDTO.providerId());
        var employees = employeeService.getByIdsAndProviderId(activityDTO.employeesId(), activityDTO.providerId());
        var providerId = activityDTO.providerId();
        var activityName = activityRepository.existsByNameAndProviderId(activityDTO.name(), providerId);

        if (employees.size() < activityDTO.employeesId().size()) {
            throw new ConflictException("Some of the listed employees are not from this provider!");
        }
        if (activityName) {
            throw new ConflictException("Activity with the name %s already exists".formatted(activityDTO.name()));
        }

        activity.setProvider(provider);
        activity.setEmployees(employees);

        employeeSetActivities(employees, activity);
        activityRepository.save(activity);

        return activityMapper.toActivityResponseDTO(activity);
    }

    @Override
    public ActivityResponseDTO getActivityById(Long id) {
        Activity activity = getById(id);

        return activityMapper.toActivityResponseDTO(activity);
    }

    @Override
    public Page<ActivityResponseDTO> getAllActivities(Long providerId, Pageable pageable) {
        var activities = activityRepository.findAllByProviderId(providerId, pageable);
        return activities.map(activityMapper::toActivityResponseDTO);
    }

    @Override
    public void deleteActivity(Long id) {
        var appointments = appointmentServices.findAllByActivityId(id);

        for (Appointment appointment : appointments) {
            var client = clientService.getClientById(appointment.getClient().getId());
            mailSender.sendEmail(appointment.getEmployee().getAccount().getEmail(), client.getAccount().getEmail(),
                    "Appointment scheduled for " + appointment.getEmployee().getProvider().getDomain()+ " is deleted because "+ appointment.getActivity().getName() + " activity is unavailable. "
                            .formatted(appointment.getStartDate()), "Appointment removed");
        }
        if (!activityRepository.existsById(id)) {
            throw new NotFoundException("Activity with id " + id + " doest not exists!");
        }
        activityRepository.deleteById(id);
    }


    @Override
    public ActivityResponseDTO updateActivity(ActivityUpdateDTO activityUpdateDTO, Long id) {
        var currentActivity = getById(id);
        var employees = employeeService.getByIdsAndProviderId(activityUpdateDTO.employeesId(), currentActivity.getProvider().getId());

        if (employees.size() < activityUpdateDTO.employeesId().size()) {
            throw new ConflictException("Some of the listed employees are not from this provider!");
        }

        currentActivity.setEmployees(employees);
        activityMapper.updateActivityFromDto(activityUpdateDTO, currentActivity);
        employeeSetActivities(employees, currentActivity);
        activityRepository.save(currentActivity);

        return activityMapper.toActivityResponseDTO(currentActivity);
    }

    public void employeeSetActivities(List<Employee> employees, Activity currentActivity) {
        var currentEmployees = Optional.ofNullable(currentActivity.getEmployees())
                .orElseGet(() -> {
                    currentActivity.setEmployees(new ArrayList<>());
                    return currentActivity.getEmployees();
                });
        employees = employees.stream()
                .filter(employee -> !currentEmployees.contains(employee))
                .collect(Collectors.toList());
        currentEmployees.addAll(employees);
    }
}
