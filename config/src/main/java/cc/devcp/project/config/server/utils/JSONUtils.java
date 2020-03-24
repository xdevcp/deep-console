package cc.devcp.project.config.server.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;


/**
 * json util
 *
 * @author Nacos
 */
@SuppressWarnings("PMD.ClassNamingShouldBeCamelRule")
public class JSONUtils {

    static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static String serializeObject(Object o) throws IOException {
        return mapper.writeValueAsString(o);
    }

    public static Object deserializeObject(String s, Class<?> clazz) throws IOException {
        return mapper.readValue(s, clazz);
    }

    public static <T> T deserializeObject(String s, TypeReference<T> typeReference)
        throws IOException {
        return mapper.readValue(s, typeReference);
    }

    public static <T> T deserializeObject(InputStream src, TypeReference<?> typeReference)
        throws IOException {
        return (T) mapper.readValue(src, typeReference);
    }

}
