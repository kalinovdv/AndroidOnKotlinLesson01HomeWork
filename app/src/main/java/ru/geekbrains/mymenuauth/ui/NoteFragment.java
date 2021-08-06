package ru.geekbrains.mymenuauth.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

import ru.geekbrains.mymenuauth.MainActivity;
import ru.geekbrains.mymenuauth.Navigation;
import ru.geekbrains.mymenuauth.data.NoteData;
import ru.geekbrains.mymenuauth.R;
import ru.geekbrains.mymenuauth.observe.Publisher;

public class NoteFragment extends Fragment {

    private static final String ARG_NOTE_DATA = "Param_NoteData";

    private NoteData noteData;
    private Publisher publisher;

    private TextInputEditText title;
    private TextInputEditText text;
    private DatePicker datePicker;

    public static NoteFragment newInstance(NoteData noteData) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE_DATA, noteData);
        fragment.setArguments(args);
        return fragment;
    }

    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteData = getArguments().getParcelable(ARG_NOTE_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        intView(view);
        if (noteData != null) {
            populateView();
        }
        return view;
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        noteData = collectNoteData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(noteData);
    }

    private NoteData collectNoteData() {
        String title = this.title.getText().toString();
        String text = this.text.getText().toString();
        Date date = getDateFromDatePicker();
        if (noteData != null) {
            NoteData answer = new NoteData(title, text, date);
            answer.setId(noteData.getId());
            return answer;
        } else {
            return new NoteData(title, text, date);
        }
    }

    private Date getDateFromDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.datePicker.getYear());
        calendar.set(Calendar.MONTH, this.datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return calendar.getTime();
    }

    private void intView(View view) {
        title = view.findViewById(R.id.NoteFragmentInputTitle);
        text = view.findViewById(R.id.NoteFragmentInputText);
        datePicker = view.findViewById(R.id.NoteFragmentDatePicker);
    }

    private void populateView() {
        title.setText(noteData.getTitel());
        text.setText(noteData.getText());
        Log.d("mylog", String.valueOf(noteData.getDate()));
        initDatePicker(noteData.getDate());
    }

    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Log.d("mylog", String.valueOf(this.datePicker));
        this.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
    }

}