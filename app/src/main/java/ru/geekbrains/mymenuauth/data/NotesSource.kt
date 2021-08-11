package ru.geekbrains.mymenuauth.data

interface NotesSource {
    fun init(notesSourceResponse : NotesSourceResponse) : NotesSource
    fun getNoteData(position : Int) : NoteData
    fun size() : Int
    fun addNoteData(noteData : NoteData) : Unit
    fun deleteNoteData(position : Int) : Unit
    fun updateNoteData(position : Int, noteData : NoteData) : Unit
    fun clearNoteData() : Unit
}
