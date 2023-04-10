package DynamoDB;

enum DynamoDataTypeEnum {
    HASH("hash"),
    SALT("salt");

    public final String label;

    private DynamoDataTypeEnum(String label) {
        this.label = label;
    }
}
