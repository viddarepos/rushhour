package com.rushhour_app.domain.activity.service;

import com.rushhour_app.domain.activity.entity.Activity;
import com.rushhour_app.domain.activity.models.ActivityDTO;
import com.rushhour_app.domain.activity.models.ActivityResponseDTO;
import com.rushhour_app.domain.activity.models.ActivityUpdateDTO;
import com.rushhour_app.domain.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ActivityService {

    ActivityResponseDTO createActivity(ActivityDTO activityDTO);

    ActivityResponseDTO getActivityById(Long id);

    Page<ActivityResponseDTO> getAllActivities(Long providerId, Pageable pageable);

    void deleteActivity(Long id);

    ActivityResponseDTO updateActivity(ActivityUpdateDTO activityUpdateDTO, Long id);

    Activity getById(Long id);

    Optional<Activity> findById(Long id);

    List<Activity> findAllById(List<Long> ids);

}
