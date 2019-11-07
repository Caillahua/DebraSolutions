package com.codev.capturalo.data.model.holder;

import java.util.ArrayList;

/**
 * Created by miguel on 7/04/17.
 */

public class TrackEntityHolder<T> {
    private String next;
    private String previous;
    private int count;
    private ArrayList<T> results;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<T> getResults() {
        return results;
    }

    public void setResults(ArrayList<T> results) {
        this.results = results;
    }
}
