package org.example;

import org.example.node.JsonNode;
import java.util.List;

public class MyJackson {
    /**
     * Публичный метод берет данные в формате Json и превращает (десериализируя) их в объект Java нужного вам класса
     */
    public <T> T jsonToObject(String json, Class<T> clazz) {

        JsonParser jtp = new JsonParser();
        JsonNode jsonNode = jtp.parse(json);
        AnalogObjectMapper myObjectMapper = new AnalogObjectMapper();
        return myObjectMapper.jsonNodeToInstance(jsonNode, clazz);
    }

    /**
     * Публичный метод берет данные в формате Json и превращает (десериализируя) их в список объектов Java нужного вам класса
     */

    public <T> List<T> jsonToList(String json, Class<T> clazz) {
        JsonParser jtp = new JsonParser();
        JsonNode jsonNode = jtp.parse(json);
        AnalogObjectMapper myObjectMapper = new AnalogObjectMapper();
        return myObjectMapper.jsonNodeToList(jsonNode, clazz);
    }
}
