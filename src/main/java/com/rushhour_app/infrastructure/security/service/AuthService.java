package com.rushhour_app.infrastructure.security.service;

import com.rushhour_app.infrastructure.security.model.AccountUserDetails;
import com.rushhour_app.infrastructure.security.model.LoginRequestDTO;
import com.rushhour_app.infrastructure.security.model.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO);

    AccountUserDetails validateAndGetDetails(String token);

}
