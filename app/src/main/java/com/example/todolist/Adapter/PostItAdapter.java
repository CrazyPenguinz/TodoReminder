package com.example.todolist.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Model.PostIt;
import com.example.todolist.R;

import java.util.ArrayList;
import java.util.List;

public class PostItAdapter extends RecyclerView.Adapter<PostItAdapter.NoteHolder> {
    private OnItemClickListener listener;
    private List<PostIt> postIts = new ArrayList<>();

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        PostIt currentNote = postIts.get(position);
        holder.tv_title.setText(currentNote.getTitle());
        holder.tv_location.setText(currentNote.getPlaceName());
        holder.tv_date.setText(currentNote.getDueDate());
    }

    @Override
    public int getItemCount() {
        return postIts.size();
    }

    public void setPostIts(List<PostIt> postIts) {
        this.postIts = postIts;
        notifyDataSetChanged();
    }

    public PostIt getNoteAt(int position) {
        return postIts.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_location;
        private TextView tv_date;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_date = itemView.findViewById(R.id.tv_dueDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(postIts.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(PostIt note);
    }

    public void SetOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
