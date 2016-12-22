package fi.ambientia.abtesting.model.user;

import fi.ambientia.abtesting.model.Identifier;

public class UserIdentifier implements Identifier{

    private final String userKey;

    public UserIdentifier(String userKey) {
        this.userKey = userKey;
    }


    @Override
    public String getIdentifier() {
        return userKey;
    }
}
