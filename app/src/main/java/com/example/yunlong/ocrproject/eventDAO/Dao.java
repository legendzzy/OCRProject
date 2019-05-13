package com.example.yunlong.ocrproject.eventDAO;

import com.example.yunlong.ocrproject.Event;

import java.util.List;

/**
 * Created by yunlong on 2019/5/5.
 */

public interface Dao {
    Event searchEvent(int id);
    List<Event> findAllEvent();
    Boolean addEvent(Event event);
    Boolean deleteEvent(int id);
    Boolean updateEvent(int id, String content, String label, String date);
}
