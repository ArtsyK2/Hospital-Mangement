package hms;

import java.io.Serializable;

public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private final int patientId;
    private final int doctorId;
    private final String date;
    private final String time;
    private final String reason;

    public Appointment(int id, int patientId, int doctorId, String date, String time, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.reason = reason;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getReason() { return reason; }
}
