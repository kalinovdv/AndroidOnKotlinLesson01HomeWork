package ru.geekbrains.mymenuauth.ui;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import ru.geekbrains.mymenuauth.data.NoteData;
import ru.geekbrains.mymenuauth.R;
import ru.geekbrains.mymenuauth.data.NotesSource;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private OnNoteClickListener noteClickListener;
    private NotesSource dataSource;
    private Fragment fragment;
    private int menuPosition;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder viewHolder, int position) {
        viewHolder.setData(dataSource.getNoteData(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    public void setDataSource(NotesSource dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView discription;
        private TextView date;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.noteItemCardViewTitel);
            this.discription = itemView.findViewById(R.id.noteItemCardViewDiscription);
            this.date = itemView.findViewById(R.id.noteItemCardViewDate);

            registerContextMenu(itemView);

            this.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (noteClickListener != null) {
                        noteClickListener.onNoteClick(view, getAdapterPosition());
                    }
                }
            });

            this.title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    menuPosition = getLayoutPosition();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        itemView.showContextMenu(10, 10);
                    }
                    return true;
                }
            });
        }

        private void registerContextMenu(View itemView) {
            if (fragment != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        menuPosition = getLayoutPosition();
                        return false;
                    }
                });
                fragment.registerForContextMenu(itemView);
            }
        }

        public void setData(NoteData noteData) {
            title.setText(noteData.getTitel());
            discription.setText(noteData.getText());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm, dd-EEEE-yyyy");
            String dateForm = simpleDateFormat.format(noteData.getDate());
            date.setText(dateForm);
        }
    }

    public void SetOnNoteClickListener(OnNoteClickListener noteClickListener) {
        this.noteClickListener = noteClickListener;
    }

    public interface OnNoteClickListener {
        void onNoteClick(View view, int position);
    }
}
