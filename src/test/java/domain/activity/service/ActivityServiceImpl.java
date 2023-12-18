package domain.activity.service;

import com.rushhour_app.domain.activity.entity.Activity;
import com.rushhour_app.domain.activity.models.ActivityDTO;
import com.rushhour_app.domain.activity.models.ActivityResponseDTO;
import com.rushhour_app.domain.activity.models.ActivityUpdateDTO;
import com.rushhour_app.domain.activity.repository.ActivityRepository;
import com.rushhour_app.domain.employee.service.EmployeeService;
import com.rushhour_app.domain.provider.entity.Provider;
import com.rushhour_app.domain.provider.service.ProviderServiceImpl;
import com.rushhour_app.infrastructure.exception.customException.ConflictException;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.mappers.ActivityMapper;
import domain.utils.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {com.rushhour_app.domain.activity.service.ActivityServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ActivityServiceImpl {

    @MockBean
    private ActivityMapper activityMapper;

    @MockBean
    private ActivityRepository activityRepository;

    @Autowired
    private com.rushhour_app.domain.activity.service.ActivityServiceImpl activityServiceImpl;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ProviderServiceImpl providerServiceImpl;
    
    @Test
    void testGetById() {
        Provider provider = new Provider();
        Util.setProvider(provider);
        Activity activity = new Activity();
        Util.setActivity(activity, provider);
        Optional<Activity> ofResult = Optional.of(activity);
        when(activityRepository.findById(any())).thenReturn(ofResult);
        assertSame(activity, activityServiceImpl.getById(1L));
        verify(activityRepository).findById(any());
    }

    @Test
    void testGetById2() {
        when(activityRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> activityServiceImpl.getById(1L));
        verify(activityRepository).findById(any());
    }

    @Test
    void testCreateActivity() {
        Provider provider = new Provider();
        Util.setProvider(provider);

        Activity activity = new Activity();
        Util.setActivity(activity, provider);

        when(activityRepository.existsByNameAndProviderId(any(), any())).thenReturn(true);
        when(activityRepository.save(any())).thenReturn(activity);
        when(activityMapper.toActivity(any())).thenReturn(activity);
        when(providerServiceImpl.getProviderById(any())).thenReturn(provider);
        when(employeeService.getByIdsAndProviderId(any(), any())).thenReturn(new ArrayList<>());
        assertThrows(ConflictException.class,
                () -> activityServiceImpl.createActivity(new ActivityDTO("Name", 10.0d, null, 1L, new ArrayList<>())));
        verify(activityRepository).existsByNameAndProviderId(any(), any());
        verify(activityMapper).toActivity(any());
        verify(providerServiceImpl).getProviderById(any());
        verify(employeeService).getByIdsAndProviderId(any(), any());
    }

    @Test
    void testGetActivityById() {
        Provider provider = new Provider();
        Util.setProvider(provider);

        Activity activity = new Activity();
        Util.setActivity(activity, provider);

        Optional<Activity> ofResult = Optional.of(activity);
        when(activityRepository.findById(any())).thenReturn(ofResult);
        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO(1L, "Name", 10.0d, null, 1L,
                new ArrayList<>());

        when(activityMapper.toActivityResponseDTO(any())).thenReturn(activityResponseDTO);
        assertSame(activityResponseDTO, activityServiceImpl.getActivityById(1L));
        verify(activityRepository).findById(any());
        verify(activityMapper).toActivityResponseDTO(any());
    }

    @Test
    void testGetAllActivities() {
        when(activityRepository.findAllByProviderId(any(), any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(activityServiceImpl.getAllActivities(1L, null).toList().isEmpty());
        verify(activityRepository).findAllByProviderId(any(), any());
    }

    @Test
    void testDeleteActivity() {
        doNothing().when(activityRepository).deleteById(any());
        when(activityRepository.existsById(any())).thenReturn(true);
        activityServiceImpl.deleteActivity(1L);
        verify(activityRepository).existsById(any());
        verify(activityRepository).deleteById(any());
    }

    @Test
    void testUpdateActivity() {
        Provider provider = new Provider();
        Util.setProvider(provider);
        Activity activity = new Activity();
        Util.setActivity(activity, provider);
        Optional<Activity> ofResult = Optional.of(activity);

        when(activityRepository.save(any())).thenReturn(activity);
        when(activityRepository.findById(any())).thenReturn(ofResult);
        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO(1L, "Name", 10.0d, null, 1L,
                new ArrayList<>());
        when(activityMapper.toActivityResponseDTO(any())).thenReturn(activityResponseDTO);
        doNothing().when(activityMapper).updateActivityFromDto(any(), any());
        when(employeeService.getByIdsAndProviderId( any(), any())).thenReturn(new ArrayList<>());
        assertSame(activityResponseDTO,
                activityServiceImpl.updateActivity(new ActivityUpdateDTO("Name", 10.0d, new ArrayList<>()), 1L));
        verify(activityRepository).save(any());
        verify(activityRepository).findById(any());
        verify(activityMapper).toActivityResponseDTO(any());
        verify(activityMapper).updateActivityFromDto(any(), any());
        verify(employeeService).getByIdsAndProviderId(any(), any());
    }
}
