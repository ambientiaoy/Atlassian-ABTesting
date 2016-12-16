package fi.ambientia.atlassian.users;

import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;

import java.util.Optional;
import java.util.function.Supplier;

public interface Users {
    public static final String ANONYMOUS_USER = "ANONYMOUS_USER";

    public static Supplier<String> getCurrentUserKey(UserManager userManager) {
        return () -> Optional.ofNullable(userManager.getRemoteUserKey()).orElse( new UserKey(ANONYMOUS_USER)).getStringValue();
    }
}
