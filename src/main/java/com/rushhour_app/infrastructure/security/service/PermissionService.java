package com.rushhour_app.infrastructure.security.service;

import com.rushhour_app.domain.appointment.models.AppointmentDTO;

import java.util.List;

public interface PermissionService {

    boolean canClientAccess(Long clientId);

    boolean canProviderAdminAccess(Long providerId);

    boolean canEmployeeAccess(Long employeeId);

    boolean canProviderAdminAccessEmployee(Long employeeId);

    boolean canProviderAdminAccessActivity(Long id);

    boolean canCreateAppointmentWithEmployee(Long employeeId, Long colleagueId);

    boolean canAppointmentBeMadeByClient(Long clientId, Long employeeId, Long activityId);

    boolean canAppointmentBeUpdated( Long appointmentId);

    boolean canAppointmentBeMadeByEmployee(Long employeeId, Long activityId );

    boolean canClientAccessActivities(Long providerId, Long employeeId, List<Long> activities);

    boolean canProviderAdminCreateEmployee(Long providerId, Long employeeRoleId);

    boolean canEmployeeAccessActivity(Long activityId);

    boolean canProviderAdminAccessEmployeeByAppointment(Long appointmentId);

    boolean canClientAccessAppointment(Long id);

    boolean canEmployeeAccessAppointment(Long id);

}


