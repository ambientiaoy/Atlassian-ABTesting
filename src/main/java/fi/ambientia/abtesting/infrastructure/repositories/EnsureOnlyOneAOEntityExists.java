package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.Entity;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.Optional;

public class EnsureOnlyOneAOEntityExists {
    public static <T extends Entity> Optional<T> execute(ActiveObjects ao, Class<T> klazz, String clause, Object... params) {
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
}
