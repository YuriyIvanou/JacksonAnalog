package org.example;

import org.example.node.ArrayNode;
import org.example.node.JsonNode;
import org.example.node.ObjectNode;
import org.example.node.TextNode;


/**
 * @author Yury
 * создан с помощью ИИ
 * предназначен для сериализации (преобразования объектов Java в JSON) и десериализации (JSON в объекты Java).
 *
 * создает объект типа JsonNode
 * Парсер использует рекурсивный спуск для обхода строки и построения дерева узлов.
 * Рекурсивный обход: Процесс идет «сверху вниз». Парсер встречает открывающую скобку {,
 * создает ObjectNode и начинает рекурсивно обрабатывать вложенные пары «ключ-значение», пока не закроет объект.
 *
 * */

public class JsonTreeParser {
    private int pos = 0;
    private String json;

    public JsonNode parse(String input) {
        this.json = input.trim();
        this.pos = 0;
        return parseValue();

    }

    private JsonNode parseValue() {
        skipWhitespace();
        char c = json.charAt(pos);
        if (c == '{') return parseObject();
        if (c == '[') return parseArray();
        if (c == '"') return new TextNode(parseString());
        return parseLiteral(); // Числа, true, false, null
    }

    private ObjectNode parseObject() {
        ObjectNode node = new ObjectNode();
        pos++; // Пропускаем '{'
        while (json.charAt(pos) != '}') {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            pos++; // Пропускаем ':'
            node.put(key, parseValue());
            skipWhitespace();
            if (json.charAt(pos) == ',') pos++;
        }
        pos++; // Пропускаем '}'
        return node;
    }

    private ArrayNode parseArray() {
        ArrayNode node = new ArrayNode();
        pos++; // Пропускаем '['
        while (json.charAt(pos) != ']') {
            node.add(parseValue());
            skipWhitespace();
            if (json.charAt(pos) == ',') pos++;
        }
        pos++; // Пропускаем ']'
        return node;
    }

    private String parseString() {
        skipWhitespace();
        pos++; // '"'
        int start = pos;
        while (json.charAt(pos) != '"') pos++;
        String result = json.substring(start, pos);
        pos++; // '"'
        return result;
    }

    private TextNode parseLiteral() {
        int start = pos;
        //«Правда ли, что текущий символ — это не запятая, не закрывающая скобка и не пробел?»
        while (pos < json.length() && ",}] ".indexOf(json.charAt(pos)) == -1) pos++;
        return new TextNode(json.substring(start, pos));
    }

    private void skipWhitespace() {
        while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) pos++;
    }
}

