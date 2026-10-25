package hms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalManagementSystem extends JFrame {
    private static final String DATA_FILE = "hospital-data.ser";
    private DataStore data;
    private JTable patientTable, doctorTable, appointmentTable;

    public HospitalManagementSystem() {
        super("Hospital Management System");
        data = loadData();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel("HOSPITAL MANAGEMENT SYSTEM", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(16, 10, 8, 10));
        add(heading, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", buildPatientsPanel());
        tabs.addTab("Doctors", buildDoctorsPanel());
        tabs.addTab("Appointments", buildAppointmentsPanel());
        add(tabs, BorderLayout.CENTER);

        JLabel footer = new JLabel("Standalone Java Swing application • Data saved locally");
        footer.setBorder(BorderFactory.createEmptyBorder(5, 12, 12, 12));
        add(footer, BorderLayout.SOUTH);
        refreshTables();
    }

    private JPanel buildPatientsPanel() {
        patientTable = new JTable();
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add Patient");
        JButton delete = new JButton("Delete Selected");
        add.addActionListener(e -> addPatient());
        delete.addActionListener(e -> deleteSelected(patientTable, "patient"));
        buttons.add(add); buttons.add(delete);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildDoctorsPanel() {
        doctorTable = new JTable();
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(doctorTable), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add Doctor");
        JButton delete = new JButton("Delete Selected");
        add.addActionListener(e -> addDoctor());
        delete.addActionListener(e -> deleteSelected(doctorTable, "doctor"));
        buttons.add(add); buttons.add(delete);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildAppointmentsPanel() {
        appointmentTable = new JTable();
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Book Appointment");
        JButton delete = new JButton("Cancel Selected");
        add.addActionListener(e -> addAppointment());
        delete.addActionListener(e -> deleteSelected(appointmentTable, "appointment"));
        buttons.add(add); buttons.add(delete);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void addPatient() {
        JTextField name = new JTextField();
        JTextField age = new JTextField();
        JComboBox<String> gender = new JComboBox<>(new String[]{"Female", "Male", "Other"});
        JTextField phone = new JTextField();
        JTextField diagnosis = new JTextField();
        Object[] fields = {"Full name:", name, "Age:", age, "Gender:", gender, "Phone:", phone, "Diagnosis / notes:", diagnosis};
        if (JOptionPane.showConfirmDialog(this, fields, "Add Patient", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            String n = required(name.getText(), "Name");
            int a = Integer.parseInt(required(age.getText(), "Age"));
            if (a < 0 || a > 130) throw new IllegalArgumentException("Enter a valid age.");
            data.patients.add(new Patient(data.nextPatientId++, n, a, (String) gender.getSelectedItem(),
                    required(phone.getText(), "Phone"), diagnosis.getText().trim()));
            saveAndRefresh();
        } catch (Exception ex) { showError(ex.getMessage() == null ? "Invalid input." : ex.getMessage()); }
    }

    private void addDoctor() {
        JTextField name = new JTextField();
        JTextField specialty = new JTextField();
        JTextField phone = new JTextField();
        Object[] fields = {"Doctor name:", name, "Specialization:", specialty, "Phone:", phone};
        if (JOptionPane.showConfirmDialog(this, fields, "Add Doctor", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            data.doctors.add(new Doctor(data.nextDoctorId++, required(name.getText(), "Name"),
                    required(specialty.getText(), "Specialization"), required(phone.getText(), "Phone")));
            saveAndRefresh();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void addAppointment() {
        if (data.patients.isEmpty() || data.doctors.isEmpty()) {
            showError("Add at least one patient and one doctor before booking an appointment.");
            return;
        }
        JComboBox<String> patients = new JComboBox<>();
        for (Patient p : data.patients) patients.addItem(p.getId() + " - " + p.getName());
        JComboBox<String> doctors = new JComboBox<>();
        for (Doctor d : data.doctors) doctors.addItem(d.getId() + " - " + d.getName() + " (" + d.getSpecialization() + ")");
        JTextField date = new JTextField("2026-10-25");
        JTextField time = new JTextField("10:00");
        JTextField reason = new JTextField();
        Object[] fields = {"Patient:", patients, "Doctor:", doctors, "Date (YYYY-MM-DD):", date,
                "Time (HH:MM):", time, "Reason:", reason};
        if (JOptionPane.showConfirmDialog(this, fields, "Book Appointment", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            String d = required(date.getText(), "Date");
            String t = required(time.getText(), "Time");
            if (!d.matches("\\d{4}-\\d{2}-\\d{2}")) throw new IllegalArgumentException("Use date format YYYY-MM-DD.");
            if (!t.matches("\\d{2}:\\d{2}")) throw new IllegalArgumentException("Use time format HH:MM.");
            int patientId = Integer.parseInt(((String) patients.getSelectedItem()).split(" - ")[0]);
            int doctorId = Integer.parseInt(((String) doctors.getSelectedItem()).split(" - ")[0]);
            data.appointments.add(new Appointment(data.nextAppointmentId++, patientId, doctorId, d, t,
                    required(reason.getText(), "Reason")));
            saveAndRefresh();
        } catch (Exception ex) { showError(ex.getMessage() == null ? "Invalid input." : ex.getMessage()); }
    }

    private void deleteSelected(JTable table, String type) {
        int row = table.getSelectedRow();
        if (row < 0) { showError("Select a row first."); return; }
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        int choice = JOptionPane.showConfirmDialog(this, "Delete selected " + type + "?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;
        if (type.equals("patient")) {
            data.patients.removeIf(p -> p.getId() == id);
            data.appointments.removeIf(a -> a.getPatientId() == id);
        } else if (type.equals("doctor")) {
            data.doctors.removeIf(d -> d.getId() == id);
            data.appointments.removeIf(a -> a.getDoctorId() == id);
        } else {
            data.appointments.removeIf(a -> a.getId() == id);
        }
        saveAndRefresh();
    }

    private void refreshTables() {
        patientTable.setModel(model(new String[]{"ID", "Name", "Age", "Gender", "Phone", "Diagnosis"},
                data.patients.stream().map(p -> new Object[]{p.getId(), p.getName(), p.getAge(), p.getGender(), p.getPhone(), p.getDiagnosis()}).toList()));
        doctorTable.setModel(model(new String[]{"ID", "Name", "Specialization", "Phone"},
                data.doctors.stream().map(d -> new Object[]{d.getId(), d.getName(), d.getSpecialization(), d.getPhone()}).toList()));
        appointmentTable.setModel(model(new String[]{"ID", "Patient", "Doctor", "Date", "Time", "Reason"},
                data.appointments.stream().map(a -> new Object[]{a.getId(), patientName(a.getPatientId()),
                        doctorName(a.getDoctorId()), a.getDate(), a.getTime(), a.getReason()}).toList()));
    }

    private DefaultTableModel model(String[] columns, List<Object[]> rows) {
        DefaultTableModel m = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        for (Object[] row : rows) m.addRow(row);
        return m;
    }

    private String patientName(int id) {
        return data.patients.stream().filter(p -> p.getId() == id).map(Patient::getName).findFirst().orElse("Unknown");
    }

    private String doctorName(int id) {
        return data.doctors.stream().filter(d -> d.getId() == id).map(Doctor::getName).findFirst().orElse("Unknown");
    }

    private String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException(field + " is required.");
        return value.trim();
    }

    private void saveAndRefresh() {
        saveData();
        refreshTables();
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(data);
        } catch (IOException ex) { showError("Could not save data: " + ex.getMessage()); }
    }

    private DataStore loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return new DataStore();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (DataStore) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Could not load saved data. Starting fresh.\n" + ex.getMessage());
            return new DataStore();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    private static class DataStore implements Serializable {
        private static final long serialVersionUID = 1L;
        List<Patient> patients = new ArrayList<>();
        List<Doctor> doctors = new ArrayList<>();
        List<Appointment> appointments = new ArrayList<>();
        int nextPatientId = 1, nextDoctorId = 1, nextAppointmentId = 1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalManagementSystem().setVisible(true));
    }
}
