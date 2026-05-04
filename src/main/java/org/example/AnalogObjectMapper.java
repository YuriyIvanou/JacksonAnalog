package org.example;

import org.example.node.ArrayNode;
import org.example.node.JsonNode;
import org.example.node.ObjectNode;
import org.example.node.TextNode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AnalogObjectMapper {

    /**
     * Публичный метод берет данные в формате JsonNode и превращает (десериализирует) их в объект Java нужного класса
     *
     * @param jsonNode Структура «ключ — значение»: Данные организуются в виде пар, где ключом является строка,
     *                 а значением может быть число, строка, массив или другой объект.
     * @param clazz    класс запрашиваемого объект Java (POJO)
     */

    public <T> T jsonNodeToInstance(JsonNode jsonNode, Class<T> clazz) {
        return switch (jsonNode) {
            case ObjectNode objectNode -> convertObjectNode(objectNode, clazz);
            case TextNode textNode -> (T) convertTextNode(textNode, clazz);
            case ArrayNode arrayNode -> (T) convertArrayNode(arrayNode, clazz);
            case null, default ->
                    throw new IllegalStateException("unsupported node type: " + jsonNode.getClass().getSimpleName());
        };
    }

    /**
     * Публичный метод берет данные в формате JsonNode и превращает (десериализирует) их в список объектов Java нужного класса
     *
     * @param jsonNode Структура «ключ — значение»: Данные организуются в виде пар, где ключом является строка,
     *                 а значением может быть число, строка, массив или другой объект.
     * @param clazz    класс запрашиваемого объект Java (POJO)
     */

    public <T> List<T> jsonNodeToList(JsonNode jsonNode, Class<T> clazz) {
        if (jsonNode instanceof ArrayNode arrayNode) {
            return getList(arrayNode, clazz);
        }
        throw new IllegalArgumentException("Unexpected value: " + jsonNode.getClass().getSimpleName());
    }

    private Object convertNodeToValue(JsonNode valueOfNode, Field field) {
        Class<?> type = field.getType();
        return switch (valueOfNode) {
            case ObjectNode objectNode -> jsonNodeToInstance(objectNode, type);
            case ArrayNode arrayNode -> convertArrayNode(arrayNode, field);
            case TextNode textNode -> convertTextNode(textNode, type);
            default -> throw new IllegalStateException("Unexpected value: " + valueOfNode.getClass().getSimpleName());
        };
    }

    private <T> List<T> convertArrayNode(ArrayNode arrayNode, Class<T> clazz) {
        Class<?> elementClass = clazz.componentType();
        return getList(arrayNode, elementClass);
    }

    private Object convertArrayNode(ArrayNode arrayNode, Field field) {
        Type fieldType = field.getGenericType();
        Class<?> fieldClass = field.getType();
        if (fieldType instanceof ParameterizedType pt) {
            Class<?> elementClass = (Class<?>) pt.getActualTypeArguments()[0];
            return getList(arrayNode, elementClass);
        } else {
            Class<?> componentType = fieldClass.getComponentType();
            Object array = java.lang.reflect.Array.newInstance(componentType, arrayNode.size());
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode elementNode = arrayNode.get(i);
                Object value;
                if (elementNode instanceof ObjectNode) {
                    value = jsonNodeToInstance(elementNode, componentType);
                } else {
                    value = convertTextNode(elementNode, componentType);
                }
                java.lang.reflect.Array.set(array, i, value);
            }
            return array;
        }
    }

    private <T> T convertObjectNode(ObjectNode objectNode, Class<T> clazz) {
        T newObject;
        try {
            var constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            newObject = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(("instance creation error " + clazz.getName()));
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field emptyField : declaredFields) {
            emptyField.setAccessible(true);
            String fieldName = emptyField.getName(); //ключ в map
            JsonNode nodeValue = objectNode.get(fieldName);//может быть 3-х типов textNode arrayNode objectNode, значение в map
            if (nodeValue == null) {
                continue;
            }
            Object value = convertNodeToValue(nodeValue, emptyField);
            try {
                emptyField.set(newObject, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return newObject;
    }

    private Object convertTextNode(JsonNode node, Class<?> clazz) {
        return switch (clazz.getSimpleName().toLowerCase()) {
            case "string" -> node.asText();
            case "integer", "int" -> node.asInt();
            case "boolean" -> node.asBoolean();
            case "double" -> node.asDouble();
            case "long" -> node.asLong();
            default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
        };
    }

    private <T> List<T> getList(ArrayNode arrayNode, Class<?> elementClass) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode elementNode = arrayNode.get(i);
            if (elementNode instanceof ObjectNode) {
                //noinspection unchecked
                list.add((T) jsonNodeToInstance(elementNode, elementClass));
            } else {
                //noinspection unchecked
                list.add((T) convertTextNode(elementNode, elementClass));
            }
        }
        return list;
    }
}
