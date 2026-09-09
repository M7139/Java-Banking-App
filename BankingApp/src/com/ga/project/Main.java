package com.ga.project;

public class Main {
    public static void main(String[] args) {
        PasswordEncryptor encryptor = new PasswordEncryptor();

        String encrypted = encryptor.encrypt("myPassword123");
        System.out.println("Encrypted: " + encrypted);

        System.out.println("Correct password verifies: " + encryptor.verify("myPassword123", encrypted));  // expect true
        System.out.println("Wrong password verifies: " + encryptor.verify("wrongPassword", encrypted));    // expect false
    }
}