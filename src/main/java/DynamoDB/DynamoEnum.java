package DynamoDB;

enum DynamoDataTypeEnum {
    PASSWORD("password"),
    SALT("salt");

    public final String label;

    private DynamoDataTypeEnum(String label) {
        this.label = label;
    }
}
