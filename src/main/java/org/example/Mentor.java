package org.example;


public class Mentor {

    private String name;
    private int age;
    private boolean married;
    private double salary;
    private Intern intern;
    //private String[] skills;

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public Mentor(String name, int age, boolean married) {
        this.name = name;
        this.age = age;
        this.married = married;
    }

    public Mentor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {

        return "Mentor [name=" + name + ", age=" + age + ", married=" + married  + ", salary=" + salary+ ", intern=" + intern + "]";
    }
}
