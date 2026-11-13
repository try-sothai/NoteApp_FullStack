package com.social7.noteapp.network;

import com.social7.noteapp.model.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("categories") Call<ApiResponse<List<Category>>> getCategories();
    @POST("categories") Call<ApiResponse<Category>> createCategory(@Body CategoryRequest body);
    @GET("categories/{id}") Call<ApiResponse<Category>> getCategory(@Path("id") long id);
    @PUT("categories/{id}") Call<ApiResponse<Category>> updateCategory(@Path("id") long id, @Body CategoryRequest body);
    @DELETE("categories/{id}") Call<ApiResponse<Void>> deleteCategory(@Path("id") long id);

    @GET("notes") Call<ApiResponse<List<Note>>> getNotes();
    @GET("notes/{id}") Call<ApiResponse<Note>> getNote(@Path("id") long id);
    @POST("notes") Call<ApiResponse<Note>> createNote(@Body NoteRequest body);
    @PUT("notes/{id}") Call<ApiResponse<Note>> updateNote(@Path("id") long id, @Body NoteRequest body);
    @DELETE("notes/{id}") Call<ApiResponse<Void>> deleteNote(@Path("id") long id);
}
