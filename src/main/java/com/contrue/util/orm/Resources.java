package com.contrue.util.orm;

import java.io.InputStream;

/**
 * @author confff
 */
public class Resources {

    /**
     * 从xml文件获得输入流
     * @param path xml配置文件
     * @return 文件
     */
    public static InputStream getResourceAsStream(String path) {
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
