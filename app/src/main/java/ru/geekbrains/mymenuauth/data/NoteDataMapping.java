package ru.geekbrains.mymenuauth.data;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class NoteDataMapping {
    public static class Fields {
        public static String DATE = "date";
        public static String TITLE = "title";
        public static String TEXT = "text";
    }

    public static NoteData toNoteData(String id, Map<String, Object> doc) {
        Timestamp timestamp = (Timestamp) doc.get(Fields.DATE);
        NoteData answer = new NoteData((String) doc.get(Fields.TITLE), (String) doc.get(Fields.TEXT), timestamp.toDate());
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(NoteData noteData) {
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.TITLE, noteData.getTitel());
        answer.put(Fields.TEXT, noteData.getText());
        answer.put(Fields.DATE, noteData.getDate());
        return answer;
    }
}
