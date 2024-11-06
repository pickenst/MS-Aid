package edu.utsa.cs3443.msaid.model;

import java.util.ArrayList;

public class User {
    private String name;
    private int age;
    private ArrayList<Alarm> alarms;
    private ArrayList<Medicine> medicines;

    public User(String name, int age){
        this.name = name;
        this.age = age;
        this.alarms = new ArrayList<>();
        this.medicines = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    public ArrayList<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(ArrayList<Medicine> medicines) {
        this.medicines = medicines;
    }
}
