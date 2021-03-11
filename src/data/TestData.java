package data;

import java.util.Arrays;
import java.util.List;

public class TestData {

    public static List<MyModel> myModels = Arrays.asList(
            new MyModel("Ivy", "Brown", 22, true),
            new MyModel("Tim", "Berners-Lee", 25, false),
            new MyModel("James", "Gosling", 99, true),
            new MyModel("Grace", "Hopper", 34, false),
            new MyModel("Ada", "Lovelace", 20, true),
            new MyModel("Robert", "Martin", 19, false)
    );
}