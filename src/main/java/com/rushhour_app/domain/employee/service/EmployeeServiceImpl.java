package com.rushhour_app.domain.employee.service;

import com.rushhour_app.domain.account.service.AccountService;
import com.rushhour_app.domain.appointment.models.AppointmentDTO;
import com.rushhour_app.domain.employee.entity.Employee;
import com.rushhour_app.domain.employee.model.EmployeeDTO;
import com.rushhour_app.domain.employee.model.EmployeeResponseDTO;
import com.rushhour_app.domain.employee.model.EmployeeUpdateDTO;
import com.rushhour_app.domain.employee.repository.EmployeeRepository;
import com.rushhour_app.domain.provider.entity.Provider;
import com.rushhour_app.domain.provider.service.ProviderService;
import com.rushhour_app.domain.provider.service.ProviderServiceImpl;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.mappers.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final ProviderService providerService;
    private final AccountService accountService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper , ProviderServiceImpl providerService, AccountService accountService) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.providerService = providerService;
        this.accountService = accountService;
    }

    @Override
    public Optional<Employee> getByAccountId(Long id) {
        return employeeRepository.findByAccountId(id);
    }

    @Override
    public Optional<Employee> findById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @Override
    public Page<EmployeeResponseDTO> getAllForProviderByAccount(Pageable pageable, Long id) {
        Optional<Employee> employeeOptional = getByAccountId(id);

        if(employeeOptional.isEmpty()){
            throw new NotFoundException("Employee with given account id is not exists");
        }

        Employee employee = employeeOptional.get();

        return employeeRepository.findAllByProviderId(pageable,employee.getProvider().getId()).map(employeeMapper::toEmployeeResponseDTO);
    }

    @Override
    public List<Employee> getByIdsAndProviderId(List<Long> employeesId, Long providerId) {
       return employeeRepository.findByIdInAndProviderId(employeesId,providerId);
    }

    @Override
    public EmployeeResponseDTO create(EmployeeDTO employeeDTO) {
        Optional<Provider> optionalProvider = providerService.findProviderById(employeeDTO);
        providerService.existsByProviderId(employeeDTO);
        accountService.assertEmployeeAccountDoesNotExists(employeeDTO.account());

        Employee employee = employeeMapper.toEmployee(employeeDTO);
        employee.setProvider(optionalProvider.get());
        Employee createdEmployee = employeeRepository.save(employee);

        return employeeMapper.toEmployeeResponseDTO(createdEmployee);
    }

    @Override
    public EmployeeResponseDTO getById(Long id) {
        Employee employee = getEmployeeById(id);

        return employeeMapper.toEmployeeResponseDTO(employee);
    }

    @Override
    public Page<EmployeeResponseDTO> getAll(Pageable pageable, Long providerId) {
        return employeeRepository.getAllByProviderId(providerId, pageable).map(employeeMapper::toEmployeeResponseDTO);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new NotFoundException("Employee with id " + id + " does not exist in database!");
        }
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO update(EmployeeUpdateDTO request, Long id) {
        var currentEmployee = getEmployeeById(id);
        employeeMapper.updateEmployeeFromDto(request, currentEmployee);
        employeeRepository.save(currentEmployee);

        return employeeMapper.toEmployeeResponseDTO(currentEmployee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Employee with id " + id + " does not exist in database!"));
    }


}
