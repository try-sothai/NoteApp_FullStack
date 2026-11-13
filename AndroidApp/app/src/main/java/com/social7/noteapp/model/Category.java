package com.social7.noteapp.model;

public class Category {
    public long id;
    public String name;
    public String description;

    @Override public String toString() { return name; }
}
