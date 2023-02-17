package run.antleg.sharp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import run.antleg.sharp.config.jackson.SharpModule;

public class JSON {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static  {
        OBJECT_MAPPER.registerModules(new SharpModule(), new JavaTimeModule());
    }


}
