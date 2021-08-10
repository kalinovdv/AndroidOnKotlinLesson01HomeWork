package ru.geekbrains.mymenuauth.data;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

class NoteDataMapping {
    object Fields {
        const val DATE: String = "date"
        const val TITLE: String = "title"
        const val TEXT: String = "text" }

    companion object {
        @JvmStatic
        fun toNoteData(id : String, doc : Map<String, Object>) : NoteData {
            var timestamp : Timestamp = doc.get(Fields.DATE) as Timestamp
            var answer : NoteData = NoteData(doc.get(Fields.TITLE) as String, doc.get(Fields.TEXT) as String, timestamp.toDate())
            answer.id = id
            return answer
        }

        @JvmStatic
       fun toDocument(noteData : NoteData) : MutableMap<String, Any> {
            var answer : MutableMap<String, Any> = HashMap();
            answer.put(Fields.TITLE, noteData.titel);
            answer.put(Fields.TEXT, noteData.text);
            answer.put(Fields.DATE, noteData.date);
            return answer;
        }
    }
}
