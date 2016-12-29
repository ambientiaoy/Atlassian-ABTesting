package fi.ambientia.abtesting.infrastructure;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import net.java.ao.Entity;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class WrappingActiveObjects implements SimpleActiveObjects {
    @ComponentImport
    private final ActiveObjects ao;

    @Autowired
    public WrappingActiveObjects(ActiveObjects ao) {
        this.ao = ao;
    }

    @Override
    public <T extends Entity> T[] find(Class<T> klazz, Query query) {
        return ao.find(klazz, query);
    }

    @Override
    public <T extends Entity> void delete(T entity) {
        ao.delete(entity);
    }

    @Override
    public <T extends Entity> T create(Class<T> klazz) {
        return ao.create(klazz);
    }

    @Override
    public <T extends Entity> T[] find(Class<T> klazz) {
        return ao.find(klazz);
    }

    @Override
    public <T> T withinTransaction(Supplier<T> supplier) {
        return supplier.get();
    }
}
