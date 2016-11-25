package fi.ambientia.abtesting.model;

public class ABTestInstance {
    private String uniqueKey;

    public ABTestInstance(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }
}
