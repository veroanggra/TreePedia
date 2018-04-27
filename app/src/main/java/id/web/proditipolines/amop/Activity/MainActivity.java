package id.web.proditipolines.amop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;

import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.fragment.ArtikelFragment;
import id.web.proditipolines.amop.fragment.DataPohonFragment;
import id.web.proditipolines.amop.fragment.GmapFragment;
import id.web.proditipolines.amop.fragment.HistoryPohonFragment;
import id.web.proditipolines.amop.fragment.HomeFragment;
import id.web.proditipolines.amop.util.Helper;

public class MainActivity extends AppCompatActivity {

    Helper help;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView imgProfile;
    private TextView txtName;
    private Toolbar toolbar;

    public static int navItemIndex = 0;

    private static final String TAG_HOME = "home";
    private static final String TAG_DATAPOHON = "datapohon";
    private static final String TAG_HISTORYPOHON = "historypohon";
    private static final String TAG_PETA = "peta";
    private static final String TAG_ARTIKEL = "artikel";
    public static String CURRENT_TAG = TAG_HOME;

    //Judul pada toolbar saat berpindah menu
    private String[] activityTitles;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        help = new Helper(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //Navigation view header
        View navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

//        username = getIntent().getStringExtra(TAG_USERNAME);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        loadNavHeader();

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void loadNavHeader() {

        //name
        help = new Helper(getApplicationContext());
        HashMap<String, String> user = help.getUserDetail();
        String username = user.get(Helper.USERNAME);
        txtName.setText(String.format("Username : %s", username));

        //loading profile image
        Glide.with(this).load(R.drawable.ic_logo)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        //showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    private void loadHomeFragment() {
        //selecting appropriate nav menu item
        selectNavMenu();

        //set toolbar title
        setToolbarTitle();

        //if user select the current navigation menu again don't do anything
        //just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        //Sometimes, when fragment has huge data, screen same hanging
        //when swtiching between navigation menus
        // so using runnable, the fragment is loaded with cross fade effect

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                //update the main content by replacing fragments

                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        mHandler.post(mPendingRunnable);


        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                return new HomeFragment();
            case 1:
                return new DataPohonFragment();
            case 2:
                return new HistoryPohonFragment();
            case 3:
                return new GmapFragment();
            case 4:
                return new ArtikelFragment();
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_datapohon:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_DATAPOHON;
                        break;
                    case R.id.nav_historypohon:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_HISTORYPOHON;
                        break;
                    case R.id.nav_peta:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_PETA;
                        break;

                    case R.id.nav_artikel:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_ARTIKEL;
                        break;

                    default:
                        navItemIndex = 0;

                }
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;

            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (navItemIndex != 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            help.logoutUser();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
