package ru.geekbrains.mymenuauth.observe;

import ru.geekbrains.mymenuauth.data.NoteData;

public interface Observer {
    void updateNoteData(NoteData noteData);
}
