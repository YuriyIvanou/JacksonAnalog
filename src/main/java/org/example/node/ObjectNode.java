package org.example.node;

import java.util.HashMap;
import java.util.Map;

/**
 * Представляет JSON-объект { }. Он изменяемый, в него можно добавлять или удалять поля.
 * Узел для JSON-объектов { "key": "value" }
 */

public class ObjectNode extends JsonNode {

    private final Map<String, JsonNode> fields = new HashMap<>();

    public void put(String key, JsonNode value) {
        fields.put(key, value);
    }

    @Override
    public JsonNode get(String field) {
        return fields.get(field);
    }
}
