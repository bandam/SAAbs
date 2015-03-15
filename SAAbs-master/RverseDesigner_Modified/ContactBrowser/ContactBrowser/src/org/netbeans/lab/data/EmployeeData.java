
package org.netbeans.lab.data;

import java.util.Vector;

public class EmployeeData {

    private static Vector data;

    public static Vector getData() {
        if (data == null) {
            data = new Vector();
            data.addElement(new MyEmployee("John", "Smith", false, 42));
            data.addElement(new MyEmployee("Ana", "Kovalsky", true, 24));
            data.addElement(new MyEmployee("Alberto", "Rodrigez", true, 24));
            data.addElement(new MyEmployee("Fabiola", "Stevenson", true, 24));
        }

        return data;
    }

    private static class MyEmployee implements Employee {

        private String firstName;
        private String lastName;
        private boolean gender;
        private int age;

        public MyEmployee(String firstName, String lastName, boolean gender, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.age = age;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public boolean  getGender() {
            return gender;
        }

        public int getAge() {
            return age;
        }

    }
}
