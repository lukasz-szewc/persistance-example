package org.luksze.config;

import java.util.HashMap;

public class Configuration extends HashMap<Object, String> {
    public Configuration() {
        put("javax.persistence.jdbc.url", "jdbc:h2:mem:cascade-test");
        put("javax.persistence.jdbc.driver", "org.h2.Driver");
        put("javax.persistence.jdbc.user", "sa");
        put("javax.persistence.jdbc.password", "");
        put("hibernate.hbm2ddl.auto", "create-drop");
        put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    }
}
