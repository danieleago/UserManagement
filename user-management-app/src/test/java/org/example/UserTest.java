package org.example;

import org.example.api.model.User;
import org.example.builder.UserBuilder;
import org.example.model.I18nException;
import org.example.service.UserManagementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;

class UserTest extends AbstractTest{

    @Autowired
    private UserManagementService service;

    @Test
    void insertSuccess(){

        User user = new UserBuilder()
                .firstName("Mario")
                .lastName("Rossi")
                .address("via Roma 1")
                .email("rossi@example.org")
                .build();
        int id = service.insertUser(user);
        Assertions.assertNotEquals(0, id);
    }

    @Test
    void getSuccess() {

        User user = new UserBuilder()
                .firstName("Mario")
                .lastName("Rossi")
                .address("via Roma 1")
                .email("rossi@example.org")
                .build();
        int id = service.insertUser(user);
        User userById = service.getUserById(id);
        Assertions.assertNotNull(userById);
        Assertions.assertEquals(id, userById.getId());
        Assertions.assertEquals(user.getFirstName(), userById.getFirstName());
        Assertions.assertEquals(user.getLastName(), userById.getLastName());
        Assertions.assertEquals(user.getAddress(), userById.getAddress());
        Assertions.assertEquals(user.getEmail(), userById.getEmail());
    }

    @Test
    void getNotFound(){

        User user = new UserBuilder()
                .firstName("Mario")
                .lastName("Rossi")
                .address("via Roma 1")
                .email("rossi@example.org")
                .build();
        int id = service.insertUser(user);
        I18nException i18nException = Assertions.assertThrows(I18nException.class, () -> service.getUserById(id + 1));
        Assertions.assertEquals("error.user.notFound", i18nException.getCode());
        Assertions.assertEquals(404, i18nException.getStatus());

    }

    @Test
    void searchSuccess(){

        User user = new UserBuilder()
                .firstName("Mario")
                .lastName("Rossi")
                .address("via Roma 1")
                .email("rossi@example.org")
                .build();
        service.insertUser(user);
        List<User> userList = service.searchUser(null);
        Assertions.assertFalse(userList.isEmpty());

        userList = service.searchUser("ros");
        Assertions.assertFalse(userList.isEmpty());

        userList = service.searchUser("ma ros");
        Assertions.assertFalse(userList.isEmpty());

        userList = service.searchUser("fab");
        Assertions.assertTrue(userList.isEmpty());

        userList = service.searchUser("ma ver");
        Assertions.assertTrue(userList.isEmpty());
    }

    @Test
    void updateSuccess() {

        User user = new UserBuilder()
                .firstName("Mario")
                .lastName("Rossi")
                .address("via Roma 1")
                .email("rossi@example.org")
                .build();
        int id = service.insertUser(user);

        user.setId(id);
        user.setFirstName("Franco");
        user.setLastName("Pozzo");
        user.address("via Grande 10");
        user.email("pozzo3@example.org");
        service.updateUser(user);

        User userById = service.getUserById(id);
        Assertions.assertNotNull(userById);
        Assertions.assertEquals(id, userById.getId());
        Assertions.assertEquals(user.getFirstName(), userById.getFirstName());
        Assertions.assertEquals(user.getLastName(), userById.getLastName());
        Assertions.assertEquals(user.getAddress(), userById.getAddress());
        Assertions.assertEquals(user.getEmail(), userById.getEmail());

    }

    @Test
    void updateFailed(){

        User user = new UserBuilder()
                .firstName("Mario")
                .lastName("Rossi")
                .address("via Roma 1")
                .email("rossi@example.org")
                .build();
        int id = service.insertUser(user);

        user.setId(id + 1);
        user.setFirstName("Franco");
        user.setLastName("Pozzo");
        user.address("via Grande 10");
        user.email("pozzo3@example.org");

        I18nException i18nException = Assertions.assertThrows(I18nException.class, () -> service.updateUser(user));
        Assertions.assertEquals("error.user.notFound", i18nException.getCode());
        Assertions.assertEquals(404, i18nException.getStatus());
    }

    @Test
    void deleteSuccess() {

        User user = new UserBuilder()
                .firstName("Mario")
                .lastName("Rossi")
                .address("via Roma 1")
                .email("rossi@example.org")
                .build();
        int id = service.insertUser(user);

        service.deleteUser(id);

        I18nException i18nException = Assertions.assertThrows(I18nException.class, () -> service.getUserById(id));
        Assertions.assertEquals("error.user.notFound", i18nException.getCode());
        Assertions.assertEquals(404, i18nException.getStatus());

        user.setId(id);
        i18nException = Assertions.assertThrows(I18nException.class, () -> service.updateUser(user));
        Assertions.assertEquals("error.user.notFound", i18nException.getCode());
        Assertions.assertEquals(404, i18nException.getStatus());

        i18nException = Assertions.assertThrows(I18nException.class, () -> service.deleteUser(id));
        Assertions.assertEquals("error.user.notFound", i18nException.getCode());
        Assertions.assertEquals(404, i18nException.getStatus());
    }

    @Test
    void deleteFailed() {

        User user = new UserBuilder()
                .firstName("Mario")
                .lastName("Rossi")
                .address("via Roma 1")
                .email("rossi@example.org")
                .build();

        int id = service.insertUser(user);

        I18nException i18nException = Assertions.assertThrows(I18nException.class, () -> service.deleteUser(id + 1));
        Assertions.assertEquals("error.user.notFound", i18nException.getCode());
        Assertions.assertEquals(404, i18nException.getStatus());

        User userById = service.getUserById(id);
        Assertions.assertNotNull(userById);

    }

    @Test
    void uploadSuccess() {

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("data/user.csv");
        service.uploadUser(resource);
        List<User> userList = service.searchUser(null);
        Assertions.assertEquals(4, userList.size());
    }

    @Test
    void uploadFailed() {

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("data/user_failed.csv");
        I18nException i18nException = Assertions.assertThrows(I18nException.class, () -> service.uploadUser(resource));
        Assertions.assertEquals("error.user.upload", i18nException.getCode());
        Assertions.assertEquals(400, i18nException.getStatus());

    }
}
