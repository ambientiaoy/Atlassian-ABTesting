package fi.ambientia.abtesting.model.experiments;

public class PageObject {
    private final String page;
    private final String spaceKey;

    public PageObject(String spaceKey, String page) {
        this.spaceKey = spaceKey;
        this.page = page;
    }

    public String getPage() {
        return page;
    }


    public String getSpaceKey() {
        return spaceKey;
    }
}
