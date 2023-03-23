import java.util.*;

public class MainServer {
    private static final int N = 3;
    private static Wrapper wrapper;

    public static void main(String[] args) {
        String ip = args[0];
        String port = args[1];

        wrapper = new Wrapper(ip, port);
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

                long start = System.nanoTime();
                if(createPasswordEntry(username, password)) {
                    System.out.println(((System.nanoTime() - start) / 1000000000.0) + " seconds");
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
            System.out.println(part);
            wrapper.insert(username + "hash" + i, part);
        }

        for(int i = 0; i < N; i++) {
            System.out.println(wrapper.get(username + "hash" + i));
        }
        return true;
    }

    private static boolean verifyPassword(String username, String password) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < N; i++) {
            sb.append(wrapper.get(username + "hash" + i));
        }

        return password.equals(sb.toString());
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
}
