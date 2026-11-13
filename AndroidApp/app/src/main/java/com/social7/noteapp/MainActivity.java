package com.social7.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.social7.noteapp.adapter.NoteAdapter;
import com.social7.noteapp.model.*;
import com.social7.noteapp.network.*;
import java.util.List;
import retrofit2.*;

import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private NoteAdapter adapter;
    private ApiService api;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = RetrofitClient.getApi();

        RecyclerView recycler = findViewById(R.id.notesRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(new NoteAdapter.Listener() {
            @Override public void onEdit(Note note) {
                Intent i = new Intent(MainActivity.this, AddEditNoteActivity.class);
                i.putExtra("note_id", note.id);
                startActivity(i);
            }
            @Override public void onDelete(Note note) { confirmDelete(note); }
        });
        recycler.setAdapter(adapter);

        findViewById(R.id.addNoteButton).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditNoteActivity.class)));
        findViewById(R.id.manageCategoriesButton).setOnClickListener(v -> manageCategories());
        loadNotes();
    }

    @Override protected void onResume() { super.onResume(); if (api != null && adapter != null) loadNotes(); }

    private void loadNotes() {
        Log.d("API_URL", "GET " + api.getNotes().request().url());
        api.getNotes().enqueue(new Callback<ApiResponse<List<Note>>>() {
            @Override public void onResponse(Call<ApiResponse<List<Note>>> call, Response<ApiResponse<List<Note>>> response) {
                Log.d("API_RESPONSE", "HTTP Code: " + response.code());
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    List<Note> notes = response.body().data;
                    Log.d("API_DATA", "Notes count: " + (notes != null ? notes.size() : 0));
                    adapter.setItems(notes);
                } else {
                    Log.e("API_ERROR", "Could not load notes. Code: " + response.code());
                    toast("Could not load notes. Check API response.");
                }
            }
            @Override public void onFailure(Call<ApiResponse<List<Note>>> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage(), t);
                toast("Network error: " + t.getMessage());
            }
        });
    }

    private void confirmDelete(Note note) {
        new AlertDialog.Builder(this).setTitle("Delete note?")
                .setMessage("Delete \"" + note.title + "\"?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (d, w) -> api.deleteNote(note.id).enqueue(new Callback<ApiResponse<Void>>() {
                    @Override public void onResponse(Call<ApiResponse<Void>> c, Response<ApiResponse<Void>> r) {
                        if (r.isSuccessful()) { toast("Note deleted"); loadNotes(); }
                        else toast("Delete failed. It may already be removed.");
                    }
                    @Override public void onFailure(Call<ApiResponse<Void>> c, Throwable t) { toast(t.getMessage()); }
                })).show();
    }

    private void manageCategories() {
        api.getCategories().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override public void onResponse(Call<ApiResponse<List<Category>>> c, Response<ApiResponse<List<Category>>> r) {
                if (!r.isSuccessful() || r.body() == null || r.body().data == null) { toast("Cannot load categories"); return; }
                List<Category> cats = r.body().data;
                String[] names = new String[cats.size()];
                for (int i = 0; i < cats.size(); i++) names[i] = cats.get(i).name;
                new AlertDialog.Builder(MainActivity.this).setTitle("Select category to manage")
                        .setItems(names, (d, which) -> showCategoryActions(cats.get(which)))
                        .setPositiveButton("Add", (d, w) -> promptAddCategory())
                        .setNegativeButton("Close", null).show();
            }
            @Override public void onFailure(Call<ApiResponse<List<Category>>> c, Throwable t) { toast(t.getMessage()); }
        });
    }

    private void showCategoryActions(Category category) {
        new AlertDialog.Builder(this).setTitle(category.name)
                .setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
                    if (which == 0) promptEditCategory(category);
                    else confirmDeleteCategory(category);
                }).setNegativeButton("Cancel", null).show();
    }

    private void promptEditCategory(Category category) {
        EditText input = new EditText(this);
        input.setText(category.name);
        input.setHint("Category name");
        new AlertDialog.Builder(this).setTitle("Edit category").setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (d, w) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) { toast("Enter a category name"); return; }
                    api.updateCategory(category.id, new CategoryRequest(name, category.description))
                            .enqueue(new Callback<ApiResponse<Category>>() {
                                @Override public void onResponse(Call<ApiResponse<Category>> c, Response<ApiResponse<Category>> r) {
                                    toast(r.isSuccessful() ? "Category updated" : "Update failed");
                                }
                                @Override public void onFailure(Call<ApiResponse<Category>> c, Throwable t) { toast(t.getMessage()); }
                            });
                }).show();
    }

    private void confirmDeleteCategory(Category category) {
        new AlertDialog.Builder(this).setTitle("Delete category?")
                .setMessage("Delete " + category.name + "? Categories containing notes cannot be deleted.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (d, w) -> api.deleteCategory(category.id)
                        .enqueue(new Callback<ApiResponse<Void>>() {
                            @Override public void onResponse(Call<ApiResponse<Void>> c, Response<ApiResponse<Void>> r) {
                                if (r.isSuccessful()) toast("Category deleted");
                                else toast("Cannot delete: move or delete its notes first.");
                            }
                            @Override public void onFailure(Call<ApiResponse<Void>> c, Throwable t) { toast(t.getMessage()); }
                        })).show();
    }

    private void promptAddCategory() {
        EditText input = new EditText(this);
        input.setHint("Category name");
        new AlertDialog.Builder(this).setTitle("New category").setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (d, w) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) { toast("Enter a category name"); return; }
                    api.createCategory(new CategoryRequest(name, "")).enqueue(new Callback<ApiResponse<Category>>() {
                        @Override public void onResponse(Call<ApiResponse<Category>> c, Response<ApiResponse<Category>> r) {
                            toast(r.isSuccessful() ? "Category created" : "Could not create category");
                        }
                        @Override public void onFailure(Call<ApiResponse<Category>> c, Throwable t) { toast(t.getMessage()); }
                    });
                }).show();
    }

    private void toast(String msg) { Toast.makeText(this, msg, Toast.LENGTH_LONG).show(); }
}
