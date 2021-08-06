package ru.geekbrains.mymenuauth;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import ru.geekbrains.mymenuauth.observe.Publisher;
import ru.geekbrains.mymenuauth.ui.NotesFragment;

public class MainActivity extends AppCompatActivity {

    private Publisher publisher = new Publisher();
    private Navigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = new Navigation(getSupportFragmentManager());
        getNavigation().showFragment(StartFragment.newInstance(), false);
        initView();
    }

    private void initView() {
        Toolbar toolbar = initToolbar();
//        initDrawer(toolbar);
//        initButtonNotes();
//        initButtonAdd();
//        initButtonSettings();
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.mainDrawerSettings:
                        showFragment(new SettingsFragment());
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.mainDrawerAbout:
                        showFragment(new AboutFragment());
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.mainMenuSearch);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        MenuItem settings = menu.findItem(R.id.mainMenuSettings);
        settings.setOnMenuItemClickListener(item -> {
            showFragment(new SettingsFragment());
            return true;
        });

        MenuItem about = menu.findItem(R.id.mainMenuAbout);
        about.setOnMenuItemClickListener(item -> {
            showFragment(new AboutFragment());
            return true;
        });

        return true;
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarMainActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        return toolbar;
    }

//    private void initButtonSettings() {
//        AppCompatButton button = findViewById(R.id.buttonMainActivitySettings);
//        button.setOnClickListener(v -> showFragment(new SettingsFragment()));
//    }
//
//    private void initButtonAdd() {
//        AppCompatButton button = findViewById(R.id.buttonMainActivityAddNote);
//        button.setOnClickListener(v -> showFragment(new AddFragment()));
//    }
//
//    private void initButtonNotes() {
//        AppCompatButton button = findViewById(R.id.buttonMainActivityNotes);
//        button.setOnClickListener(v -> showFragment(new NotesFragment()));
//    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}