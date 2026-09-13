package com.ga.project;

public class Banker extends User {

    public Banker(String id, String firstName, String lastName, String encryptedPassword) {
        super(id, firstName, lastName, encryptedPassword);
    }

    @Override
    public String getRole() {
        return "Banker";
    }
}