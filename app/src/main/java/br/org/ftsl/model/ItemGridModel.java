package br.org.ftsl.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

import br.org.ftsl.utils.Utils;

@DatabaseTable(tableName = "grid")
public class ItemGridModel implements Serializable{

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private Integer pid;

    @DatabaseField
    private Integer date;

    @DatabaseField
    private Integer time;

    @DatabaseField
    private Integer type;

    @DatabaseField
    private Integer place;

    @DatabaseField
    private String title;

    @DatabaseField
    private String description;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private AuthorModel author;

    private Integer authorId;

    private String authorName;

    private String curriculum;

    @DatabaseField
    private Date inicio;

    @DatabaseField
    private Date fim;

    @DatabaseField
    private Boolean assistir = Boolean.FALSE;

    public Integer getPlace() {
        return place;
    }

    public String getPlaceDescription(){
        return Utils.getPlace(getType(), getPlace());
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuthorModel getAuthor() {
        return author;
    }

    public void setAuthor(AuthorModel author) {
        this.author = author;
    }

    public Boolean getAssistir() {
        return assistir;
    }

    public void setAssistir(Boolean assistir) {
        this.assistir = assistir;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "ItemGridModel{" +
                "id=" + id +
                ", pid=" + pid +
                ", date=" + date +
                ", time=" + time +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", assistir=" + assistir +
                '}';
    }

}
