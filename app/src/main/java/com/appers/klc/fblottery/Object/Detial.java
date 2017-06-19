package com.appers.klc.fblottery.Object;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klc on 2017/4/22.
 */

public class Detial {
    public String created_time;
    public String message;
    public String id;
    public String name;
    public List<msgtag> message_tags = new ArrayList<>();
    public String story;
    public From from;

    public From getFrom() {
        return from;
    }

    public List<msgtag> getMessage_tags() {
        return message_tags;
    }

    public void setMessage_tags(List<msgtag> message_tags) {
        this.message_tags = message_tags;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
