package ru.geekbrains.mymenuauth.data;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.geekbrains.mymenuauth.R;

public class NotesSourceImpl implements NotesSource{

    private List<NoteData> dataSource;
    private Resources resources;

    public NotesSourceImpl(Resources resources) {
        this.dataSource = new ArrayList<>(20);
        this.resources = resources;
    }

    public NotesSource init(NotesSourceResponse notesSourceResponse) {
        String[] titels = resources.getStringArray(R.array.titels);
        String[] discription = resources.getStringArray(R.array.discription);

        for (int i = 0; i < discription.length; i++) {
            dataSource.add(new NoteData(titels[i], discription[i], Calendar.getInstance().getTime()));
        }

        if (notesSourceResponse != null) {
            notesSourceResponse.initialized(this);
        }
        return this;
    }

    @Override
    public NoteData getNoteData(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size() {
        return dataSource.size();
    }

    @Override
    public void addNoteData(NoteData noteData) {
        dataSource.add(noteData);
    }

    @Override
    public void deleteNoteData(int position) {
        dataSource.remove(position);
    }

    @Override
    public void updateNoteData(int position, NoteData noteData) {
        dataSource.set(position, noteData);
    }

    @Override
    public void clearNoteData() {
        dataSource.clear();
    }
}
