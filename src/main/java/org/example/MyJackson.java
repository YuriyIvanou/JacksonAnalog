package org.example;

import org.example.node.JsonNode;

import java.lang.reflect.InvocationTargetException;

public class MyJackson {


    public <T> T jsonToObject(String json, Class<T> clazz)  {

        JsonTreeParser jtp = new JsonTreeParser();
        JsonNode root = jtp.parse(json);
        MyObjectMapper myObjectMapper = new MyObjectMapper();

        return myObjectMapper.treeToValue(root, clazz);

    }
}
