package ru.geekbrains.mymenuauth.data;

public interface NotesSource {
    NotesSource init(NotesSourceResponse notesSourceResponse);
    NoteData getNoteData(int position);
    int size();
    void addNoteData(NoteData noteData);
    void deleteNoteData(int position);
    void updateNoteData(int position, NoteData noteData);
    void clearNoteData();
}
