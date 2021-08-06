package ru.geekbrains.mymenuauth.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import ru.geekbrains.mymenuauth.MainActivity;
import ru.geekbrains.mymenuauth.Navigation;
import ru.geekbrains.mymenuauth.StartFragment;
import ru.geekbrains.mymenuauth.data.NoteData;
import ru.geekbrains.mymenuauth.R;
import ru.geekbrains.mymenuauth.data.NotesSource;
import ru.geekbrains.mymenuauth.data.NotesSourceFirebaseImpl;
import ru.geekbrains.mymenuauth.data.NotesSourceResponse;
import ru.geekbrains.mymenuauth.observe.Observer;
import ru.geekbrains.mymenuauth.observe.Publisher;

public class NotesFragment extends Fragment {

    private static final int MY_DEFAULT_DURATION = 1000;

    private NotesSource data;
    private NotesAdapter adapter;
    private RecyclerView recyclerView;
    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToFirstPosition;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        data = new NotesSourceImpl(getResources()).init();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        //RecyclerView recyclerView = view.findViewById(R.id.fragmentNotesRecyclerView);
        initView(view);
        setHasOptionsMenu(true);
        //initPopupMenu(view);
        data = new NotesSourceFirebaseImpl().init(new NotesSourceResponse() {
            @Override
            public void initialized(NotesSource notesSource) {
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setDataSource(data);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity =(MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.fragmentNotesRecyclerView);
        //data = new NotesSourceImpl(getResources()).init();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NotesAdapter(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
            recyclerView.addItemDecoration(itemDecoration);
        }

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        if (moveToFirstPosition && data.size() > 0) {
            recyclerView.smoothScrollToPosition(0);
            moveToFirstPosition = false;
        }

        adapter.SetOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(View view, int position) {
                Toast.makeText(getContext(), String.format("%s - %d", ((TextView) view).getText(), position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPopupMenu(View view) {
//        TextView textView = view.findViewById(R.id.textViewNotesFragment);
//        textView.setOnClickListener(v -> {
//            Activity activity = requireActivity();
//            PopupMenu popupMenu = new PopupMenu(activity, v);
//            activity.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
//            popupMenu.setOnMenuItemClickListener(item -> {
//                int id = item.getItemId();
//                switch (id) {
//                    case R.id.popupMenuAdd:
//                        Toast.makeText(getContext(), "Добавить заметку", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.popupMenuEdit:
//                        Toast.makeText(getContext(), "Изменить заметку", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.popupMenuDelete:
//                        Toast.makeText(getContext(), "Удалить заметку", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.popupMenuUpdate:
//                        Toast.makeText(getContext(), "Обновить список", Toast.LENGTH_SHORT).show();
//                        return true;
//                }
//                return true;
//            });
//            popupMenu.show();
//        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    private boolean onItemSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.notesFragmentMenuAdd:
                navigation.showFragment(NoteFragment.newInstance(), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateNoteData(NoteData noteData) {
                        data.addNoteData(noteData);
                        adapter.notifyItemInserted(data.size() - 1);
                        moveToFirstPosition = true;
                    }
                });
                return true;
            case R.id.notesFragmentMenuClear:
                data.clearNoteData();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.noteMenuUpdate:
                int updatePosition = adapter.getMenuPosition();
                navigation.showFragment(NoteFragment.newInstance(data.getNoteData(updatePosition)), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateNoteData(NoteData noteData) {
                        data.updateNoteData(updatePosition, noteData);
                        adapter.notifyItemChanged(updatePosition);
                    }
                });
                return true;
            case R.id.noteMenuDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(R.string.alertDialogTitle)
                        .setMessage(R.string.alertDialogVessage)
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setCancelable(true)
                        .setNegativeButton(R.string.alertDialogNegativeButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton(R.string.alertDialogPositiveButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int deletePosition = adapter.getMenuPosition();
                                data.deleteNoteData(deletePosition);
                                adapter.notifyItemRemoved(deletePosition);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.notesFragmentMenuExit:
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        navigation.showFragment(StartFragment.newInstance(), false);
                    }
                });
                return true;
        }
        return false;
    }
}