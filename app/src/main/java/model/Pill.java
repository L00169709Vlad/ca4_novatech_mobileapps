package model;

public class Pill {
    private String name;
    private int hour;
    private int minute;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public Pill() {} //must for firestore to work

    public Pill(String name, int hour, int minute){
        this.name = name;
        this.hour = hour;
        this.minute = minute;
    }
    public Pill(String name, int hour, int minute, String userId){
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Pill{" +
                "name='" + name + '\'' +
                ", hour=" + hour +
                ", minute=" + minute +
                ", userId='" + userId + '\'' +
                '}';
    }
}
