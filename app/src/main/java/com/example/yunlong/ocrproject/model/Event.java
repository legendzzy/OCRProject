package com.example.yunlong.ocrproject.model;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yunlong on 2019/5/5.
 */

public class Event {

    private int id;
    private String content;
    private String label;
    private Date date;
    private String datebyString;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void updateDate() {
        date = new Date();
    }
    public void setDate(String date) {
        this.datebyString = date;
    }

    public String getDateByString() {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy年MM月dd日 E a hh:mm");
        datebyString = ft.format(date).toString();
        return datebyString;
    }
    public String getDate() {
        return datebyString;
    }

}
