package org.example;

import java.util.List;

public class Mentor {

    private String name;
    private int age;
    private boolean married;
    private double salary;
    private List<Intern> interns;
    private List<String> skills;

    @Override
    public String toString() {

        return "Mentor [name=" + name + ", age=" + age + ", married=" + married + ", salary=" + salary + ", intern=" + interns + ", skills=" + skills + "]";
    }
}
