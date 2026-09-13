package com.ga.project;

public class Banker extends User {

    public Banker(String id, String name, String encryptedPassword) {
        super(id, name, encryptedPassword);
    }

    @Override
    public String getRole() {
        return "Banker";
    }
}