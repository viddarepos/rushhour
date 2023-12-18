package domain.account.service;

import com.rushhour_app.domain.account.model.ClientAccountDTO;
import com.rushhour_app.domain.account.model.EmployeeAccountDTO;
import com.rushhour_app.domain.account.repository.AccountRepository;
import com.rushhour_app.domain.account.service.AccountService;
import com.rushhour_app.domain.account.service.AccountServiceImpl;
import com.rushhour_app.infrastructure.exception.customException.ConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    @Test
    public void testClientAccountDoesNotExists_WhenEmailExists_ShouldThrowConflictException() {
        when(accountRepository.existsByEmail(any())).thenReturn(true);
        assertThrows(ConflictException.class, () -> accountServiceImpl.assertEmployeeAccountDoesNotExists(
                new EmployeeAccountDTO(123L, "david@hotmail.com", "David", "David123@", 1L)));

        verify(accountRepository).existsByEmail(any());

    }

    @Test
    void testAssertEmployeeAccountDoesNotExists2() {
        when(accountRepository.existsByEmail(any())).thenReturn(false);
        accountServiceImpl.assertEmployeeAccountDoesNotExists(
                new EmployeeAccountDTO(123L, "david@hotmail.com", "David", "David123@", 1L));
        verify(accountRepository).existsByEmail(any());
    }

    @Test
    void testAssertEmployeeAccountDoesNotExists4() {
        when(accountRepository.existsByEmail(any())).thenThrow(new ConflictException("An error occurred"));
        assertThrows(ConflictException.class, () -> accountServiceImpl.assertEmployeeAccountDoesNotExists(
                new EmployeeAccountDTO(123L, "david@hotmail.com", "David", "David123@", 1L)));
        verify(accountRepository).existsByEmail(any());
    }

    @Test
    void testAssertClientAccountDoesNotExists() {
        when(accountRepository.existsByEmail(any())).thenReturn(true);
        assertThrows(ConflictException.class, () -> accountServiceImpl.assertClientAccountDoesNotExists(
                new ClientAccountDTO(123L, "david@hotmail.com", "David", "David123@", 1L)));
        verify(accountRepository).existsByEmail(any());
    }

    @Test
    void testAssertClientAccountDoesNotExists2() {
        when(accountRepository.existsByEmail(any())).thenReturn(false);
        accountServiceImpl.assertClientAccountDoesNotExists(
                new ClientAccountDTO(123L, "david@hotmail.com", "David", "David123@", 1L));
        verify(accountRepository).existsByEmail(any());
    }

    @Test
    void testAssertClientAccountDoesNotExists4() {
        when(accountRepository.existsByEmail(any())).thenThrow(new ConflictException("An error occurred"));
        assertThrows(ConflictException.class, () -> accountServiceImpl.assertClientAccountDoesNotExists(
                new ClientAccountDTO(123L, "david@hotmail.com", "David", "David123@", 1L)));
        verify(accountRepository).existsByEmail(any());
    }
}





