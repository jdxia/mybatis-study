package org.example.io;

import java.io.InputStream;

public class Resources {

    /**
     * 给个路径,返回对应的流
     * @param path
     * @return InputStream
     */
    public static InputStream getResourceAsSteam(String path) {
        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);
        return resourceAsStream;
    }
}
