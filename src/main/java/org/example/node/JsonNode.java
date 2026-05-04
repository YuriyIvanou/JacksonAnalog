package org.example.node;

/**
 * Базовый абстрактный класс для всех узлов
 * Методы для навигации, возвращающие null или пустые значения по умолчанию
 */

public abstract class JsonNode {


    public JsonNode get(String field) {
        return null;
    }

    public JsonNode get(int index) {
        return null;
    }

    public String asText() {
        return "";
    }

    public int asInt() {
        return 0;
    }

    public boolean asBoolean() {
        return false;
    }

    public double asDouble() {
        return 0;
    }

    public long asLong() {
        return 0L;
    }
}
