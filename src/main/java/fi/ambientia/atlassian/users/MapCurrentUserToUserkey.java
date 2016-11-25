package fi.ambientia.atlassian.users;

import java.io.Serializable;

public interface MapCurrentUserToUserkey<T extends Serializable> {
    public String getCurrentUserKey() ;

    public T getCurrentUserIdentifier();
}
