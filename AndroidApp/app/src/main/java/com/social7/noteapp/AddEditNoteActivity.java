package com.social7.noteapp;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.social7.noteapp.model.*;
import com.social7.noteapp.network.*;
import java.util.*;
import retrofit2.*;

import android.util.Log;

public class AddEditNoteActivity extends AppCompatActivity {
    private EditText titleInput, contentInput;
    private Spinner categorySpinner;
    private ApiService api;
    private List<Category> categories = new ArrayList<>();
    private long noteId = -1;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);
        api = RetrofitClient.getApi();
        titleInput = findViewById(R.id.titleInput);
        contentInput = findViewById(R.id.contentInput);
        categorySpinner = findViewById(R.id.categorySpinner);
        noteId = getIntent().getLongExtra("note_id", -1);
        findViewById(R.id.saveNoteButton).setOnClickListener(v -> saveNote());
        loadCategories();
    }

    private void loadCategories() {
        Log.d("API_URL", "GET " + api.getCategories().request().url());
        api.getCategories().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override public void onResponse(Call<ApiResponse<List<Category>>> c, Response<ApiResponse<List<Category>>> r) {
                Log.d("API_RESPONSE", "HTTP Code: " + r.code());
                if (!r.isSuccessful() || r.body() == null || r.body().data == null || r.body().data.isEmpty()) {
                    Log.e("API_ERROR", "loadCategories failed. Code: " + r.code() + " Body: " + (r.body() != null ? r.body().message : "null"));
                    Toast.makeText(AddEditNoteActivity.this, "Create a category first.", Toast.LENGTH_LONG).show();
                    finish(); return;
                }
                categories = r.body().data;
                Log.d("API_DATA", "Category count: " + categories.size());
                ArrayAdapter<Category> a = new ArrayAdapter<>(AddEditNoteActivity.this, android.R.layout.simple_spinner_item, categories);
                a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(a);
                if (noteId > 0) loadNote();
            }
            @Override public void onFailure(Call<ApiResponse<List<Category>>> c, Throwable t) {
                Log.e("API_ERROR", "loadCategories Network Error: " + t.getMessage(), t);
                toast(t.getMessage());
            }
        });
    }

    private void loadNote() {
        api.getNote(noteId).enqueue(new Callback<ApiResponse<Note>>() {
            @Override public void onResponse(Call<ApiResponse<Note>> c, Response<ApiResponse<Note>> r) {
                if (!r.isSuccessful() || r.body() == null || r.body().data == null) { toast("Could not load note"); finish(); return; }
                Note n = r.body().data;
                titleInput.setText(n.title);
                contentInput.setText(n.content);
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).id == n.categoryId) { categorySpinner.setSelection(i); break; }
                }
            }
            @Override public void onFailure(Call<ApiResponse<Note>> c, Throwable t) { toast(t.getMessage()); }
        });
    }

    private void saveNote() {
        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();
        if (title.isEmpty()) { titleInput.setError("Title is required"); return; }
        if (categories.isEmpty()) { toast("No categories available"); return; }
        long categoryId = categories.get(categorySpinner.getSelectedItemPosition()).id;
        NoteRequest body = new NoteRequest(categoryId, title, content);
        if (noteId > 0) {
            api.updateNote(noteId, body).enqueue(new Callback<ApiResponse<Note>>() {
                @Override public void onResponse(Call<ApiResponse<Note>> c, Response<ApiResponse<Note>> r) {
                    if (r.isSuccessful()) { toast("Note updated"); finish(); }
                    else toast("Save failed. HTTP " + r.code());
                }
                @Override public void onFailure(Call<ApiResponse<Note>> c, Throwable t) { toast(t.getMessage()); }
            });
        } else {
            api.createNote(body).enqueue(new Callback<ApiResponse<Note>>() {
                @Override public void onResponse(Call<ApiResponse<Note>> c, Response<ApiResponse<Note>> r) {
                    if (r.isSuccessful()) { toast("Note created"); finish(); }
                    else toast("Save failed. HTTP " + r.code());
                }
                @Override public void onFailure(Call<ApiResponse<Note>> c, Throwable t) { toast(t.getMessage()); }
            });
        }
    }

    private void toast(String m) { Toast.makeText(this, m, Toast.LENGTH_LONG).show(); }
}
