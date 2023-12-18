package com.rushhour_app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = {
        BCryptPasswordEncoder.class
})
class RushhourApplicationTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        var password = "TestPassword123";
        var encodedPassword = passwordEncoder.encode(password);

        Assertions.assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

}
