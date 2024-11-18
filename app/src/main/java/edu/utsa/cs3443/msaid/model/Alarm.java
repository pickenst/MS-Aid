package edu.utsa.cs3443.msaid.model;

public class Alarm {
    private final String name;
    private final String time;
    private final String amPm;

    public Alarm(String name, String time, String amPm) {
        this.name = name;
        this.time = time;
        this.amPm = amPm;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getAmPm() {
        return amPm;
    }
}
