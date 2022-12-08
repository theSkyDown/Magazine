/*
 * author:eccentric
 * time:2022/11/9
 */
package com.eccentric.pojo;

public class User {
    private Integer id;
    private String username;
    private String phone;
    private String password;
    private String address;

    private Integer isManager;

    public User() {
    }

    public User(Integer id, String username, String phone, String password, String address, Integer isManager) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.isManager = isManager;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", isManager=" + isManager +
                '}';
    }

    public Integer getIsManager() {
        return isManager;
    }

    public void setIsManager(Integer isManager) {
        this.isManager = isManager;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

