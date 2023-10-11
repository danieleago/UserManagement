package org.example.service.impl;


import org.apache.commons.lang3.StringUtils;
import org.example.model.I18nException;
import org.example.api.model.User;
import org.example.builder.UserBuilder;
import org.example.mapper.UserManagementMapper;
import org.example.service.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UserManagementServiceImpl implements UserManagementService {
    private static final Logger log = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    private static final String COMMA_DELIMITER = ",";

    @Autowired
    private UserManagementMapper mapper;

    @Override
    public User getUserById(int id) throws I18nException {
        log.debug("get user by id {}", id);
        User userById = mapper.getUserById(id);
        if (userById == null)
            throw new I18nException(404, "error.user.notFound", "User not found");
        return userById;
    }

    @Override
    public List<User> searchUser(String keyword) {
        log.debug("search user by keyword {}", keyword);
        if (keyword != null) {
            List<String> collect = Arrays.stream(keyword.split(" ")).filter(cs -> !cs.isBlank())
                    .map(String::trim).toList();
            keyword = StringUtils.join(collect, ":*&") + ":*";
        }
        return mapper.searchUser(keyword);
    }

    @Override
    public int insertUser(User user) {
        log.debug("insert user {}", user);
        return mapper.insertUser(user);
    }

    @Override
    public void updateUser(User user) throws I18nException {
        log.debug("update user {}", user);
        boolean upload = mapper.updateUser(user);
        if (!upload)
            throw new I18nException(404, "error.user.notFound", "User not found");
    }

    @Override
    public void deleteUser(int id) throws I18nException {
        log.debug("delete user with id {}", id);
        boolean upload = mapper.deleteUser(id);
        if (!upload)
            throw new I18nException(404, "error.user.notFound", "User not found");
    }

    @Override
    public void uploadUser(Resource resource) throws I18nException {
        log.debug("upload user list by csv file");
        List<User> users = parseCsv(resource);
        mapper.bulkUser(users);
    }

    private static List<User> parseCsv(Resource resource) throws I18nException {
        List<User> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader (new InputStreamReader(resource.getInputStream(), UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> row = Arrays.asList(line.split(COMMA_DELIMITER));
                if (row.isEmpty())
                    continue;
                else if (row.size() != 4){
                    throw new I18nException(400, "error.user.upload", "Malformatted CSV file");
                } else {
                    userList.add(new UserBuilder()
                            .firstName(row.get(0))
                            .lastName(row.get(1))
                            .address(row.get(2))
                            .email(row.get(3))
                            .build());
                }
            }
        } catch (IOException e) {
            throw new I18nException(400, "error.user.upload", "Failed to get the resource");
        }
        return userList;
    }
}
