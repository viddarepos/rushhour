package com.rushhour_app.web;

import com.rushhour_app.domain.employee.model.EmployeeDTO;
import com.rushhour_app.domain.employee.model.EmployeeResponseDTO;
import com.rushhour_app.domain.employee.model.EmployeeUpdateDTO;
import com.rushhour_app.domain.employee.service.EmployeeService;
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
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') || 
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminCreateEmployee(#employeeDTO.providerId, #employeeDTO.account.roleId))
            """)
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.create(employeeDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') || 
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccessEmployee(#id)) ||
            (hasRole('EMPLOYEE') && @permissionService.canEmployeeAccess(#id))
            """)
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @GetMapping
    @PreAuthorize("""
            hasRole('ADMINISTRATOR')
            """)
    public ResponseEntity<Page<EmployeeResponseDTO>> getAll(Pageable pageable, Long providerId) {
        return ResponseEntity.ok(employeeService.getAll(pageable, providerId));
    }
    @GetMapping("/provider")
    @PreAuthorize("""
    hasRole('PROVIDER_ADMINISTRATOR')
    """)
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllForProvider(@AuthenticationPrincipal AccountUserDetails user, Pageable pageable) {
        return ResponseEntity.ok(employeeService.getAllForProviderByAccount(pageable, user.getAccount().getId()));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("""
             hasRole('ADMINISTRATOR') || 
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccessEmployee(#id))
            """)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') || 
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccessEmployee(#id)) ||
            (hasRole('EMPLOYEE') && @permissionService.canEmployeeAccess(#id))
            """)
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@Valid @RequestBody EmployeeUpdateDTO request,@PathVariable Long id) {
        return ResponseEntity.ok().body(employeeService.update(request, id));
    }
}


