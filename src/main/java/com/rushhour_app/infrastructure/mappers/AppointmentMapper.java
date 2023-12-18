package com.rushhour_app.infrastructure.mappers;

import com.rushhour_app.domain.appointment.entity.Appointment;
import com.rushhour_app.domain.appointment.models.AppointmentDTO;
import com.rushhour_app.domain.appointment.models.AppointmentResponseDTO;
import com.rushhour_app.domain.appointment.models.AppointmentUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    Appointment toAppointment(AppointmentDTO appointmentDTO);

    @Mapping(source = "employee.provider.id", target = "employee.providerId")
    AppointmentResponseDTO toAppointmentResponseDTO(Appointment appointment);

    void updateAppointmentFromDto(AppointmentUpdateDTO appointmentUpdateDTO, @MappingTarget Appointment currentAppointment);

}
