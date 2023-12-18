package com.rushhour_app.domain.account.service;


import com.rushhour_app.domain.account.model.ClientAccountDTO;
import com.rushhour_app.domain.account.model.EmployeeAccountDTO;

public interface AccountService {

    void assertEmployeeAccountDoesNotExists(EmployeeAccountDTO accountDTO);

    void assertClientAccountDoesNotExists(ClientAccountDTO accountDTO);
}
