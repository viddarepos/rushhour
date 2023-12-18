package com.rushhour_app.domain.provider.service;

import com.rushhour_app.domain.employee.model.EmployeeDTO;
import com.rushhour_app.domain.provider.entity.Provider;
import com.rushhour_app.domain.provider.model.ProviderDTO;
import com.rushhour_app.domain.provider.model.ProviderResponseDTO;
import com.rushhour_app.domain.provider.model.ProviderUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProviderService {

    ProviderResponseDTO create(ProviderDTO provider);

    ProviderResponseDTO getById(Long id);

    Page<ProviderResponseDTO> getPage(Pageable pageable);

    void delete(Long id);

    ProviderResponseDTO update(ProviderUpdateDTO request, Long id);

    Optional<Provider> findProviderById(EmployeeDTO employeeDTO);

    Provider findProviderByAccountId(Long accountId);

    Provider getProviderById(Long id);

    void existsByProviderId(EmployeeDTO employeeDTO);

    ProviderDTO checkIfProviderExists(ProviderDTO provider);

    Optional<Provider> getProviderByEmployeeAccountId(Long id);
}


