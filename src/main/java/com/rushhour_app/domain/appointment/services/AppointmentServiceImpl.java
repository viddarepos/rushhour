package com.rushhour_app.domain.appointment.services;

import com.rushhour_app.domain.activity.service.ActivityService;
import com.rushhour_app.domain.appointment.entity.Appointment;
import com.rushhour_app.domain.appointment.models.*;
import com.rushhour_app.domain.appointment.repository.AppointmentRepository;
import com.rushhour_app.domain.client.entity.Client;
import com.rushhour_app.domain.client.service.ClientService;
import com.rushhour_app.domain.client.service.ClientServiceImpl;
import com.rushhour_app.domain.employee.service.EmployeeService;
import com.rushhour_app.domain.employee.service.EmployeeServiceImpl;
import com.rushhour_app.domain.provider.service.ProviderService;
import com.rushhour_app.infrastructure.security.model.AccountUserDetails;
import com.rushhour_app.infrastructure.smtp.MyMailSender;
import com.rushhour_app.infrastructure.exception.customException.ConflictException;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.mappers.AppointmentMapper;
import com.rushhour_app.infrastructure.utils.DurationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentServices {

    private final AppointmentRepository appointmentRepository;
    private final MyMailSender sender;
    private final AppointmentMapper appointmentMapper;
    private final EmployeeService employeeService;
    private final ActivityService activityService;
    private final ProviderService providerService;
    private final ClientService clientService;

    @Value("${app.email.default-sender}")
    private String defaultSender;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  MyMailSender sender,
                                  AppointmentMapper appointmentMapper,
                                  ClientServiceImpl clientService,
                                  EmployeeServiceImpl employeeService,
                                  ProviderService providerService, @Lazy ActivityService activityService) {
        this.appointmentRepository = appointmentRepository;
        this.sender = sender;
        this.appointmentMapper = appointmentMapper;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.providerService = providerService;
        this.activityService = activityService;
    }

    private Appointment getById(Long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment not found"));
    }

    private boolean checkForSameProviderId(Appointment appointment, AppointmentDTO appointmentDTO) {
        if (appointment.getEmployee().getProvider().getId().equals(appointment.getEmployee().getProvider().getId())) {
            if (appointmentRepository.existsByStartDate(appointmentDTO.startDate())) {
                throw new ConflictException("You can't create this appointment because the activity is scheduled for that period!");
            }
        }
        return false;
    }

    @Override
    public List<Appointment> findAllByActivityId(Long id) {
        return appointmentRepository.findAllByActivityId(id);
    }

    @Override
    public AppointmentResponseDTO create(AppointmentDTO appointmentDTO) {
        var employee = employeeService.getEmployeeById(appointmentDTO.employeeId());
        var client = clientService.getClientById(appointmentDTO.clientId());
        var activity = activityService.findById(appointmentDTO.activityId());
        var appointment = appointmentMapper.toAppointment(appointmentDTO);


        if (activity.isEmpty()) {
            throw new NotFoundException("Some of the id's is not valid!");
        }

        appointment.setEndDate(DurationUtility.calculateTime(activity.get().getDuration(), appointmentDTO.startDate()));

        appointment.setEmployee(employee);
        appointment.setClient(client);
        appointment.setActivity(activity.get());

        if (!appointmentRepository.checkIfEmployeeIsBusy(employee.getId(), appointment.getStartDate(),
                appointment.getEndDate()).isEmpty() || checkForSameProviderId(appointment, appointmentDTO)) {
            throw new ConflictException("You can't create this appointment because the activity is scheduled for that period!");
        }

        var workingEndHours =
                (appointment.getEndDate().getHour() > activityService.getById(appointmentDTO.activityId()).getProvider().getEndTime().getHour()) ||
                        (appointment.getEndDate().getHour() == activityService.getById(appointmentDTO.activityId()).getProvider().getEndTime().getHour() &&
                                appointment.getEndDate().getMinute() > activityService.getById(appointmentDTO.activityId()).getProvider().getEndTime().getMinute());

        if (workingEndHours) {
            throw new ConflictException("This provider is closed!");
        }

        var workingStartHours =
                (appointment.getStartDate().getHour() < activityService.getById(appointmentDTO.activityId()).getProvider().getStartTime().getHour()) ||

                        (appointment.getStartDate().getHour() == activityService.getById(appointmentDTO.activityId()).getProvider().getStartTime().getHour() &&
                                appointment.getStartDate().getMinute() < activityService.getById(appointmentDTO.activityId()).getProvider().getStartTime().getMinute());

        if (workingStartHours) {
            throw new ConflictException("This provider is closed!");
        }


        Appointment createdAppointment = appointmentRepository.save(appointment);
        String[] mailList = {employee.getAccount().getEmail(), client.getAccount().getEmail()};

        sender.sendEmail(appointment.getEmployee().getAccount().getEmail(), mailList, "Scheduled appointment in %s."
                .formatted(appointment.getStartDate()), "Appointment scheduled");

        return appointmentMapper.toAppointmentResponseDTO(createdAppointment);
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = getById(id);

        return appointmentMapper.toAppointmentResponseDTO(appointment);
    }

    @Override
    public Page<AppointmentResponseDTO> getAll(Pageable pageable, AccountUserDetails accountUserDetails) {
        if (accountUserDetails.hasRole("ADMINISTRATOR")) {
            return appointmentRepository.findAll(pageable).map(appointmentMapper::toAppointmentResponseDTO);
        }

        if (accountUserDetails.hasRole("PROVIDER_ADMINISTRATOR")) {
            var provider = providerService.findProviderByAccountId(accountUserDetails.getAccount().getId());
            return appointmentRepository.findAllByProviderId(provider.getId(), pageable).map(appointmentMapper::toAppointmentResponseDTO);
        }

        if (accountUserDetails.hasRole("EMPLOYEE")) {
            var employeeOptional = employeeService.getByAccountId(accountUserDetails.getAccount().getId());
            if (employeeOptional.isEmpty()) {
                throw new NotFoundException("Employee not found");
            }
            var employee = employeeOptional.get();
            return appointmentRepository.findAllByEmployeeId(employee.getId(), pageable).map(appointmentMapper::toAppointmentResponseDTO);
        }
        var clientOptional = clientService.findByAccountId(accountUserDetails.getAccount().getId());

        if (clientOptional.isEmpty()) {
            throw new NotFoundException("Client not found");
        }
        var client = clientOptional.get();
        return appointmentRepository.findAllByClientId(client.getId(), pageable).map(appointmentMapper::toAppointmentResponseDTO);
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new NotFoundException("Appointment with id " + id + " does not exists!");
        }
        appointmentRepository.deleteById(id);
    }

    @Override
    public AppointmentResponseDTO updateAppointment(AppointmentUpdateDTO appointmentUpdateDTO, Long id) {
        var appointment = getById(id);
        var activity = activityService.getById(appointment.getActivity().getId());

        appointmentMapper.updateAppointmentFromDto(appointmentUpdateDTO, appointment);
        appointment.setEndDate(DurationUtility.calculateTime(activity.getDuration(), appointmentUpdateDTO.startDate()));

        if (!appointmentRepository.checkIfEmployeeIsBusy(activity.getId(), appointment.getStartDate(), appointment.getEndDate()).isEmpty()) {
            throw new ConflictException("You can't create this appointment because the activity is scheduled for that period!");
        }

        appointment.setActivity(activity);
        appointmentRepository.save(appointment);

        return appointmentMapper.toAppointmentResponseDTO(appointment);
    }

    @Override
    public AppointmentClientCreateResponseDTO createForClient(AppointmentClientCreateDTO dto, Long authUserId) {
        var activities = activityService.findAllById(dto.activities());
        var time = dto.startTime();
        var totalPrice = 0.0;
        var client = clientService.findByAccountId(authUserId);

        if (client.isEmpty()) {
            throw new NotFoundException("Client is not present");
        }
        Client createdClient = client.get();

        for (var activity : activities) {
            var appointment = new Appointment();
            var employee = employeeService.getEmployeeById(dto.employeeId());
            var startDate = time;

            appointment.setStartDate(startDate);
            var endTime = DurationUtility.calculateTime(activity.getDuration(), time);

            appointment.setEndDate(endTime);

            var conflictingActivity = appointmentRepository.checkIfEmployeeIsBusy(employee.getId(), startDate, endTime);

            if (conflictingActivity.size() == 0) {

                appointment.setActivity(activity);
                appointment.setEmployee(employee);
                appointment.setClient(createdClient);
                appointmentRepository.save(appointment);
                totalPrice += activity.getPrice();
                time = endTime;

                var outsideOfWorkingEndHours = (appointment.getEndDate().getHour() > activityService.getById(activity.getId()).getProvider().getEndTime().getHour()) ||
                        (appointment.getEndDate().getHour() == activityService.getById(activity.getId()).getProvider().getEndTime().getHour() &&
                                appointment.getEndDate().getMinute() > activityService.getById(activity.getId()).getProvider().getEndTime().getMinute());

                if (outsideOfWorkingEndHours) {
                    throw new ConflictException("This provider is closed!");
                }

                var outsideOfWorkingStartHours =
                        (appointment.getStartDate().getHour() < activityService.getById(activity.getId()).getProvider().getStartTime().getHour()) ||

                                (appointment.getStartDate().getHour() == activityService.getById(activity.getId()).getProvider().getStartTime().getHour() &&
                                        appointment.getStartDate().getMinute() < activityService.getById(activity.getId()).getProvider().getStartTime().getMinute());

                if (outsideOfWorkingStartHours) {
                    throw new ConflictException("This provider is closed!");
                }
            } else {
                throw new ConflictException("You can't create this appointment because the activity is scheduled for that period!");
            }

        }

        return new AppointmentClientCreateResponseDTO(totalPrice);
    }
}
