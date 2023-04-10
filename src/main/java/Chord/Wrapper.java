package Chord;

import DynamoDB.DynamoDBWrapper;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class Wrapper {
    /* need non static variables for instantiating wrapper instances
    private static InetSocketAddress localAddress;
    private static InetSocketAddress targetAddress;
    private static HashMap<String, String> storage;
    private static Chord.Helper helper;
     */
    private InetSocketAddress localAddress;
    private InetSocketAddress targetAddress;
    private HashMap<String, String> storage;
    private DynamoDBWrapper dbStorage;
    private Helper helper;

    public Wrapper(String ip, String port) {
        helper = new Helper();
        storage = new HashMap<String, String>();
        dbStorage = new DynamoDBWrapper();
        joinChordNetwork(ip, port);
    }



    public Wrapper() {
        storage = new HashMap<String, String>();
    }


    /**
     * Main method
     * @param args
     */
    /*
    public static void main(String[] args) {
        helper = new Chord.Helper();

        // 0. node ip 1. node port

        if (args.length == 2) {
            localAddress = Chord.Helper.createSocketAddress(args[0] + ":" + args[1]);
            if (localAddress == null) {
                System.out.println("Cannot find address you are trying to contact. Now exit.");
                System.exit(0);
            }

            // successfully constructed socket address of the node we are
            // trying to contact, check if it's alive
            String response = Chord.Helper.sendRequest(localAddress, "KEEP");

            // if it's dead, exit
            if (response == null || !response.equals("ALIVE")) {
                System.out.println("\nCannot find node you are trying to contact. Now exit.\n");
                System.exit(0);
            }

            // it's alive, print connection info
            System.out.println("Connection to node " + localAddress.getAddress().toString() + ", port "
                    + localAddress.getPort() + ", position " + Chord.Helper.hexIdAndPosition(localAddress) + ".");

            // check if system is stable
            boolean pred = false;
            boolean succ = false;
            InetSocketAddress pred_addr = Chord.Helper.requestAddress(localAddress, "YOURPRE");
            InetSocketAddress succ_addr = Chord.Helper.requestAddress(localAddress, "YOURSUCC");
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
                pred_addr = Chord.Helper.requestAddress(localAddress, "YOURPRE");
                succ_addr = Chord.Helper.requestAddress(localAddress, "YOURSUCC");
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

            Scanner userinput = new Scanner(System.in);
            while (true) {
                System.out.println("\nPlease enter insert or get(or type \"quit\" to leave): ");
                String command = userinput.nextLine();
                if (command.equals("quit")) {
                    System.out.println("Now exit.");
                    System.exit(0);
                    break;
                }
                if (command.equals("insert")) {
                    System.out.println("Please enter key: ");
                    String key = userinput.nextLine();
                    // search key location and print out response
                    long hash = Chord.Helper.hashString(key);
                    System.out.println("\nHash value is " + Long.toHexString(hash));
                    InetSocketAddress result = Chord.Helper.requestAddress(localAddress, "FINDSUCC_" + hash);

                    // if fail to send request, local node is disconnected, exit
                    if (result == null) {
                        System.out.println("The node your are contacting is disconnected. Now exit.");
                        System.exit(0);
                    }

                    // print out response
                    System.out.println("\nResponse from node " + localAddress.getAddress().toString() + ", port "
                            + localAddress.getPort() + ", position " + Chord.Helper.hexIdAndPosition(localAddress) + ":");
                    System.out.println("Target Chord.Node " + result.getAddress().toString() + ", port " + result.getPort()
                            + ", position " + Chord.Helper.hexIdAndPosition(result));

                    targetAddress = Chord.Helper.createSocketAddress(result.getAddress().toString() + ":" + result.getPort());

                    System.out.println("Please enter value: ");
                    String value = userinput.nextLine();
                    System.out.println("Inserting key: " + key + " value: " + value + " to node: "
                            + targetAddress.getAddress().toString() + " port: " + targetAddress.getPort());
                    String res = insertTempHashMap(key, value);
                    System.out.println(res);
                } else if (command.equals("get")) {
                    System.out.println("Please enter key: ");
                    String key = userinput.nextLine();

                    // search key location and print out response
                    long hash = Chord.Helper.hashString(key);
                    System.out.println("\nHash value is " + Long.toHexString(hash));
                    InetSocketAddress result = Chord.Helper.requestAddress(localAddress, "FINDSUCC_" + hash);

                    // if fail to send request, local node is disconnected, exit
                    if (result == null) {
                        System.out.println("The node your are contacting is disconnected. Now exit.");
                        System.exit(0);
                    }

                    // print out response
                    System.out.println("\nResponse from node " + localAddress.getAddress().toString() + ", port "
                            + localAddress.getPort() + ", position " + Chord.Helper.hexIdAndPosition(localAddress) + ":");
                    System.out.println("Target Chord.Node " + result.getAddress().toString() + ", port " + result.getPort()
                            + ", position " + Chord.Helper.hexIdAndPosition(result));

                    targetAddress = Chord.Helper.createSocketAddress(result.getAddress().toString() + ":" + result.getPort());

                    System.out.println("Getting key: " + key + " from node: " + targetAddress.getAddress().toString()
                            + " port: " + targetAddress.getPort());
                    String res = getTempHashMap(key);
                    System.out.println("\n" + res + "\n" ) ;
                } else {
                    System.out.println("Invalid command. Please try again.");
                }

            }

        } else {
            System.out.println("Invalid number of arguments. Now exit.");
            System.exit(0);
        }
    }
     */

    public String getTempHashMap(String key) {
        // get from local storage
        if (storage == null) {
            storage = new HashMap<String, String>();
        }

        // check if key exists
        if (!storage.containsKey(key)) {
            return "key: " + key + " does not exist in Chord.Chord";
        }

        // get value
        String value = storage.get(key);

        return value;
    }

    private String getDynamoDB(String key, String address, int port) {
        if (dbStorage == null) {
            dbStorage = new DynamoDBWrapper();
        }

        String value = dbStorage.getHash(address, port, key);

        return value;
    }

    public String insertTempHashMap(String key, String value) {
        // insert into local storage
        if (storage == null) {
            storage = new HashMap<String, String>();
        }

        // add key and value to storage
        storage.put(key, value);

        return "successfully added key: " + key + " value: " + value + " to Chord.Chord";
    }

    private String insertDynamoDB(String key, String value, String address, int port) {
        if (dbStorage == null) {
            dbStorage = new DynamoDBWrapper();
        }

        dbStorage.putHash(address, port, key, value);

        return "successfully added key: " + key + " value: " + value + " to Chord Node " + address+":"+port;
    }

    private void joinChordNetwork(String ip, String port) {
        localAddress = Helper.createSocketAddress(ip + ":" + port);
        if (localAddress == null) {
            System.out.println("Cannot find address you are trying to contact. Now exit.");
            System.exit(0);
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
    }

    public String connectToNode(String ip, String port) {
        helper = new Helper();
        localAddress = Helper.createSocketAddress(ip + ":" + port);
        if (localAddress == null) {
            System.out.println("Cannot find address you are trying to contact. Now exit.");
            System.exit(0);
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

            // system is stable
            return "System is stable. You are now connected to node " + localAddress.getAddress().toString() + ", port "
                    + localAddress.getPort() + ", position " + Helper.hexIdAndPosition(localAddress) + ".";
    }
    public void insert(String key, String value) {
        // search key location and print out response
        long hash = Helper.hashString(key);
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
        System.out.println("Target Chord.Node " + result.getAddress().toString() + ", port " + result.getPort()
                + ", position " + Helper.hexIdAndPosition(result));

        targetAddress = Helper.createSocketAddress(result.getAddress().toString() + ":" + result.getPort());

        System.out.println("Inserting key: " + key + " value: " + value + " to node: "
                + targetAddress.getAddress().toString() + " port: " + targetAddress.getPort());

        // String res = insertTempHashMap(key, value);
        String res = insertDynamoDB(key, value, targetAddress.getAddress().toString(), targetAddress.getPort());
        System.out.println(res);
    }

    public String get(String key) {

        // search key location and print out response
        long hash = Helper.hashString(key);
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
        System.out.println("Target Chord.Node " + result.getAddress().toString() + ", port " + result.getPort()
                + ", position " + Helper.hexIdAndPosition(result));

        targetAddress = Helper.createSocketAddress(result.getAddress().toString() + ":" + result.getPort());

        System.out.println("Getting key: " + key + " from node: " + targetAddress.getAddress().toString()
                + " port: " + targetAddress.getPort());
        String res = getDynamoDB(key, targetAddress.getAddress().toString(), targetAddress.getPort());
        System.out.println("\n" + res + "\n" ) ;
        return res;
    }
}
