package br.org.ftsl.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by 05081364908 on 09/08/17.
 */
@DatabaseTable(tableName = "time")
public class TimeModel implements Serializable {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TimeModel{" +
                "id=" + id +
                ", time='" + time + '\'' +
                '}';
    }
}
