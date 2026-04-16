package org.example.node;

import java.util.ArrayList;
import java.util.List;

/**
 * Узел для JSON-массивов [ ... ]
 */

public class ArrayNode extends JsonNode  {

    private final List<JsonNode> elements = new ArrayList<>();

    public void add(JsonNode node) {
        elements.add(node);
    }

    @Override
    public JsonNode get(int index) {
        return elements.get(index);
    }

    public int size() {
        return elements.size();
    }
}
