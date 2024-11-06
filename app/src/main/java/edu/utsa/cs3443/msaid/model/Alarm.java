package edu.utsa.cs3443.msaid.model;

public class Alarm {
    private String name;
    private int hour;
    private int minute;
    private boolean pm;

    public Alarm(String name, int hour, int minute, boolean pm) {
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.pm = pm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isPm() {
        return pm;
    }

    public void setPm(boolean pm) {
        this.pm = pm;
    }
}
