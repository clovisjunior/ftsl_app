package br.org.ftsl.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by 05081364908 on 08/08/2017.
 */
@DatabaseTable(tableName = "classroom")
public class ClassRoomModel implements Serializable {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private Integer type;

    @DatabaseField
    private Integer place;

    @DatabaseField
    private String description;

    public ClassRoomModel(){

    }

    public ClassRoomModel(Integer id, Integer type, Integer place, String description) {
        this.setId(id);
        this.setType(type);
        this.setPlace(place);
        this.setDescription(description);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ClassRoomModel{" +
                "id=" + id +
                ", type=" + type +
                ", place=" + place +
                ", description='" + description + '\'' +
                '}';
    }
}
