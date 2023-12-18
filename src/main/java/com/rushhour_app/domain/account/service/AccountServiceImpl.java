package com.rushhour_app.domain.account.service;

import com.rushhour_app.domain.account.model.ClientAccountDTO;
import com.rushhour_app.domain.account.model.EmployeeAccountDTO;
import com.rushhour_app.domain.account.repository.AccountRepository;
import com.rushhour_app.infrastructure.exception.customException.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void assertEmployeeAccountDoesNotExists(EmployeeAccountDTO accountDTO) {
        var userEmail = accountDTO.email();
        var alreadyEmailExists = accountRepository.existsByEmail(userEmail);

        if (alreadyEmailExists) {
            throw new ConflictException(String.format("User with email %s, is already present.", userEmail));
        }
    }

    @Override
    public void assertClientAccountDoesNotExists(ClientAccountDTO accountDTO) {
        var userEmail = accountDTO.email();
        var alreadyEmailExists = accountRepository.existsByEmail(userEmail);

        if (alreadyEmailExists) {
            throw new ConflictException(String.format("User with email %s, is already present.", userEmail));
        }
    }
}
