package com.social7.noteapp.model;

import com.google.gson.annotations.SerializedName;

public class NoteRequest {
    @SerializedName("category_id") public long categoryId;
    public String title;
    public String content;

    public NoteRequest(long categoryId, String title, String content) {
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
    }
}
