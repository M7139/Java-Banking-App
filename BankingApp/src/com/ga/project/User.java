package com.ga.project;



public abstract class User {
    protected String id;
    protected String name;
    protected String encryptedPassword;

    public User(String id, String name, String encryptedPassword) {
        this.id = id;
        this.name = name;
        this.encryptedPassword = encryptedPassword;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}