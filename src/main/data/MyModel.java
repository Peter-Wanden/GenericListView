package main.data;

public class MyModel {

    private final String firstName, lastName;
    private final int age;
    private final boolean isMember;

    public MyModel() {
        this("", "", 0, false);
    }

    public MyModel(String firstName,
                   String lastName,
                   int age,
                   boolean isMember) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.isMember = isMember;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public boolean isMember() {
        return isMember;
    }

    @Override
    public String toString() {
        return "MyModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", isMember=" + isMember +
                '}';
    }
}
