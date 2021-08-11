package ru.geekbrains.mymenuauth.data

import com.google.firebase.Timestamp
import java.util.*

class NoteDataMapping {
    object Fields {
        const val DATE: String = "date"
        const val TITLE: String = "title"
        const val TEXT: String = "text" }

    companion object {
        @JvmStatic
        fun toNoteData(id : String, doc : MutableMap<String, Any>) : NoteData {
            val timestamp : Timestamp = doc.get(Fields.DATE) as Timestamp
            val answer = NoteData(doc.get(Fields.TITLE) as String, doc.get(Fields.TEXT) as String, timestamp.toDate())
            answer.id = id
            return answer
        }

        @JvmStatic
       fun toDocument(noteData : NoteData) : MutableMap<String, Any> {
            val answer : MutableMap<String, Any> = HashMap()
            answer.put(Fields.TITLE, noteData.titel)
            answer.put(Fields.TEXT, noteData.text)
            answer.put(Fields.DATE, noteData.date)
            return answer
        }
    }
}
