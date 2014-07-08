package co.digitaloracle;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by liorsaar on 3/27/14.
 */
public class Util {
    public static String getResourceString(String filename) throws FileNotFoundException {
        String rootPath = "./Oracle/src/test/resources/co/digitaloracle/";
        String string = new Scanner(new File(rootPath + filename)).useDelimiter("\\Z").next();
        return string;
    }

    public static Object getJson( String filename, Class classs ) throws Exception {
        String jsonString = Util.getResourceString(filename);
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(jsonString, classs);
    }
}
