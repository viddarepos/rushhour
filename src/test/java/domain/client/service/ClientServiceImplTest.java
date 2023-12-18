package domain.client.service;

import com.rushhour_app.domain.account.entity.Account;
import com.rushhour_app.domain.account.model.AccountUpdateDTO;
import com.rushhour_app.domain.account.model.ClientAccountDTO;
import com.rushhour_app.domain.account.service.AccountService;
import com.rushhour_app.domain.client.entity.Client;
import com.rushhour_app.domain.client.model.ClientDTO;
import com.rushhour_app.domain.client.model.ClientResponseDTO;
import com.rushhour_app.domain.client.model.ClientUpdateDTO;
import com.rushhour_app.domain.client.repository.ClientRepository;
import com.rushhour_app.domain.client.service.ClientServiceImpl;
import com.rushhour_app.domain.role.entity.Role;
import com.rushhour_app.domain.role.service.RoleService;
import com.rushhour_app.infrastructure.mappers.ClientMapper;
import domain.utils.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ClientServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ClientServiceImplTest {

    @MockBean
    private AccountService accountService;

    @MockBean
    private ClientMapper clientMapper;

    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private ClientServiceImpl clientServiceImpl;

    @MockBean
    private RoleService roleService;

    @Test
    void testCreateClient() {
        Account account = new Account();
        Util.setAccount(account);
        Client client = new Client();
        Util.setClient(client, account);

        ClientAccountDTO accountDTO = new ClientAccountDTO(1L, "mail@gmail.com", "David", "David!123", 1L);
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(123L, "4105551212", "42 Main St", accountDTO);

        when(clientRepository.save(any())).thenReturn(client);
        when(clientMapper.toClientResponseDTO(any())).thenReturn(clientResponseDTO);
        when(clientMapper.toClient(any())).thenReturn(client);

        Role role2 = new Role();
        role2.setId(1L);
        role2.setName("Name");
        when(roleService.getRoleByName(any())).thenReturn(role2);
        doNothing().when(accountService).assertClientAccountDoesNotExists(any());
        assertSame(clientResponseDTO, clientServiceImpl.createClient(new ClientDTO(1L, "4105551212", "address",
                new ClientAccountDTO(1L, "david@hotmail.com", "Ime", "David123@", 1L))));
        verify(clientRepository).save(any());
        verify(clientMapper).toClient(any());
        verify(clientMapper).toClientResponseDTO(any());
        verify(roleService).getRoleByName(any());
        verify(accountService).assertClientAccountDoesNotExists(any());
    }

    @Test
    void testGetById() {
        Account account = new Account();
        Util.setAccount(account);
        Client client = new Client();
        Util.setClient(client, account);
        Optional<Client> ofResult = Optional.of(client);

        ClientAccountDTO accountDTO = new ClientAccountDTO(1L, "mail@gmail.com", "David", "David!123", 1L);
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(1L, "123123123", "Address", accountDTO);

        when(clientRepository.findById(any())).thenReturn(ofResult);
        when(clientMapper.toClientResponseDTO(any())).thenReturn(clientResponseDTO);
        assertSame(clientResponseDTO, clientServiceImpl.getById(1L));
        verify(clientRepository).findById(any());
        verify(clientMapper).toClientResponseDTO(any());
    }


    @Test
    void testGetPage() {
        when(clientRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(clientServiceImpl.getPage(null).toList().isEmpty());
        verify(clientRepository).findAll((Pageable) any());
    }

    @Test
    void testDeleteClient() {
        doNothing().when(clientRepository).deleteById((Long) any());
        when(clientRepository.existsById((Long) any())).thenReturn(true);
        clientServiceImpl.deleteClient(1L);
        verify(clientRepository).existsById((Long) any());
        verify(clientRepository).deleteById((Long) any());
    }

    @Test
    void testUpdateClient() {
        Account account = new Account();
        Util.setAccount(account);
        Client client = new Client();
        Util.setClient(client, account);
        Optional<Client> ofResult = Optional.of(client);

        ClientAccountDTO accountDTO = new ClientAccountDTO(1L, "mail@gmail.com", "David", "David!123", 1L);
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(1L, "123123123", "Street312", accountDTO);

        when(clientRepository.save(any())).thenReturn(client);
        when(clientRepository.findById(any())).thenReturn(ofResult);
        when(clientMapper.toClientResponseDTO(any())).thenReturn(clientResponseDTO);
        doNothing().when(clientMapper).updateEmployeeFromDto(any(), any());
        assertSame(clientResponseDTO, clientServiceImpl.updateClient(
                new ClientUpdateDTO("4105551212", "Address", new AccountUpdateDTO("David", "Password123")), 1L));
        verify(clientRepository).save(any());
        verify(clientRepository).findById(any());
        verify(clientMapper).toClientResponseDTO(any());
        verify(clientMapper).updateEmployeeFromDto(any(), any());
    }
}



