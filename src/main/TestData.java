package main;

import java.util.Arrays;
import java.util.List;

public class TestData {

    /**
     * A simple data holder representing some domain data.
     * Your data model can be fully interactive, or just
     * a pojo.
     */
    public static class Model {

        public String firstName, lastName;
        public int age;
        public boolean isMember;

        public Model() {
            this("", "", 0, false);
        }

        public Model(String firstName,
                     String lastName,
                     int age,
                     boolean isMember) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.isMember = isMember;
        }

        @Override
        public String toString() {
            return "Model{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", age=" + age +
                    ", isMember=" + isMember +
                    '}';
        }
    }

    /**
     * Some data for testing
     */
    public static List<Model> models = Arrays.asList(
            new Model("Ivy", "Brown", 22, true),
            new Model("Tim", "Berners-Lee", 25, false),
            new Model("James", "Gosling", 99, true),
            new Model("Grace", "Hopper", 34, false),
            new Model("Ada", "Lovelace", 20, true),
            new Model("Robert", "Martin", 19, false)
    );
}
