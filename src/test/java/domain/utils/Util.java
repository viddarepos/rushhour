package domain.utils;

import com.rushhour_app.domain.account.entity.Account;
import com.rushhour_app.domain.activity.entity.Activity;
import com.rushhour_app.domain.client.entity.Client;
import com.rushhour_app.domain.employee.entity.Employee;
import com.rushhour_app.domain.provider.entity.Provider;
import com.rushhour_app.domain.role.entity.Role;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Util {

    public static Account setAccount(Account account) {
        Role role = new Role();
        role.setId(1L);
        role.setName("CLIENT");

        account.setId(1L);
        account.setEmail("email@gmail.com");
        account.setFullName("David");
        account.setPassword("Password123!");
        account.setRole(role);

        return account;
    }

    public static Provider setProvider(Provider provider) {
        provider.setId(1L);
        provider.setDomain("Domain");
        provider.setName("Name");
        provider.setWebsite("Website");
        provider.setPhone("1234231233");
        provider.setStartTime(LocalTime.of(1, 1));
        provider.setEndTime(LocalTime.of(1, 1));
        provider.setWorkingDays(new ArrayList<>());
        provider.setActivities(new ArrayList<>());

        return provider;
    }

    public static Employee setEmployee(Employee employee, Account account, Provider provider) {
        employee.setId(1L);
        employee.setPhone("123123123");
        employee.setTitle("Title");
        employee.setRate(1.0d);
        employee.setDate(LocalDate.ofEpochDay(1L));
        employee.setAccount(account);
        employee.setProvider(provider);
        employee.setActivities(new ArrayList<>());

        return employee;
    }

    public static Client setClient(Client client, Account account) {
        client.setId(1L);
        client.setPhone("123123123");
        client.setAddress("Adress");
        client.setAccount(account);

        return client;
    }
    public static Activity setActivity(Activity activity,Provider provider) {

        Account account = new Account();
        setAccount(account);

        activity.setId(1L);
        activity.setDuration(null);
        activity.setName("Activity");
        activity.setPrice(2.0);
        activity.setProvider(setProvider(provider));
        activity.setEmployees(new ArrayList<>());
        return activity;
    }

}
