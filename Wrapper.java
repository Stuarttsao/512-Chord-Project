import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.HashMap;

public class Wrapper {

    private static InetSocketAddress localAddress;
    private static InetSocketAddress targetAddress;
	private static HashMap<String, String> storage;
    private static Helper helper;

    public static void main(String[] args) {
        helper = new Helper();

        // 0. node ip 1. node port 2. key 3. value

        // arg = 3 means insert , arg = 4 means get
        if (args.length == 3 || args.length == 4) {
            localAddress = Helper.createSocketAddress(args[0] + ":" + args[1]);
            if (localAddress == null) {
                System.out.println("Cannot find address you are trying to contact. Now exit.");
                System.exit(0);
                ;
            }

            // successfully constructed socket address of the node we are
            // trying to contact, check if it's alive
            String response = Helper.sendRequest(localAddress, "KEEP");

            // if it's dead, exit
            if (response == null || !response.equals("ALIVE")) {
                System.out.println("\nCannot find node you are trying to contact. Now exit.\n");
                System.exit(0);
            }

            // it's alive, print connection info
            System.out.println("Connection to node " + localAddress.getAddress().toString() + ", port "
                    + localAddress.getPort() + ", position " + Helper.hexIdAndPosition(localAddress) + ".");

            // check if system is stable
            boolean pred = false;
            boolean succ = false;
            InetSocketAddress pred_addr = Helper.requestAddress(localAddress, "YOURPRE");
            InetSocketAddress succ_addr = Helper.requestAddress(localAddress, "YOURSUCC");
            if (pred_addr == null || succ_addr == null) {
                System.out.println("The node your are contacting is disconnected. Now exit.");
                System.exit(0);
            }
            if (pred_addr.equals(localAddress))
                pred = true;
            if (succ_addr.equals(localAddress))
                succ = true;

            // we suppose the system is stable if (1) this node has both valid
            // predecessor and successor or (2) none of them
            while (pred ^ succ) {
                System.out.println("Waiting for the system to be stable...");
                pred_addr = Helper.requestAddress(localAddress, "YOURPRE");
                succ_addr = Helper.requestAddress(localAddress, "YOURSUCC");
                if (pred_addr == null || succ_addr == null) {
                    System.out.println("The node your are contacting is disconnected. Now exit.");
                    System.exit(0);
                }
                if (pred_addr.equals(localAddress))
                    pred = true;
                else
                    pred = false;
                if (succ_addr.equals(localAddress))
                    succ = true;
                else
                    succ = false;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }

            }


            // search key location and print out response
            long hash = Helper.hashString(args[2]);
            System.out.println("\nHash value is " + Long.toHexString(hash));
            InetSocketAddress result = Helper.requestAddress(localAddress, "FINDSUCC_" + hash);

            // if fail to send request, local node is disconnected, exit
            if (result == null) {
                System.out.println("The node your are contacting is disconnected. Now exit.");
                System.exit(0);
            }

            // print out response
            System.out.println("\nResponse from node " + localAddress.getAddress().toString() + ", port "
                            + localAddress.getPort() + ", position " + Helper.hexIdAndPosition(localAddress) + ":");
            System.out.println("Target Node " + result.getAddress().toString() + ", port " + result.getPort()
                            + ", position " + Helper.hexIdAndPosition(result));

            targetAddress = Helper.createSocketAddress(result.getAddress().toString() + ":" + result.getPort());


                
            
            if(args.length == 4){
                // insert key value
                System.out.println("Inserting key: " + args[2] + " value: " + args[3] + " to node: " + targetAddress.getAddress().toString() + " port: " + targetAddress.getPort());
                String res = insert(args[2], args[3]);
                System.out.println(res);

            } else if(args.length == 3){
                // get key
                System.out.println("Getting key: " + args[2] + " from node: " + targetAddress.getAddress().toString() + " port: " + targetAddress.getPort());
                String res = get(args[2]);
                System.out.println(res);
            }

        }
    }

    public static String get(String key) {
        return "ret get ";
    }

    public static String insert(String key, String value) {
        return "ret insert ";
    }
    public static String connectToNode(String ip, String port){
        return "ret connectToNode ";
    }

}
