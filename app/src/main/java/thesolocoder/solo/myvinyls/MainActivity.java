package thesolocoder.solo.myvinyls;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView  recordDisplayList;
    ListViewAdapterMain customAdapter;
    String databaseTable = "records";
    EditText inputSearch;
    MenuItem menuItem;
    View lastClickedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        setupDefaultAppearance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animRotate);
                Intent open_AddRecords = new Intent(MainActivity.this, AddRecord.class);
                open_AddRecords.putExtra("toAddToTable", String.valueOf(databaseTable));
                startActivity(open_AddRecords);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer == null)
            return;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);

        inputSearch = (EditText) findViewById(R.id.editTextSearchBox);
        setListClickListener();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        if (id == R.id.nav_my_collection) {
            databaseTable = "records";
            customAdapter.isOnLendoutScreen = false;
            reloadListView();
            getSupportActionBar().setTitle("My Collection");
            changeActionBarColor("#1c2f2f");

        } else if (id == R.id.nav_wishlist) {
            databaseTable = "wishlist";
            customAdapter.isOnLendoutScreen = false;
            reloadListView();
            getSupportActionBar().setTitle("Wishlist");
            changeActionBarColor("#2f1c2f");


        } else if (id == R.id.nav_backup) {
            Intent open_BackupMenu = new Intent(MainActivity.this, BackupRestore.class);
            startActivity(open_BackupMenu);
        } else if(id == R.id.nav_lentout)
        {
            databaseTable = "lentout";
            reloadListView();
            fab.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Lentout");
            changeActionBarColor("#2f2f1c");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuItem=menu.add("Title"); //your desired title here
        menuItem.setIcon(R.mipmap.ic_search_white_48dp); //your desired icon here
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        tabSearchAnimationHandler();
        return super.onOptionsItemSelected(item);
    }
    private void tabSearchAnimationHandler(){
        final LinearLayout tabBar = (LinearLayout) findViewById(R.id.linearLayout);
        final LinearLayout searchArea = (LinearLayout) findViewById(R.id.linearLayoutSearchBar);

        if(tabBar.getVisibility() == View.GONE) {
            searchBarAnimation(searchArea, tabBar);
            menuItem.setIcon(R.mipmap.ic_search_white_48dp);
        }
        else {
            searchBarAnimation(tabBar, searchArea);
            menuItem.setIcon(R.mipmap.ic_clear_white_36dp);
        }
    }

    private void searchBarAnimation(final LinearLayout fadeoutElement, final LinearLayout fadeInElement){

        fadeoutElement.setAlpha(1);
        fadeoutElement.animate().setDuration(1000).alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fadeoutElement.setVisibility(View.GONE);
                fadeInElement.setVisibility(View.VISIBLE);
                fadeInElement.setAlpha(0);
                fadeInElement.animate().setDuration(1000).alpha(1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fadeInElement.setVisibility(View.VISIBLE);
                        }
                    });
                }
        });
    }

    private void populateArrayList(String dbCall, boolean artistMode)
    {
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        ArrayList<Records> recordList;
        if(dbCall.equals("GENRES"))
        {
            recordList = dbHandler.getGenres(databaseTable);
            populateGenreList(recordList);
        }
        else {
            recordList = dbHandler.databaseToList(dbCall, databaseTable);
            populateList(recordList, artistMode);
        }
        dbHandler.close();

    }
    private void populateGenreList(ArrayList<Records> recordList)
    {
        recordDisplayList = (ListView) findViewById(R.id.listViewMainDisplay);

        customAdapter = new ListViewAdapterMain(this, recordList, null);
        customAdapter.artistView = true;
        recordDisplayList.setAdapter(customAdapter);
        recordDisplayList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Records selectedRecord = customAdapter.getItem(position);
                String selectedBandName = selectedRecord.get_bandname();
                populateArrayList("SELECT * FROM records INNER JOIN recordsgenres ON records._id=recordsgenres.album_id WHERE recordsgenres.genre='" + selectedBandName + "' ORDER BY records._id;", false);
            }

        });

        customAdapter.notifyDataSetChanged();
    }

   private void populateList(ArrayList<Records> recordList, boolean artistMode) {
       final MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        recordDisplayList = (ListView) findViewById(R.id.listViewMainDisplay);

       if(!databaseTable.equals("lentout")) {
           customAdapter = new ListViewAdapterMain(this, recordList, null);
           customAdapter.artistView = artistMode;
           customAdapter.callingTable = databaseTable;
       }

       else{
           ArrayList<LentOut> lentOutList = dbHandler.getLentOut("SELECT * FROM lentout ORDER BY album_id");
           customAdapter = new ListViewAdapterMain(this, recordList, lentOutList);
           customAdapter.setIsOnLendOutScreen(true);
           customAdapter.callingTable = databaseTable;
       }

       recordDisplayList.setAdapter(customAdapter);
       recordDisplayList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
           @Override

           public void onItemClick(AdapterView<?> parent, View view, int position, long id){

               Records selectedRecord = customAdapter.getItem(position);
               String selectedBandName = selectedRecord.get_bandname();
               populateArrayList("SELECT * FROM "+ databaseTable +" WHERE bandname='" + selectedBandName +"';", false);
           }

       });
        customAdapter.notifyDataSetChanged();
        dbHandler.close();
    }

    private void setListClickListener(){
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (arg3 == 0) {
                    populateArrayList("SELECT * FROM records ORDER BY albumname;", false);
                }
                MainActivity.this.customAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

    }
    public void reloadListView()
    {
        Button artist = (Button) findViewById(R.id.buttonArtists);
        Button album = (Button) findViewById(R.id.buttonAlbums);
        Button genres = (Button) findViewById(R.id.buttonGeneres);
        Button lastClicked = null;

        if(album == null || artist == null || genres == null)
            return;

        if(artist.getPaintFlags() != 0)
            lastClicked = artist;
        else if(album.getPaintFlags() != 0)
            lastClicked = album;
        else if(genres.getPaintFlags() != 0)
            lastClicked = genres;

        if(lastClicked == null) {
            lastClicked = album;
            album.setPaintFlags(album.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        tabButtonClicked(lastClicked);
    }
    public void tabButtonClicked(View v) {
        Button artist = (Button) findViewById(R.id.buttonArtists);
        Button album = (Button) findViewById(R.id.buttonAlbums);
        Button genres = (Button) findViewById(R.id.buttonGeneres);

        if(album == null || artist == null || genres == null)
            return;

        artist.setPaintFlags(0);
        album.setPaintFlags(0);
        genres.setPaintFlags(0);
        lastClickedButton = v;

        if(databaseTable.equals("lentout"))
        {
            populateArrayList("SELECT * FROM records INNER JOIN lentout ON  records._id=lentout.album_id ORDER BY album_id", false);
        }
        else if (v.getId() == artist.getId()) {
            artist.setPaintFlags(artist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            populateArrayList("SELECT * FROM "+ databaseTable +" GROUP BY bandname", true);
        } else if (v.getId() == album.getId()) {
            album.setPaintFlags(album.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            populateArrayList("SELECT * FROM "+ databaseTable +" ORDER BY albumname;", false);
        } else {
            genres.setPaintFlags(genres.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            populateArrayList("GENRES", false);
        }
    }

    private void setupDefaultAppearance()
    {
        Button album = (Button) findViewById(R.id.buttonAlbums);
        if(album != null) {
            album.setPaintFlags(album.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            lastClickedButton = album;
            tabButtonClicked(lastClickedButton);
            getSupportActionBar().setTitle("My Collection");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(lastClickedButton != null)
            tabButtonClicked(lastClickedButton);
    }
    private void changeActionBarColor(String color){
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);

        LinearLayout tabBar = (LinearLayout)findViewById(R.id.linearLayout);
        tabBar.setBackgroundColor(Color.parseColor(color));
    }
}