package com.rushhour_app.web;

import com.rushhour_app.infrastructure.security.model.LoginRequestDTO;
import com.rushhour_app.infrastructure.security.model.LoginResponseDTO;
import com.rushhour_app.infrastructure.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/authenticate")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.authenticate(loginRequestDTO));

    }

}
