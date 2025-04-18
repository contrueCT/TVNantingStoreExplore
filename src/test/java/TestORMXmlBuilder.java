import com.contrue.Framework.orm.configuration.Configuration;
import com.contrue.Framework.orm.Resources;
import com.contrue.Framework.orm.configuration.XmlConfigBuilder;
import com.contrue.Framework.orm.configuration.XmlMapperBuilder;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class TestORMXmlBuilder {

    @Test
    public void TestResource(){
        Configuration config = new Configuration();
        InputStream in = Resources.getResourceAsStream("mapper/UserMapper.xml");
        System.out.println(in);
        XmlMapperBuilder builder = new XmlMapperBuilder(config);
        try {
            builder.parse(in);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void TestXmlBuilder() throws DocumentException {
        Configuration config = new Configuration();
        InputStream in = Resources.getResourceAsStream("batis-config.xml");
        XmlConfigBuilder builder = new XmlConfigBuilder(config);
        builder.parseConfig(in);
        System.out.println(config);
    }
}
