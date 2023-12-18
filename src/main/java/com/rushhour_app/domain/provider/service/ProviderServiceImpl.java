package com.rushhour_app.domain.provider.service;

import com.rushhour_app.domain.employee.model.EmployeeDTO;
import com.rushhour_app.domain.provider.entity.Provider;
import com.rushhour_app.domain.provider.model.ProviderDTO;
import com.rushhour_app.domain.provider.model.ProviderResponseDTO;
import com.rushhour_app.domain.provider.model.ProviderUpdateDTO;
import com.rushhour_app.domain.provider.repository.ProviderRepository;
import com.rushhour_app.infrastructure.exception.customException.ConflictException;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.mappers.ProviderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;

    private final ProviderMapper providerMapper;

    @Autowired
    public ProviderServiceImpl(ProviderRepository providerRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
    }

    public Optional<Provider> findProviderById(EmployeeDTO employeeDTO) {
        return providerRepository.findById(employeeDTO.providerId());
    }

    @Override
    public Provider findProviderByAccountId(Long accountId) {
        return providerRepository.findProviderByAccountId(accountId);
    }

    @Override
    public Optional<Provider> getProviderByEmployeeAccountId(Long id) {
        return providerRepository.findByEmployeeAccountId(id);
    }

    @Override
    public Provider getProviderById(Long id) {
        return providerRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Provider with id " + id + " does not exist!"));
    }

    @Override
    public void existsByProviderId(EmployeeDTO employeeDTO) {
        var existProvider = providerRepository.existsById(employeeDTO.providerId());

        if (!existProvider) {
            throw new NotFoundException("Provider with id " + employeeDTO.providerId() + " does not exist!");
        }
    }

    @Override
    public ProviderDTO checkIfProviderExists(ProviderDTO provider) {
        var providerName = provider.name();
        var providerDomain = provider.domain();
        var nameExists = providerRepository.existsByName(providerName);
        var domainExists = providerRepository.existsByDomain(providerDomain);

        if (nameExists) {
            throw new ConflictException(String.format("Provider with name %s, is already registered into database!", providerName));
        }

        if (domainExists) {
            throw new ConflictException(String.format("Provider with domain %s, is already registered into database!", providerDomain));
        }

        return provider;
    }

    @Override
    public ProviderResponseDTO create(ProviderDTO providerDTO) {
        checkIfProviderExists(providerDTO);
        Provider createdProvider = providerMapper.toProviderFromDTO(providerDTO);
        Provider savedProvider = providerRepository.save(createdProvider);

        return providerMapper.toProviderResponseDTO(savedProvider);

    }

    @Override
    public ProviderResponseDTO getById(Long id) {
        Provider provider = getProviderById(id);

        return providerMapper.toProviderResponseDTO(provider);
    }

    @Override
    public Page<ProviderResponseDTO> getPage(Pageable pageable) {
        return providerRepository.findAll(pageable).map(providerMapper::toProviderResponseDTO);
    }

    @Override
    public void delete(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new NotFoundException("Provider with id " + id + " does not exist!");
        }
        providerRepository.deleteById(id);
    }

    @Override
    public ProviderResponseDTO update(ProviderUpdateDTO request, Long id) {
        var currentProvider = getProviderById(id);

        providerMapper.updateProviderFromDto(request, currentProvider);
        providerRepository.save(currentProvider);

        return providerMapper.toProviderResponseDTO(currentProvider);

    }

}