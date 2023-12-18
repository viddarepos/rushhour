package com.rushhour_app.infrastructure.mappers;

import com.rushhour_app.domain.activity.entity.Activity;
import com.rushhour_app.domain.activity.models.ActivityDTO;
import com.rushhour_app.domain.activity.models.ActivityResponseDTO;
import com.rushhour_app.domain.activity.models.ActivityUpdateDTO;
import com.rushhour_app.domain.employee.entity.Employee;
import com.rushhour_app.domain.employee.service.EmployeeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = { EmployeeService.class})
public interface ActivityMapper {

    Activity toActivity(ActivityDTO activityDTO);

    ActivityDTO toActivityDTO(Activity activity);

    @Mapping(source = "activity.provider.id", target = "providerId")
    @Mapping(source = "activity.employees", target = "employeesIds", qualifiedByName = "employeesToEmployeeIds")
    ActivityResponseDTO toActivityResponseDTO(Activity activity);

    void updateActivityFromDto(ActivityUpdateDTO activityUpdateDTO, @MappingTarget Activity currentActivity);

    @Named("employeesToEmployeeIds")
    default List<Long> employeesToEmployeeIds(List<Employee> employees){
        return employees.stream().map((Employee::getId)).toList();
    }
}

