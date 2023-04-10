package Chord;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * MainServer provides the interface through which users can create and verify password entries to be stored in Chord.
 *
 * @author Edison Ooi, Brandon Bae
 */
public class MainServer {
    private static final int N = 3;
    private static Wrapper wrapper;
    private static SecureRandom random;
    private static MessageDigest digest;

    public static void main(String[] args) {
        String ip = args[0];
        String port = args[1];

        wrapper = new Wrapper(ip, port);

        random = new SecureRandom();
        try {
            digest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("UNABLE TO GET INSTANCE OF DIGEST");
        }

        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("Enter an operation (create or verify), or exit to quit: ");
            String command = sc.nextLine();

            if(command.equals("exit")) {
                break;
            } else if (command.equals("create")) {
                System.out.println("Enter username: ");
                String username = sc.nextLine();
                System.out.println("Enter password: ");
                String password = sc.nextLine();

//                long start = System.nanoTime();
                if(createPasswordEntry(username, password)) {
//                    System.out.println(((System.nanoTime() - start) / 1000000000.0) + " seconds");
                    System.out.println("Entry successfully created for user " + username);
                } else {
                    System.out.println("Error creating entry for user " + username);
                }
            } else if (command.equals("verify")) {
                System.out.println("Enter username: ");
                String username = sc.nextLine();
                System.out.println("Enter password: ");
                String password = sc.nextLine();

                if(verifyPassword(username, password)) {
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

        for(int i = 0; i < split.size(); i++) {
            String part = split.get(i);

            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String saltString = new String(salt, Charset.forName("UTF-8"));
            String hashedPart = hashSaltedPart(part, saltString);

            wrapper.insert(username + "salt" + i, saltString);
            wrapper.insert(username + "hash" + i, hashedPart);
        }

        return true;
    }

    private static boolean verifyPassword(String username, String password) {
        List<String> split = splitIntoNParts(password);
        StringBuilder passwordHash = new StringBuilder();

        for(int i = 0; i < split.size(); i++) {
            String part = split.get(i);
            String saltString = wrapper.get(username + "salt" + i);
            String hashedPart = hashSaltedPart(part, saltString);
            passwordHash.append(hashedPart);
        }

        StringBuilder correctHash = new StringBuilder();
        for(int i = 0; i < N; i++) {
            correctHash.append(wrapper.get(username + "hash" + i));
        }

        return passwordHash.toString().equals(correctHash.toString());
    }

    public static List<String> splitIntoNParts(String s) {
        List<String> result = new ArrayList<>();
        int len = s.length() / N;
        int rem = s.length() % N;
        for (int i = 0, pos = 0; i < N; i++) {
            int end = pos + len + (rem-- > 0 ? 1: 0);
            result.add(s.substring(pos, end));
            pos = end;
        }
        return result;
    }

    private static String hashSaltedPart(String part, String salt) {
        String saltedPart = part + salt; // Suffix salt
        final byte[] bytes = digest.digest(saltedPart.getBytes(StandardCharsets.UTF_8));

        return bytesToHex(bytes);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

