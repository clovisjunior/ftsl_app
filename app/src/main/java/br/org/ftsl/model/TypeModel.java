package br.org.ftsl.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by 05081364908 on 09/08/17.
 */
@DatabaseTable(tableName = "type")
public class TypeModel implements Serializable {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TypeModel{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
