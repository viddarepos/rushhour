package com.rushhour_app.infrastructure.security.service;

import com.rushhour_app.domain.account.entity.Account;
import com.rushhour_app.domain.account.repository.AccountRepository;
import com.rushhour_app.infrastructure.exception.customException.ForbidenException;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import com.rushhour_app.infrastructure.security.model.AccountUserDetails;
import com.rushhour_app.infrastructure.security.model.LoginRequestDTO;
import com.rushhour_app.infrastructure.security.model.LoginResponseDTO;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.signing-secret}")
    private String signingSecret;

    @Value("${jwt.expiration-time-seconds}")
    private long expirationTime;

    @Autowired
    public AuthServiceImpl(AccountRepository accountRepository, PasswordEncoder encoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = encoder;
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {

        var loginEmail = loginRequestDTO.email();
        Optional<Account> optionalAccount = accountRepository.findByEmail(loginEmail);

        if (optionalAccount.isEmpty()) {
            throw new NotFoundException("Bad req");
        }

        Account account = optionalAccount.get();
        var passwordMatches = passwordEncoder.matches(loginRequestDTO.password(), account.getPassword());

        if (!passwordMatches) {
            throw new NotFoundException("Bad credentials");
        }

        return new LoginResponseDTO(generateToken(account));
    }

    @Override
    public AccountUserDetails validateAndGetDetails(String token) {

        String email = validateToken(token);
        var optionalEmail = accountRepository.findByEmail(email);

        if (optionalEmail.isEmpty()) {
            throw new NotFoundException("Account not found");
        }

        return new AccountUserDetails(optionalEmail.get());
    }


    public String generateToken(Account account) {

        var claims = new HashMap<String, Object>();
        claims.put("id", account.getId());
        claims.put("role", account.getRole().getName());

        var now = System.currentTimeMillis();
        var exp = now + expirationTime * 1000;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(account.getEmail())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(exp))
                .signWith(SignatureAlgorithm.HS512, signingSecret).compact();
    }

    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(signingSecret).parseClaimsJws(token).getBody();

            return claims.getSubject();
        } catch (SignatureException | ExpiredJwtException ex) {
            throw new ForbidenException("Token invalid");
        }
    }
}
