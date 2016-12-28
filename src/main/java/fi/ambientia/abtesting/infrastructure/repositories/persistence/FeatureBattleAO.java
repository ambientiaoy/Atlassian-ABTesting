package fi.ambientia.abtesting.infrastructure.repositories.persistence;

import net.java.ao.Entity;

public interface FeatureBattleAO extends Entity{
    void setFeatureBattleId(String identifier);
    String getFeatureBattleId();

    void setThreshold(int threshold);
    int getThreshold();

    void setGoodOld(String oldPage);
    String getGoodOld();

    void setNewAndShiny(String newPage);
    String getNewAndShiny();
}
