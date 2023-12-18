package com.rushhour_app.web;

import com.rushhour_app.domain.appointment.models.*;
import com.rushhour_app.domain.appointment.services.AppointmentServices;
import com.rushhour_app.infrastructure.security.model.AccountUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/appointments")
public class AppointmentController {

    private final AppointmentServices appointmentServices;

    public AppointmentController(AppointmentServices appointmentServices) {
        this.appointmentServices = appointmentServices;
    }

    @PostMapping
    @PreAuthorize("""
                hasRole('ADMINISTRATOR') ||
                (hasRole('PROVIDER_ADMINISTRATOR') &&  @permissionService.canAppointmentBeMadeByEmployee(#appointmentDTO.employeeId ,#appointmentDTO.activityId))||
                (hasRole('CLIENT') && @permissionService.canAppointmentBeMadeByClient(#appointmentDTO.clientId,#appointmentDTO.employeeId,#appointmentDTO.activityId))  ||
                (hasRole('EMPLOYEE') && @permissionService.canAppointmentBeMadeByEmployee(#appointmentDTO.employeeId ,#appointmentDTO.activityId))
            """)
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentServices.create(appointmentDTO));
    }

    @PostMapping("/multiple")
    @PreAuthorize("""
            (hasRole('CLIENT') && @permissionService.canClientAccessActivities(#dto.providerId,#dto.employeeId, #dto.activities))
            """)
    public ResponseEntity<AppointmentClientCreateResponseDTO> createAppointmentForClient(@RequestBody AppointmentClientCreateDTO dto,
                                                                                         @AuthenticationPrincipal AccountUserDetails loggedUser) {
        return ResponseEntity.ok(appointmentServices.createForClient(dto, loggedUser.getAccount().getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') || 
            hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccessEmployeeByAppointment(#id) ||
            (hasRole('CLIENT') && @permissionService.canClientAccessAppointment(#id)) ||
            (hasRole('EMPLOYEE') && @permissionService.canEmployeeAccessAppointment(#id))
            """)
    public ResponseEntity<AppointmentResponseDTO> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentServices.getAppointmentById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentResponseDTO>> getAll(Pageable pageable, @AuthenticationPrincipal AccountUserDetails authUser) {
        return ResponseEntity.ok(appointmentServices.getAll(pageable, authUser));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') || 
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canEmployeeAccessAppointment(#id)) ||
            (hasRole('CLIENT') && @permissionService.canClientAccessAppointment(#id)) ||
             (hasRole('EMPLOYEE') && @permissionService.canEmployeeAccessAppointment(#id))
            """)
    public void deleteAppointment(@PathVariable Long id) {
        appointmentServices.deleteAppointment(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') || 
            hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canAppointmentBeUpdated(#id) ||
            (hasRole('CLIENT') && @permissionService.canClientAccessAppointment(#id)) 
                   
            """)
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@Valid @RequestBody AppointmentUpdateDTO appointmentUpdateDTO, @PathVariable
    Long id) {
        return ResponseEntity.ok().body(appointmentServices.updateAppointment(appointmentUpdateDTO, id));

    }

}