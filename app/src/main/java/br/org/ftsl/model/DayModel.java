package br.org.ftsl.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 05081364908 on 09/08/17.
 */
@DatabaseTable(tableName = "day")
public class DayModel implements Serializable {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField(dataType = DataType.DATE_STRING, format = "dd/MM/yyyy")
    private Date day;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "DayModel{" +
                "id=" + id +
                ", day='" + day + '\'' +
                '}';
    }
}
