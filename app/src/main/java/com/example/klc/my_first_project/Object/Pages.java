package com.example.klc.my_first_project.Object;

/**
 * Created by klc on 2017/4/22.
 */

public class Pages {
    public Cursors cursors;
    public String next;
    public String previous;
    public Cursors getCursors() {
        return cursors;
    }

    public void setCursors(Cursors cursors) {
        this.cursors = cursors;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }
}
