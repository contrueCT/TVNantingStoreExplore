package com.contrue.util.JWT;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

/**
 * @author confff
 */
public class KeyLoader {

    private static final String PROPERTIES_FILE = "SecretKey.properties";
    private static final String KEY_PROPERTY = "KEY_PROPERTY";

    public static SecretKey getSecretKey() {
        Properties prop = new Properties();
        try(InputStream in = KeyLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if(in == null){
                throw new FileNotFoundException("配置文件： '" + PROPERTIES_FILE + "' 没有找到");
            }
            prop.load(in);
            String base64EncodedKey = prop.getProperty(KEY_PROPERTY);
            if(base64EncodedKey == null||base64EncodedKey.isEmpty()){
                throw new RuntimeException("配置文件中未找到密钥");
            }
            byte[] decodeKey = Base64.getDecoder().decode(base64EncodedKey);
            return new SecretKeySpec(decodeKey, 0, decodeKey.length, "HmacSHA512");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
