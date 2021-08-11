package ru.geekbrains.mymenuauth.data

import android.content.res.Resources
import ru.geekbrains.mymenuauth.R
import java.util.*

class NotesSourceImpl : NotesSource{

    var dataSource : MutableList<NoteData>
    var resources : Resources

    constructor(resources : Resources) {
        this.dataSource = ArrayList(20)
        this.resources = resources
    }

    override fun init(notesSourceResponse : NotesSourceResponse) : NotesSource{
        val titels = resources.getStringArray(R.array.titels)
        val discription = resources.getStringArray(R.array.discription)

        for (i in 0 .. discription.size) {
            dataSource.add(NoteData(titels[i], discription[i], Calendar.getInstance().getTime()))
        }

        if (notesSourceResponse != null) {
            notesSourceResponse.initialized(this)
        }
        return this
    }

    override fun getNoteData(position : Int) : NoteData{
        return dataSource.get(position)
    }

    override fun size() : Int {
        return dataSource.size
    }

    override fun addNoteData(noteData : NoteData) {
        dataSource.add(noteData)
    }

    override fun deleteNoteData(position : Int) {
        dataSource.removeAt(position)
    }

    override fun updateNoteData(position : Int, noteData : NoteData) {
        dataSource.set(position, noteData)
    }

    override fun clearNoteData() {
        dataSource.clear()
    }
}
