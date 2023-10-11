package org.example.service;

import org.example.model.I18nException;
import org.example.api.model.User;
import org.springframework.core.io.Resource;

import java.util.List;

public interface UserManagementService {

    User getUserById(int id) throws I18nException;

    List<User> searchUser(String keyword);
    int insertUser(User user);

    void updateUser(User user) throws I18nException;

    void deleteUser(int id) throws I18nException;

    void uploadUser(Resource resource) throws I18nException;

}
