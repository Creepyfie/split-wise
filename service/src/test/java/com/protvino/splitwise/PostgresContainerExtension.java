package com.protvino.splitwise;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainerExtension implements Extension, BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static PostgreSQLContainer<?> container;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (container == null) {
            container = new PostgreSQLContainer<>("postgres:13.2");
            container.start();

            System.setProperty("DB_HOST", container.getHost());
            System.setProperty("DB_PORT", String.valueOf(container.getMappedPort(5432)));
            System.setProperty("DB_NAME", String.valueOf(container.getDatabaseName()));
            System.setProperty("DB_USERNAME", String.valueOf(container.getUsername()));
            System.setProperty("DB_PASSWORD", String.valueOf(container.getPassword()));
        }
    }

    @Override
    public void close() {
        container.stop();
    }
}
