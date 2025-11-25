package com.example.userapi.domain.model;

import java.util.List;

public class User {
    private Long id;
    private String name;
    private String email;
    private List<Address> addressList;

    public User() {
    }

    public User(Long id, String name, String email, List<Address> addressList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.addressList = addressList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }


}
