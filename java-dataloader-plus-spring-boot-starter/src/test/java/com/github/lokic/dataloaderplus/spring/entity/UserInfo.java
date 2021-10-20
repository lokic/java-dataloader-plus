package com.github.lokic.dataloaderplus.spring.entity;

public class UserInfo {
    private final String uid;
    private final String name;
    private final String address;

    public UserInfo(String uid, String name, String address) {
        this.uid = uid;
        this.name = name;
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
