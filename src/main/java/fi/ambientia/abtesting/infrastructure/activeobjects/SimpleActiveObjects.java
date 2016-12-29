package fi.ambientia.abtesting.infrastructure.activeobjects;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Entity;
import net.java.ao.Query;

import java.util.function.Supplier;

public interface SimpleActiveObjects{
    <T extends Entity> T[] find(Class<T> klazz, Query where);

    <T extends Entity> void delete(T entity);

    <T extends Entity> T create(Class<T> klazz);

    <T extends Entity> T[] find(Class<T> klazz);

    <T> T withinTransaction(Supplier<T> supplier);
}

