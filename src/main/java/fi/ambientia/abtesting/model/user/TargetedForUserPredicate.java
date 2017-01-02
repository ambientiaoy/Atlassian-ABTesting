package fi.ambientia.abtesting.model.user;

import fi.ambientia.abtesting.model.Identifier;

public interface TargetedForUserPredicate {
    boolean forUser(Identifier user);
}
