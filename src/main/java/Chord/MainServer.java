package Chord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class MainServer {
    private static final int N = 3;

    public MainServer() {
    }

    public static void main(String[] args) {
        new Wrapper();
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("Enter an operation (create or verify), or exit to quit: ");
            String command = sc.nextLine();
            if (command.equals("exit")) {
                return;
            }

            String username;
            String password;
            if (command.equals("create")) {
                System.out.println("Enter username: ");
                username = sc.nextLine();
                System.out.println("Enter password: ");
                password = sc.nextLine();
                if (createPasswordEntry(username, password)) {
                    System.out.println("Entry successfully created for user " + username);
                } else {
                    System.out.println("Error creating entry for user " + username);
                }
            } else if (command.equals("verify")) {
                System.out.println("Enter username: ");
                username = sc.nextLine();
                System.out.println("Enter password: ");
                password = sc.nextLine();
                if (verifyPassword(username, password)) {
                    System.out.println("Correct password");
                } else {
                    System.out.println("Incorrect password");
                }
            } else {
                System.out.println("Invalid operation");
            }
        }
    }

    private static boolean createPasswordEntry(String username, String password) {
        List<String> split = splitIntoNParts(password);
        Iterator var3 = split.iterator();

        while(var3.hasNext()) {
            String s = (String)var3.next();
            System.out.println(s);
        }

        return true;
    }

    private static boolean verifyPassword(String username, String password) {
        return true;
    }

    public static List<String> splitIntoNParts(String s) {
        List<String> result = new ArrayList();
        int len = s.length() / 3;
        int rem = s.length() % 3;
        int i = 0;

        for(int pos = 0; i < 3; ++i) {
            int end = pos + len + (rem-- > 0 ? 1 : 0);
            result.add(s.substring(pos, end));
            pos = end;
        }

        return result;
    }
}
