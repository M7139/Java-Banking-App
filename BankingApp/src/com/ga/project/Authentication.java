package com.ga.project;

import java.time.LocalDateTime;

public class Authentication {
    private PasswordEncryptor passwordEncryptor;

    public Authentication(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    public String login(User user, String rawPassword) {

        if (user.getLockedUntil() != null && LocalDateTime.now().isBefore(user.getLockedUntil())) {
            return "LOCKED";
        }

        boolean passwordCorrect = passwordEncryptor.verify(rawPassword, user.getEncryptedPassword());

        if (!passwordCorrect) {
            user.setFailedLoginCount(user.getFailedLoginCount() + 1);

            if (user.getFailedLoginCount() >= 3) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(1));
            }

            return "INVALID_PASSWORD";
        }

        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        return "SUCCESS";
    }
}