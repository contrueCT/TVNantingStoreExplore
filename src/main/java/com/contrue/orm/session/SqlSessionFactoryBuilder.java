package com.contrue.orm.session;

import com.contrue.orm.configuration.Configuration;
import com.contrue.orm.configuration.XmlConfigBuilder;
import org.dom4j.DocumentException;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public static SqlSessionFactory build(InputStream inputStream) throws DocumentException {
        Configuration configuration = new Configuration();
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(configuration);
        configuration = xmlConfigBuilder.parseConfig(inputStream);
        return new DefaultSqlSessionFactory(configuration);
    }

}
