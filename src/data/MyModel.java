package data;

import java.util.Objects;

public class MyModel {

    private final String firstName, lastName;
    private final int age;
    private final boolean isMember;

    private MyModel(String firstName,
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var model = (MyModel) o;
        return age == model.age &&
                isMember == model.isMember &&
                Objects.equals(firstName, model.firstName) &&
                Objects.equals(lastName, model.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age, isMember);
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

    public static class Builder {
        private String firstName;
        private String lastName;
        private int age;
        private boolean isMember;

        public Builder() {
            firstName = "";
            lastName = "";
            age = 0;
            isMember = false;
        }

        public Builder basedOn(MyModel model) {
            firstName = model.firstName;
            lastName = model.lastName;
            age = model.age;
            isMember = model.isMember;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setMember(boolean member) {
            isMember = member;
            return this;
        }

        public MyModel build() {
            return new MyModel(
                    firstName,
                    lastName,
                    age,
                    isMember
            );
        }
    }
}
