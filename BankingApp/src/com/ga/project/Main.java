package com.ga.project;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Bank bank = new Bank(new FileManager());
    static PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
    static Authentication authentication = new Authentication(passwordEncryptor);

    public static void main(String[] args) {
        System.out.println("=== Welcome to ACME Bank ===");

        BankerMenu bankerMenu = new BankerMenu(scanner, bank);
        CustomerMenu customerMenu = new CustomerMenu(scanner, bank);

        while (true) {
            User loggedInUser = login();

            if (loggedInUser == null) {
                System.out.println("Exiting.");
                break;
            }

            System.out.println("\nWelcome, " + loggedInUser.getFullName() + " (" + loggedInUser.getRole() + ")");

            if (loggedInUser instanceof Banker) {
                bankerMenu.show((Banker) loggedInUser);
            } else if (loggedInUser instanceof Customer) {
                customerMenu.show((Customer) loggedInUser);
            }
        }
    }

    private static User login() {
        while (true) {
            System.out.print("\nEnter ID (or 'exit' to quit): ");
            String id = scanner.nextLine();

            if (id.equalsIgnoreCase("exit")) {
                return null;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            Optional<User> foundUser = bank.findUserById(id);

            if (!foundUser.isPresent()) {
                System.out.println("No account found with that ID.");
                continue;
            }

            User user = foundUser.get();
            String result = authentication.login(user, password);

            if (user instanceof Customer) {
                bank.saveCustomers();
            } else if (user instanceof Banker) {
                bank.saveBankers();
            }

            if (result.equals("SUCCESS")) {
                return user;
            } else if (result.equals("LOCKED")) {
                System.out.println("Account is locked until " + user.getLockedUntil() + ". Try a different account, or wait and try again.");
            } else {
                System.out.println("Incorrect password. Try again.");
            }
        }
    }
}