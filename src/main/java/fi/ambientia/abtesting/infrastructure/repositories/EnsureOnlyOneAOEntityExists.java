package fi.ambientia.abtesting.infrastructure.repositories;

import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleResultAO;
import net.java.ao.Entity;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.Optional;

public class EnsureOnlyOneAOEntityExists {
    public static <T extends Entity> Optional<T> execute(SimpleActiveObjects ao, Class<T> klazz, String clause, Object... params) {
        T[] ts = ao.find(klazz, Query.select().where(clause, params));
        if( ts.length > 1){
            Arrays.asList(ts).stream().skip(1).forEach(entity -> ao.delete( entity ));
            ts = ao.find(klazz, Query.select().where(clause, params));
        }
        if( ts.length == 1){
            return Optional.of(ts[0]);
        }
        return Optional.empty();
    }


    public static <T extends Entity> T andCreate(SimpleActiveObjects ao, Class<T> klazz, String clause, Object... params) {
        Optional<T> entity = execute(ao, klazz, clause, params);
        if( entity.isPresent() ){
            return entity.get();
        }
        return ao.create(klazz);
    }
}
