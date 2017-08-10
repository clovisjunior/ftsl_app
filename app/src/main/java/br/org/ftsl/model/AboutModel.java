package br.org.ftsl.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by 05081364908 on 10/08/17.
 */
@DatabaseTable(tableName = "about")
public class AboutModel implements Serializable {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String event;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
