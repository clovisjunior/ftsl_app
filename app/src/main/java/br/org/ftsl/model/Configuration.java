package br.org.ftsl.model;

import java.util.List;

/**
 * Created by 05081364908 on 09/08/17.
 */

public class Configuration {

    private List<TimeModel> times;
    private List<ClassRoomModel> labs;
    private List<ClassRoomModel> rooms;
    private List<DayModel> days;
    private List<TypeModel> types;
    private String about;

    public List<TimeModel> getTimes() {
        return times;
    }

    public void setTimes(List<TimeModel> times) {
        this.times = times;
    }

    public List<ClassRoomModel> getLabs() {
        return labs;
    }

    public void setLabs(List<ClassRoomModel> labs) {
        this.labs = labs;
    }

    public List<ClassRoomModel> getRooms() {
        return rooms;
    }

    public void setRooms(List<ClassRoomModel> rooms) {
        this.rooms = rooms;
    }

    public List<DayModel> getDays() {
        return days;
    }

    public void setDays(List<DayModel> days) {
        this.days = days;
    }

    public List<TypeModel> getTypes() {
        return types;
    }

    public void setTypes(List<TypeModel> types) {
        this.types = types;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "times=" + times +
                ", labs=" + labs +
                ", rooms=" + rooms +
                ", days=" + days +
                ", types=" + types +
                '}';
    }
}
