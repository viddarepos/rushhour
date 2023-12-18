package com.rushhour_app.web;

import com.rushhour_app.domain.provider.model.ProviderDTO;
import com.rushhour_app.domain.provider.model.ProviderResponseDTO;
import com.rushhour_app.domain.provider.model.ProviderUpdateDTO;
import com.rushhour_app.domain.provider.service.ProviderService;
import com.rushhour_app.infrastructure.security.model.AccountUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/providers")
public class ProviderController {

    private final ProviderService providerService;

    @Autowired
    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<ProviderResponseDTO> createProvider(@Valid @RequestBody ProviderDTO provider) {
        return ResponseEntity.ok(providerService.create(provider));
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
        hasRole('ADMINISTRATOR') ||
        hasRole('CLIENT') ||
        (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccess(#id))
        """)
    public ResponseEntity<ProviderResponseDTO> getProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.getById(id));
    }

    @GetMapping
    @PreAuthorize("""
        hasRole('ADMINISTRATOR') ||
        hasRole('CLIENT')
        """)
    public ResponseEntity<Page<ProviderResponseDTO>> getPageProvider(Pageable pageable) {
        return ResponseEntity.ok(providerService.getPage(pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        providerService.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("""
            hasRole('ADMINISTRATOR') || 
            (hasRole('PROVIDER_ADMINISTRATOR') && @permissionService.canProviderAdminAccess(#id))
            """)
    public ResponseEntity<ProviderResponseDTO> updateProvider(@Valid @RequestBody ProviderUpdateDTO providerDTO, @PathVariable Long id) {
        return ResponseEntity.ok().body(providerService.update(providerDTO, id));
    }
}
