package com.group11.shoppuka.project.model.account;

public class UserData {
    private String phoneNumber;
    private String password;
    private String fullName;

    private int idMode;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getIdMode() {
        return idMode;
    }

    public void setIdMode(int idMode) {
        this.idMode = idMode;
    }
}
