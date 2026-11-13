package com.social7.noteapp.model;

import com.google.gson.annotations.SerializedName;

public class Note {
    public long id;
    @SerializedName("category_id") public long categoryId;
    public String title;
    public String content;
    @SerializedName("category_name") public String categoryName;
    @SerializedName("created_at") public String createdAt;
    @SerializedName("updated_at") public String updatedAt;
}
