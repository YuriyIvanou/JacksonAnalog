package org.example;

import org.example.node.JsonNode;

public class MyJackson {


    public <T> T jsonToObject(String json, Class<T> clazz)  {

        JsonTreeParser jtp = new JsonTreeParser();
        JsonNode root = jtp.parse(json);
        AnalogObjectMapper myObjectMapper = new AnalogObjectMapper();

        return myObjectMapper.treeToValue(root, clazz);

    }
}
