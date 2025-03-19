package com.contrue.util.orm.configuration;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XmlMapperBuilder {

    private Configuration configuration;
    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        String namespace = root.attributeValue("namespace");
        List<Element> elements = root.elements();
        for (Element element : elements) {
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");
            String sql = element.getTextTrim();
            String openToken = element.attributeValue("openToken");
            String closeToken = element.attributeValue("closeToken");
            String actionType = element.attributeValue("actionType");

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSql(sql);
            mappedStatement.setParameters(parameterType);
            mappedStatement.setOpenToken(openToken);
            mappedStatement.setCloseToken(closeToken);
            mappedStatement.setActionType(actionType);

            configuration.getMappedStatementMap().put(namespace+"."+mappedStatement.getId(), mappedStatement);
        }
    }
}
