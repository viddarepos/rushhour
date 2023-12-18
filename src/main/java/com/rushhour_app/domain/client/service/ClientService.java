package com.rushhour_app.domain.client.service;

import com.rushhour_app.domain.appointment.models.AppointmentDTO;
import com.rushhour_app.domain.client.entity.Client;
import com.rushhour_app.domain.client.model.ClientDTO;
import com.rushhour_app.domain.client.model.ClientResponseDTO;
import com.rushhour_app.domain.client.model.ClientUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientService {

    ClientResponseDTO createClient(ClientDTO clientDTO);

    ClientResponseDTO getById(Long id);

    Page<ClientResponseDTO> getPage(Pageable pageable);

    void deleteClient(Long id);

    ClientResponseDTO updateClient(ClientUpdateDTO request, Long id);

    Client getClientById(Long id);

    Optional<Client>  findByAccountId(Long id);

}
