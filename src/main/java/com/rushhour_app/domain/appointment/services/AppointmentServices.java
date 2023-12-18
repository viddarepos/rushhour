package com.rushhour_app.domain.appointment.services;

import com.rushhour_app.domain.appointment.entity.Appointment;
import com.rushhour_app.domain.appointment.models.*;
import com.rushhour_app.infrastructure.security.model.AccountUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppointmentServices {

    AppointmentResponseDTO create(AppointmentDTO appointmentDTO);

    AppointmentResponseDTO getAppointmentById(Long id);

    Page<AppointmentResponseDTO> getAll(Pageable pageable, AccountUserDetails accountUserDetails);

    void deleteAppointment(Long id);

    AppointmentResponseDTO updateAppointment(AppointmentUpdateDTO appointmentUpdateDTO, Long id);

    AppointmentClientCreateResponseDTO createForClient(AppointmentClientCreateDTO dto, Long authUserId);

    List<Appointment> findAllByActivityId(Long id);

}
