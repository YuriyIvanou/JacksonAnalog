package org.example;


public class Main {
    public static void main(String[] args)  {

        String rawJson = "{ \"name\": \"Nikita\", \"age\": 28 ,\"married\": false, \"salary\": 4.500, \"intern\":{ \"name\": \"Yury\"} }";

        MyJackson myJackson = new MyJackson();

        Mentor mentor = myJackson.jsonToObject(rawJson, Mentor.class);

        System.out.println(mentor);

    }
}



