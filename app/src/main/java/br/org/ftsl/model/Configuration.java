package br.org.ftsl.model;

/**
 * Created by 05081364908 on 09/08/17.
 */

public class Configuration {

    private String year;
    private String path;
    private String config;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "year='" + year + '\'' +
                ", path='" + path + '\'' +
                ", config='" + config + '\'' +
                '}';
    }
}
