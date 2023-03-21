import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.HashMap;

public class Wrapper {
    
    private static InetSocketAddress localAddress;
    private static Helper helper;


    public static void main(String[] args) {
        helper = new Helper();

        // 0. node ip 1. node port 2. operation 3. key 4. value

        // arg = 5 means insert
        if (args.length == 5) {
            System.out.println("Inserting key: " + args[3] + " value: " + args[4]);
			localAddress = Helper.createSocketAddress(args[0]+":"+args[1]);
            if (localAddress == null) {
				System.out.println("Cannot find address you are trying to contact. Now exit.");
				System.exit(0);;	
			}

            // successfully constructed socket address of the node we are 



        }   
    }

    public static String get(String key) {
        return " ";
    }

    public static String insert(String key, String value) {
        return " ";
    }
    
}
