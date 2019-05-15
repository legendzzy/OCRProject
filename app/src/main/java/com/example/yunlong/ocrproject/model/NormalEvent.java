package com.example.yunlong.ocrproject.model;

import com.example.yunlong.ocrproject.model.Event;

/**
 * Created by yunlong on 2019/5/5.
 */

public class NormalEvent extends Event {
    public NormalEvent() {
        super();
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }


    public String getContent() {
        return super.getContent();
    }

    @Override
    public void setContent(String content) {
        super.setContent(content);
    }

    @Override
    public String getLabel() {
        return super.getLabel();
    }

    @Override
    public void setLabel(String label) {
        super.setLabel(label);
    }


}
