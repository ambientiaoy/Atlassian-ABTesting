package fi.ambientia.abtesting.model.user;

import fi.ambientia.abtesting.model.Identifier;

public class UserIdentifier implements Identifier{

    private final String userKey;

    public UserIdentifier(String userKey) {
        this.userKey = userKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserIdentifier)) return false;

        UserIdentifier that = (UserIdentifier) o;

        return userKey != null ? userKey.equals(that.userKey) : that.userKey == null;
    }

    @Override
    public int hashCode() {
        return userKey != null ? userKey.hashCode() : 0;
    }

    @Override
    public String getIdentifier() {
        return userKey;
    }
}
