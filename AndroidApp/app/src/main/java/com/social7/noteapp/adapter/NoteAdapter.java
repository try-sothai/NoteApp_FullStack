package com.social7.noteapp.adapter;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.social7.noteapp.R;
import com.social7.noteapp.model.Note;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteVH> {
    public interface Listener {
        void onEdit(Note note);
        void onDelete(Note note);
    }

    private final List<Note> items = new ArrayList<>();
    private final Listener listener;

    public NoteAdapter(Listener listener) { this.listener = listener; }
    public void setItems(List<Note> notes) {
        items.clear();
        if (notes != null) items.addAll(notes);
        notifyDataSetChanged();
    }

    @NonNull @Override public NoteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteVH(v);
    }

    @Override public void onBindViewHolder(@NonNull NoteVH h, int position) {
        Note n = items.get(position);
        h.title.setText(n.title);
        h.content.setText(n.content == null ? "" : n.content);
        h.category.setText(n.categoryName == null ? "Uncategorized" : n.categoryName);
        h.edit.setOnClickListener(v -> listener.onEdit(n));
        h.delete.setOnClickListener(v -> listener.onDelete(n));
    }

    @Override public int getItemCount() { return items.size(); }

    static class NoteVH extends RecyclerView.ViewHolder {
        TextView title, content, category;
        ImageButton edit, delete;
        NoteVH(View v) {
            super(v);
            title = v.findViewById(R.id.noteTitle);
            content = v.findViewById(R.id.noteContent);
            category = v.findViewById(R.id.noteCategory);
            edit = v.findViewById(R.id.editNote);
            delete = v.findViewById(R.id.deleteNote);
        }
    }
}
