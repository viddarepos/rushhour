package com.rushhour_app.web;

import com.rushhour_app.domain.client.model.ClientDTO;
import com.rushhour_app.domain.client.model.ClientResponseDTO;
import com.rushhour_app.domain.client.model.ClientUpdateDTO;
import com.rushhour_app.domain.client.service.ClientService;
import com.rushhour_app.infrastructure.security.model.AccountUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.createClient(clientDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
        hasRole('ADMINISTRATOR') ||
        (hasRole('CLIENT') && @permissionService.canClientAccess(#id))
    """)
    public ResponseEntity<ClientResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @GetMapping
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') 
            """)
    public ResponseEntity<Page<ClientResponseDTO>> getPageClient(Pageable pageable) {
        return ResponseEntity.ok(clientService.getPage(pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') || 
            (hasRole('CLIENT') && @permissionService.canClientAccess(#id))
            """)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') || 
            (hasRole('CLIENT') && @permissionService.canClientAccess(#id))
            """)
    public ResponseEntity<ClientResponseDTO> updateClient(@Valid @RequestBody ClientUpdateDTO request, @PathVariable Long id) {
        return ResponseEntity.ok().body(clientService.updateClient(request, id));
    }
}
