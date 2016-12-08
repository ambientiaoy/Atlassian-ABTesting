package fi.ambientia.atlassian.users;

import java.io.Serializable;

public interface CurrentUser<T extends Serializable> {
    public T getIdentifier();
}
