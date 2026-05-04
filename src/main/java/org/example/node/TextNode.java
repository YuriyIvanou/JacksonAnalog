package org.example.node;


/**
 * Узел для примитивных значений (строки, числа, булевы значения)
 */

public class TextNode extends JsonNode {

    private final String value;

    public TextNode(Object value) {
        this.value = String.valueOf(value);
    }

    @Override
    public String asText() {
        return value;
    }

    @Override
    public int asInt() {
        return Integer.parseInt(value);
    }

    @Override
    public boolean asBoolean() {
        return Boolean.parseBoolean(value);
    }

    @Override
    public double asDouble() {
        return Double.parseDouble(value);
    }

    @Override
    public long asLong() {
        return Long.parseLong(value);
    }
}
