package com.app.service.repository;

import com.app.entity.User;
import com.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
public class UserRepository_RepoTest {

//    @Autowired
//    private UserRepository userRepository;

//    @Test
    void findByUsername() {
        // given
        User user = new User();
        //when

        //then

    }


}
