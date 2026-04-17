package org.example;

import org.example.node.ArrayNode;
import org.example.node.JsonNode;
import org.example.node.ObjectNode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AnalogObjectMapper {

    /**
     * Публичный метод берет данные в формате JsonNode и превращает их в объект Java нужного вам класса
     * десериализации (JSON в объекты Java)
     *
     * @param jsonNode Структура «ключ — значение»: Данные организуются в виде пар,
     *                 где ключом является строка, а значением может быть число, строка, массив или другой объект.
     * @param clazz    класс запрашиваемого объект Java (POJO)
     * @return объект Java (POJO)
     */

    public <T> T treeToValue(JsonNode jsonNode, Class<T> clazz) {

        try {
            return initializeObject(jsonNode, createObjectOfClass(clazz));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T createObjectOfClass(Class<T> clazz) throws RuntimeException {

        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T initializeObject(JsonNode jsonNode, T emptyObject) throws IllegalAccessException {

        return mapObjectNode((ObjectNode) jsonNode, emptyObject);
    }

    private <T> T mapObjectNode(ObjectNode objectNode, T emptyObject) throws IllegalAccessException {

        System.out.println("Start mapObjectNode");

        Class<?> clazz = emptyObject.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field fieldOfEmptyObject : declaredFields) {

            fieldOfEmptyObject.setAccessible(true);
            String fieldName = fieldOfEmptyObject.getName();
            JsonNode valueOfNode = objectNode.get(fieldName);
            Object value = convertNodeToValue(valueOfNode, fieldOfEmptyObject);
            fieldOfEmptyObject.set(emptyObject, value);
        }
        return emptyObject;
    }

    private Object convertNodeToValue(JsonNode valueOfNode, Field field) {

        return switch (valueOfNode) {
            case null -> null;
            case ObjectNode objectNode -> treeToValue(valueOfNode, field.getType());
            case ArrayNode arrayNode -> mapArrayNodeToList(arrayNode, field);
            default -> convertSimpleNode(valueOfNode, field.getType());
        };
    }

    private List<Object> mapArrayNodeToList(ArrayNode arrayNode, Field field) {

        List<Object> list = new ArrayList<>();
        //Извлекаем List<Intern>
        //Если поле имеет дженерик-тип (напр., Map<String, Integer>), метод вернет ParameterizedType
        //Помогает обойти стирание типов (type erasure) для сигнатуры поля
        Type genericType = field.getGenericType();

        if (genericType instanceof ParameterizedType pt) {
            // Извлекаем класс (например, User.class из List<User>)
            Class<?> listElementClass = (Class<?>) pt.getActualTypeArguments()[0];

            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode elementNode = arrayNode.get(i);

                if (elementNode instanceof ObjectNode) {
                    list.add(treeToValue(elementNode, listElementClass));
                }
                // TODO добавить List в List

                /*  if (elementNode instanceof ArrayNode) {
                    list.add(mapArrayNodeToList((ArrayNode) elementNode, field));
                }*/

                else {
                    list.add(convertSimpleNode(elementNode, listElementClass));
                }
            }
        }
        return list;
    }

    private Object convertSimpleNode(JsonNode node, Class<?> clazz) {

        return switch (clazz.getSimpleName()) {
            case "String" -> node.asText();
            case "Integer", "int" -> node.asInt();
            case "Boolean", "boolean" -> node.asBoolean();
            case "Double", "double" -> node.asDouble();
            case "Long", "long" -> node.asLong();
            default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
        };
    }
}
