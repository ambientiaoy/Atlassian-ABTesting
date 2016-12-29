package fi.ambientia.abtesting.infrastructure.activeobjects;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.transaction.TransactionCallback;
import fi.ambientia.abtesting.infrastructure.WrappingActiveObjects;
import net.java.ao.Entity;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("TransactionalActiveObject")
public class TransactionalActiveObjects implements SimpleActiveObjects{

    @ComponentImport
    private final ActiveObjects ao;
    private final WrappingActiveObjects wrappingActiveObjects;

    @Autowired
    public TransactionalActiveObjects(ActiveObjects ao, WrappingActiveObjects wrappingActiveObjects) {
        this.ao = ao;
        this.wrappingActiveObjects = wrappingActiveObjects;
    }

    @Override
    public <T extends Entity> T[] find(Class<T> klazz, Query where) {
        return ao.executeInTransaction(new TransactionCallback<T[]>() {
            @Override
            public T[] doInTransaction() {
                return wrappingActiveObjects.find(klazz, where);
            }
        });
    }

    @Override
    public <T extends Entity> void delete(T entity) {
        ao.executeInTransaction(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction() {
                wrappingActiveObjects.delete(entity);
                return null;
            }
        });
    }

    @Override
    public <T extends Entity> T create(Class<T> klazz) {
        return wrappingActiveObjects.create(klazz);
    }

    @Override
    public <T extends Entity> T[] find(Class<T> klazz) {
        return ao.executeInTransaction(new TransactionCallback<T[]>() {
            @Override
            public T[] doInTransaction() {
                return wrappingActiveObjects.find(klazz);
            }
        });
    }
}
