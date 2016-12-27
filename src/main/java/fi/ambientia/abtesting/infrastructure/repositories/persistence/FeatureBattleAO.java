package fi.ambientia.abtesting.infrastructure.repositories.persistence;

import net.java.ao.Entity;

public interface FeatureBattleAO extends Entity{
    void setFeatureBattleId(String identifier);
    String getFeatureBattleId();

    void setThreshold(int threshold);
    int getThreshold();
}
