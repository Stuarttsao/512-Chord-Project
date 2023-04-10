package DynamoDB;

import AWSCreds.DynamoDBCreds;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.HashMap;
import java.util.Map;

public class DynamoDBWrapper {
    //This is located in an enum that is not on git for security purposes
    private final BasicAWSCredentials AWS_CREDS = DynamoDBCreds.MY_CREDS.awsCred;
    private final String TABLE_NAME = "CS512-Project";
    private AmazonDynamoDB ddb;

    public DynamoDBWrapper() {
        this.ddb = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(AWS_CREDS))
                .build();
    }


    private String buildKey(String address, int port, String user) {
        StringBuilder key = new StringBuilder();
        key.append("Chord.Node#");
        key.append(address);
        key.append(":");
        key.append(port);
        key.append("-");
        key.append(user);
        return key.toString();
    }

    public void putHash(String address, int port, String user, String hash) {
        Map<String, AttributeValue> item_values = new HashMap<>();

        String key = buildKey(address, port, user);

        item_values.put("ID", new AttributeValue(key));
        item_values.put("Value", new AttributeValue(hash));

        try {
            ddb.putItem(TABLE_NAME, item_values);
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The table \"%s\" can't be found.\n", TABLE_NAME);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (AmazonServiceException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    /**
     * Finds hash associated with given parameters
     * @param user user inforamtion associated with the hash
     * @return Returns either a String of the hash or an empty String if the hash could not be found
     */
    public String getHash(String address, int port, String user) {
        String keyString = buildKey(address, port, user);

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("ID", new AttributeValue(keyString));

        GetItemRequest request = new GetItemRequest()
                                        .withKey(key)
                                        .withTableName(TABLE_NAME);

        String hashVal = new String();

        try {
            Map<String,AttributeValue> returned_item =
                    ddb.getItem(request).getItem();
            if (returned_item != null) {
                hashVal = returned_item.get("Value").getS();
            } else {
                System.out.format("No item found with the key %s!\n", keyString);
                hashVal = "";
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        return hashVal;
    }

    public static void main (String[] args) {
        DynamoDBWrapper test = new DynamoDBWrapper();
        System.out.println("Connected");
        // test.putHash(1, DynamoDataTypeEnum.HASH, "test_user", "1", "test_hash");
        // System.out.println("Got hash " + test.getHash(1, DynamoDataTypeEnum.HASH, "test_user", "1"));
    }
}
