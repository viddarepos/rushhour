package com.rushhour_app.domain.client.service;

import com.rushhour_app.domain.account.service.AccountService;
import com.rushhour_app.domain.appointment.models.AppointmentDTO;
import com.rushhour_app.domain.client.entity.Client;
import com.rushhour_app.domain.client.model.ClientDTO;
import com.rushhour_app.domain.client.model.ClientResponseDTO;
import com.rushhour_app.domain.client.model.ClientUpdateDTO;
import com.rushhour_app.domain.client.repository.ClientRepository;
import com.rushhour_app.domain.employee.entity.Employee;
import com.rushhour_app.domain.role.entity.Role;
import com.rushhour_app.domain.role.enums.RoleNames;
import com.rushhour_app.domain.role.service.RoleService;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.mappers.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final RoleService roleService;
    private final AccountService accountService;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, RoleService roleService, AccountService accountService) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.roleService = roleService;
        this.accountService = accountService;
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Client with id " + id + " does not exists"));

    }

    @Override
    public Optional<Client> findByAccountId(Long id) {
        return clientRepository.findByAccountId(id);
    }

    @Override
    public ClientResponseDTO createClient(ClientDTO clientDTO) {
        accountService.assertClientAccountDoesNotExists(clientDTO.account());
        Role role = roleService.getRoleByName(RoleNames.CLIENT.name());
        Client client = clientMapper.toClient(clientDTO);
        client.getAccount().setRole(role);
        Client createClient = clientRepository.save(client);

        return clientMapper.toClientResponseDTO(createClient);
    }

    @Override
    public ClientResponseDTO getById(Long id) {
        Client client = getClientById(id);

        return clientMapper.toClientResponseDTO(client);
    }

    @Override
    public Page<ClientResponseDTO> getPage(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::toClientResponseDTO);
    }

    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new NotFoundException("Client with id " + id + " does not exists");
        }
        clientRepository.deleteById(id);
    }

    @Override
    public ClientResponseDTO updateClient(ClientUpdateDTO request, Long id) {
        var currentClient = getClientById(id);
        clientMapper.updateEmployeeFromDto(request, currentClient);
        clientRepository.save(currentClient);

        return clientMapper.toClientResponseDTO(currentClient);
    }

    public Client getByAccountId(Long id) {
        var clientOptional = clientRepository.findByAccountId(id);
        if (clientOptional.isEmpty()) {
            throw new NotFoundException("Client with account id " + id + " does not exists");
        }
        return clientOptional.get();
    }

}

