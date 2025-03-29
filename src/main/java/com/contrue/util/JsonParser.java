package com.contrue.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author confff
 */
public class JsonParser {

    public static <T> T parseJson(HttpServletRequest request, Class<T> clazz) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try(BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        String json = stringBuilder.toString();

        Gson gson = new Gson();
        return gson.fromJson(json,clazz);
    }
}
