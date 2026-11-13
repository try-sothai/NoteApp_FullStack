package com.social7.noteapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse<T> {
    @SerializedName("success") public boolean success;
    @SerializedName("message") public String message;
    @SerializedName("data") public T data;
}
