package tfg.dam.socialsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tfg.dam.socialsports.fragments.home.Home;
import tfg.dam.socialsports.fragments.myevents.MyEvents;
import tfg.dam.socialsports.fragments.newevent.NewEvent;
import tfg.dam.socialsports.fragments.searchevent.SearchEvent;
import tfg.dam.socialsports.fragments.userconfig.UserConfig;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private Home home;
    private NewEvent newEvent;
    private SearchEvent searchEvent;
    private MyEvents myEvents;
    private UserConfig userConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.menu_nav_main);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.menu_home):
                        home = new Home();
                        Utils.showSelectedFragment(R.id.container,getSupportFragmentManager(),home);
                        break;
                    case (R.id.menu_new_event):
                        newEvent = new NewEvent();
                        Utils.showSelectedFragment(R.id.container,getSupportFragmentManager(),newEvent);
                        break;
                    case (R.id.menu_search_event):
                        searchEvent = new SearchEvent();
                        Utils.showSelectedFragment(R.id.container,getSupportFragmentManager(),searchEvent);
                        break;
                    case (R.id.menu_my_events):
                        myEvents = new MyEvents();
                        Utils.showSelectedFragment(R.id.container,getSupportFragmentManager(),myEvents);
                        break;
                    case (R.id.menu_user):
                        userConfig = new UserConfig();
                        Utils.showSelectedFragment(R.id.container,getSupportFragmentManager(),userConfig);
                        break;
                }
                return true;
            }
        });

        if (LoginActivity.user.getFirstName() == null || LoginActivity.user.getBirthday() == null) {
            navigationView.setSelectedItemId(R.id.menu_user);
        }
        else {
            navigationView.setSelectedItemId(R.id.menu_home);
        }
    }
}
