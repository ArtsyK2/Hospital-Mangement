package hms;

import java.io.Serializable;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private final String name;
    private final String specialization;
    private final String phone;

    public Doctor(int id, String name, String specialization, String phone) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getPhone() { return phone; }
}
