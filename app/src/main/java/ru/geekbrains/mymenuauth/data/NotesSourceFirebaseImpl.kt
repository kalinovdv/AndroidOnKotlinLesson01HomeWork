package ru.geekbrains.mymenuauth.data

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class NotesSourceFirebaseImpl : NotesSource {

    val NOTES_COLLECTION: String = "notes"
    val TAG: String = "NotesSrcFirebaseImpl"

    var store: FirebaseFirestore = FirebaseFirestore.getInstance()

    var collection: CollectionReference = store.collection(NOTES_COLLECTION)

    var notesData: MutableList<NoteData> = ArrayList<NoteData>()

    override fun init(notesSourceResponse: NotesSourceResponse): NotesSource {
        Log.d(TAG, "override fun init(notesSourceResponse : NotesSourceResponse) : NotesSource")
        collection.orderBy(NoteDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful()) {
                        Log.d(TAG, "if (task.isSuccessful()) {")
                        notesData = ArrayList()
                        for (document in task.result!!) {
                            val doc: MutableMap<String, Any> = document.data
                            val id: String = document.id
                            val noteData: NoteData = NoteDataMapping.toNoteData(id, doc)
                            notesData.add(noteData)
                        }
                        Log.d(TAG, "данные получены: " + notesData.size)
                        notesSourceResponse.initialized(this)
                    } else {
                        Log.d(TAG, "данные не получены: " + task.getException())
                    }
                }).addOnFailureListener(OnFailureListener { exception -> Log.d(TAG, "ошибка при получении данных: " + exception.toString() ) })
        return this
    }

    override fun getNoteData(position: Int): NoteData {
        return notesData.get(position)
    }

    override fun size(): Int {
        if (notesData == null) {
            return 0
        }
        return notesData.size
    }

    override fun addNoteData(noteData: NoteData) {
        collection.add(NoteDataMapping.toDocument(noteData))
            .addOnSuccessListener(OnSuccessListener<DocumentReference>() {
                fun onSuccess(documentReference: DocumentReference) {
                    noteData.id = documentReference.getId()
                }
            })
    }

    override fun deleteNoteData(position: Int) {
        collection.document(notesData.get(position).id).delete()
        notesData.removeAt(position)
    }

    override fun updateNoteData(position: Int, noteData: NoteData) {
        val id: String = noteData.id
        collection.document(id).set(NoteDataMapping.toDocument(noteData))
    }

    override fun clearNoteData() {
        for (noteData in notesData) {
            collection.document(noteData.id).delete()
        }
        notesData = ArrayList<NoteData>()
    }
}
