package hms;

import java.io.Serializable;

public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private final String name;
    private final int age;
    private final String gender;
    private final String phone;
    private final String diagnosis;

    public Patient(int id, String name, int age, String gender, String phone, String diagnosis) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.diagnosis = diagnosis;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getDiagnosis() { return diagnosis; }
}
