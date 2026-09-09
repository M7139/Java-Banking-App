package com.ga.project;

public class Main {
    public static void main(String[] args) {
//        PasswordEncryptor encryptor = new PasswordEncryptor();
//
//        String encrypted = encryptor.encrypt("myPassword123");
//        System.out.println("Encrypted: " + encrypted);
//
//        System.out.println("Correct password verifies: " + encryptor.verify("myPassword123", encrypted));  //  true
//        System.out.println("Wrong password verifies: " + encryptor.verify("wrongPassword", encrypted));    //  false

        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        Authentication authentication = new Authentication(passwordEncryptor);

        String rawPassword = "myPassword123";
        String encrypted = passwordEncryptor.encrypt(rawPassword);
        Customer customer = new Customer("10001", "Saad Iqbal", encrypted);

        // 1: correct password
        String result1 = authentication.login(customer, rawPassword);
        System.out.println("Test 1 - Correct password: " + result1);
        System.out.println("Failed count after success: " + customer.getFailedLoginCount());

        // 2: wrong password
        String result2 = authentication.login(customer, "wrongPassword");
        System.out.println("Test 2 - Wrong password: " + result2);
        System.out.println("Failed count after 1 wrong attempt: " + customer.getFailedLoginCount());

        // 3: two fails = 3 failures
        authentication.login(customer, "wrongPassword");
        authentication.login(customer, "wrongPassword");
        System.out.println("Failed count after 3 total wrong attempts: " + customer.getFailedLoginCount());

        // 4: should be locked
        String result4 = authentication.login(customer, rawPassword);
        System.out.println("Test 4 - Login after lockout: " + result4);
    }
}

