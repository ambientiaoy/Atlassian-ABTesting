package fi.ambientia.atlassian.users;

import java.io.Serializable;

public interface MapCurrentUserToUserkey<T extends Serializable> {
    public T getCurrentUserIdentifier();
}
