package org.example.controller;

import org.example.api.UserApi;
import org.example.api.model.User;
import org.example.service.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserController implements UserApi {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserManagementService service;
    @Override
    public ResponseEntity<Void> createUser(User user) {
        log.debug("insert user {}", user);
        service.insertUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Integer id) {
        log.debug("delete user with id {}", id);
        service.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getUserById(Integer id) {
        log.debug("get user by id {}", id);
        User userById = service.getUserById(id);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<User>> getUserList(String search) {
        log.debug("search user by keyword {}", search);
        List<User> users = service.searchUser(search);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateUser(User user) {
        log.debug("update user {}", user);
        service.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> userListUpload(Resource body) {
        log.debug("bulk insert user");
        service.uploadUser(body);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
