package data;

import java.util.Arrays;
import java.util.List;

public class TestData {

    public static List<MyModel> myModels = Arrays.asList(

            new MyModel.Builder()
                    .setFirstName("Ivy")
                    .setLastName("Brown")
                    .setAge(22)
                    .setMember(true)
                    .build(),

            new MyModel.Builder()
                    .setFirstName("Tim")
                    .setLastName("Berners-Lee")
                    .setAge(25)
                    .setMember(false)
                    .build(),

            new MyModel.Builder()
                    .setFirstName("James")
                    .setLastName("Gosling")
                                    .setAge(99)
                    .setMember(true)
                    .build(),

            new MyModel.Builder()
                    .setFirstName("Grace")
                    .setLastName("Hopper")
                    .setAge(34)
                    .setMember(false)
                    .build(),

            new MyModel.Builder()
                    .setFirstName("Ada")
                    .setLastName("Lovelace")
                    .setAge(20)
                    .setMember(true)
                    .build(),

            new MyModel.Builder()
                    .setFirstName("Robert")
                    .setLastName("Martin")
                    .setAge(19)
                    .setMember(false)
                    .build()
    );
}