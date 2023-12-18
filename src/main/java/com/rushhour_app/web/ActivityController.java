package com.rushhour_app.web;

import com.rushhour_app.domain.activity.models.ActivityDTO;
import com.rushhour_app.domain.activity.models.ActivityResponseDTO;
import com.rushhour_app.domain.activity.models.ActivityUpdateDTO;
import com.rushhour_app.domain.activity.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;

    }

    @PostMapping
    @PreAuthorize("""
             hasRole('ADMINISTRATOR') ||
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccess(#activityDTO.providerId))
            """)
    public ResponseEntity<ActivityResponseDTO> createActivity(@Valid @RequestBody ActivityDTO activityDTO) {
        return ResponseEntity.ok(activityService.createActivity(activityDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') ||
            hasRole('CLIENT') ||
            (hasRole('EMPLOYEE') && @permissionService.canEmployeeAccessActivity(#id)) ||
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccessActivity(#id))
            """)
    public ResponseEntity<ActivityResponseDTO> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    @GetMapping
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') ||
            hasRole('CLIENT') ||
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccess(#providerId))
            """)
    public ResponseEntity<Page<ActivityResponseDTO>> getAll(@RequestParam Long providerId, Pageable pageable) {
        return ResponseEntity.ok(activityService.getAllActivities(providerId, pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') ||
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccessActivity(#id))
            """)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') ||
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccessActivity(#id))
            """)
    public ResponseEntity<ActivityResponseDTO> updateActivity(@Valid @RequestBody ActivityUpdateDTO activityUpdateDTO
            , @PathVariable Long id) {
        return ResponseEntity.ok(activityService.updateActivity(activityUpdateDTO, id));
    }

}
