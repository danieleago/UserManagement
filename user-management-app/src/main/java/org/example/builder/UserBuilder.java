package org.example.builder;

import org.example.api.model.User;

public class UserBuilder {
    private Integer id;
    private String firstName;
    private String lastName;

    private String email;

    private String address;

    public UserBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder address(String address) {
        this.address = address;
        return this;
    }

    public User build(){

        return new User()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .address(this.address)
                .email(this.email);
    }
}
