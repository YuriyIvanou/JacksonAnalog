package org.example;

import org.example.node.ArrayNode;
import org.example.node.JsonNode;
import org.example.node.ObjectNode;
import org.example.node.TextNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class MyObjectMapper {


    /**
     * Этот метод берет данные в формате JSON (строку, файл или поток) и превращает их в объект Java нужного вам класса
     * десериализации (JSON в объекты Java)
     * @param jsonNode Структура «ключ — значение»: Данные организуются в виде пар,
     *                 где ключом является строка, а значением может быть число, строка, массив или другой объект.
     * @param clazz
     * @return объект Java (POJO)
     */


    public <T> T treeToValue(JsonNode jsonNode, Class<T> clazz)   {

        System.out.println("start treeToValue");
        try {
            return initializeObject(jsonNode, createObjectOfClass(clazz));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }


    }

    private <T> T createObjectOfClass(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        System.out.println("start createObjectOfClass");

        Constructor<T> constructor = clazz.getDeclaredConstructor();//узнать поля конструктора string int

        T t = constructor.newInstance();
        return t;
    }

    private <T> T initializeObject(JsonNode jsonNode, T emptyObject) throws IllegalAccessException {

        System.out.println("start initializeObject");

        return switch (jsonNode) {

            case ObjectNode objectNode -> mapObjectNode(objectNode, emptyObject);

            default -> throw new IllegalStateException("Unexpected value: " + jsonNode);
        };

    }

    private <T> T mapObjectNode(ObjectNode objectNode, T emptyObject) throws IllegalAccessException {

        System.out.println("Start mapObjectNode");

        Class<?> clazz = emptyObject.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {

            field.setAccessible(true);
            String fieldName = field.getName();
            JsonNode valueOfNode = objectNode.get(fieldName);
            Object value = convertNodeToValue(valueOfNode, field.getType());
            field.set(emptyObject, value);
        }
        return emptyObject;

    }

    private Object convertNodeToValue(JsonNode valueOfNode, Class<?> fieldType) {
        System.out.println("start convertNodeToValue");

        if (valueOfNode instanceof ObjectNode){
            return treeToValue(valueOfNode, fieldType );
        }

        // TODO: Здесь нужно добавить обработку ArrayNode -> List<T>
        /**
         * Чтобы реализовать обработку ArrayNode и превратить его в List, нужно решить главную проблему:
         * Java должна знать, объекты какого типа лежат внутри списка (например, List<String> или List<User>).
         * Для этого в метод convertNodeToValue лучше передавать не просто класс, а весь объект Field, чтобы вытащить информацию о Generics.
         *
         * */

        return switch (fieldType.getSimpleName()) {
            case "String" -> valueOfNode.asText();
            case "int", "Integer" -> valueOfNode.asInt();
            case "boolean", "Boolean" -> valueOfNode.asBoolean();
            case "double", "Double" -> valueOfNode.asDouble();


            default -> throw new IllegalStateException("Unexpected value: " + fieldType);
        };
    }

    private void ArrayObjectNode(ArrayNode arrayNode) {

    }

    private void TextNode(TextNode textNode) {

    }


}
