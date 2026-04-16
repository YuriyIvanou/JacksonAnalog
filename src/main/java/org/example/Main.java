package org.example;

public class Main {
    public static void main(String[] args) {


        String json2 = "{ \"name\": \"Nikita\", \"age\": 28, \"married\": false, \"salary\": 4500, \"skills\": [\"Java\", \"Spring\", \"Hibernate\"], \"intern\": [{ \"name\": \"Yury\" }, { \"name\": \"Oleg\" }] }";


        MyJackson myJackson = new MyJackson();

        Mentor mentor = myJackson.jsonToObject(json2, Mentor.class);

        System.out.println(mentor);

    }
}