package com.rushhour_app.infrastructure.mappers;

import com.rushhour_app.domain.employee.entity.Employee;
import com.rushhour_app.domain.employee.model.EmployeeDTO;
import com.rushhour_app.domain.employee.model.EmployeeResponseDTO;
import com.rushhour_app.domain.employee.model.EmployeeUpdateDTO;
import com.rushhour_app.domain.role.service.RoleService;
import com.rushhour_app.domain.role.service.RoleServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", uses = {RoleService.class})
public abstract class EmployeeMapper {

    private PasswordEncoder passwordEncoder;

    @Mapping(source = "account", target = "account")
    @Mapping(source = "provider.id", target = "providerId")
    public abstract EmployeeDTO toEmployeeDto(Employee employee);

    @Mapping(source = "account.password", target = "account.password", qualifiedByName = "encodePassword")
    @Mapping(target = "provider", ignore = true)
    @Mapping(source = "account.roleId", target = "account.role")
    public abstract Employee toEmployee(EmployeeDTO employeeDTO);

    @Mapping(source = "account.password", target = "account.password", qualifiedByName = "encodePassword")
    public abstract void updateEmployeeFromDto(EmployeeUpdateDTO employeeDTO, @MappingTarget Employee entity);

    @Mapping(source = "account", target = "account")
    @Mapping(source = "provider.id", target = "providerId")
    public abstract EmployeeResponseDTO toEmployeeResponseDTO(Employee employee);

    @Named("encodePassword")
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.passwordEncoder = encoder;
    }

}


