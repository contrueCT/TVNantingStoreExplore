package com.contrue.util.orm.configuration;

import com.contrue.util.orm.Resources;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XmlConfigBuilder {
    private Configuration config;
    public XmlConfigBuilder(Configuration config) {
        this.config = config;
    }

    public Configuration parseConfig(InputStream in) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(in);
        Element root = document.getRootElement();

        List<Element> elements = root.elements();
        for (Element element : elements) {
            String resource = element.attributeValue("resource");
            InputStream inputStream = Resources.getResourceAsStream(resource);
            XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(config);
            xmlMapperBuilder.parse(inputStream);
        }
        return config;
    }
}
