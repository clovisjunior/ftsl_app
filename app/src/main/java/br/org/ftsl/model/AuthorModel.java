package br.org.ftsl.model;

import android.content.ClipData;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by 05081364908 on 21/07/14.
 */
@DatabaseTable(tableName = "author")
public class AuthorModel implements Serializable {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String curriculum;

    public AuthorModel(){

    }

    public AuthorModel(Integer authorId, String authorName, String curriculum) {
        this.id = authorId;
        this.name = authorName;
        this.curriculum = curriculum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    @Override
    public String toString() {
        return "AuthorModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", curriculum='" + curriculum + '\'' +
                '}';
    }
}
