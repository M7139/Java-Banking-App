package com.ga.project;

import java.time.LocalDateTime;

public abstract class User {
    protected String id;
    protected String name;
    protected String encryptedPassword;
    protected int failedLoginCount;
    protected LocalDateTime lockedUntil;

    public User(String id, String name, String encryptedPassword) {
        this.id = id;
        this.name = name;
        this.encryptedPassword = encryptedPassword;
        this.failedLoginCount = 0;
        this.lockedUntil = null;
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

    public int getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(int failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
}