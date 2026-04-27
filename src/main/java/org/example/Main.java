package org.example;

public class Main {
    public static void main(String[] args) {
        String json1 = "{ \"name\": \"Nikita\", \"age\": 28, \"married\": false, \"salary\": 4500, \"skills\": [\"Java\", \"Spring\", \"Hibernate\"], \"interns\": [{ \"name\": \"Yury\" }, { \"name\": \"Oleg\" }] }";
        String json3 = "\"Yuriy\"";
        String jsonArray = "[\"Yury\", \"Oleg\"]";
        String arrayObjects = "[{ \"name\": \"Yury\" }, { \"name\": \"Oleg\" }] ";
        String value = "2";

        MyJackson myJackson = new MyJackson();
        System.out.println(myJackson.jsonToObject(json3, String.class));
        System.out.println(myJackson.jsonToObject(json1, Mentor.class));
        System.out.println(myJackson.jsonToObject(value, Integer.class));
        System.out.println(myJackson.jsonToObject(jsonArray, String[].class));
        System.out.println(myJackson.jsonToObject(arrayObjects, Intern[].class));
        System.out.println(myJackson.jsonToList(jsonArray, String.class));
        System.out.println(myJackson.jsonToList(jsonArray, String.class).getClass());
    }
}