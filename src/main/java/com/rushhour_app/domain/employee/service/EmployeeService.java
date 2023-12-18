package com.rushhour_app.domain.employee.service;

import com.rushhour_app.domain.employee.entity.Employee;
import com.rushhour_app.domain.employee.model.EmployeeDTO;
import com.rushhour_app.domain.employee.model.EmployeeResponseDTO;
import com.rushhour_app.domain.employee.model.EmployeeUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee getEmployeeById(Long id);

    EmployeeResponseDTO create(EmployeeDTO employeeDTO);

    EmployeeResponseDTO getById(Long id);

    Page<EmployeeResponseDTO> getAll(Pageable pageable, Long providerId);

    void deleteEmployee(Long id);

    EmployeeResponseDTO update(EmployeeUpdateDTO request, Long id);

    Optional<Employee> getByAccountId(Long id);

    Optional<Employee> findById(Long employeeId);

    Page<EmployeeResponseDTO> getAllForProviderByAccount(Pageable pageable, Long id);

    List<Employee> getByIdsAndProviderId(List<Long> employeesId, Long providerId);
}
