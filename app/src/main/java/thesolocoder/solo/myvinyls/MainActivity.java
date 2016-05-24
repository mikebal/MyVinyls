package thesolocoder.solo.myvinyls;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

    MyDBHandler dbHandler;
    ListView  recordDisplayList;
    ListViewAdapterMain customAdapter;
    ListViewAdapterGenre customAdapterGenre;
    String databaseTable = "records";
    EditText inputSearch;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        populateArrayList("SELECT * FROM records ORDER BY albumname;");
        underlineAlbum();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animRotate);
                if(databaseTable.equals("lentout"))
                {
                    Intent open_addLentOut = new Intent(MainActivity.this, AddLentOut.class);
                    startActivity(open_addLentOut);
                }
                else {
                    Intent open_AddRecords = new Intent(MainActivity.this, AddRecord.class);
                    open_AddRecords.putExtra("toEditID", String.valueOf("New Entry"));
                    open_AddRecords.putExtra("toAddToTable", String.valueOf(databaseTable));
                    startActivity(open_AddRecords);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        inputSearch = (EditText) findViewById(R.id.editTextSearchBox);
        setListClickListener();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_collection) {
            databaseTable = "records";
            reloadListView();

        } else if (id == R.id.nav_wishlist) {
            databaseTable = "wishlist";
            reloadListView();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_settings) {

        } else if(id == R.id.nav_lentout)
        {
            databaseTable = "lentout";
            reloadListView();
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

    private void populateArrayList(String dbCall)
    {
        ArrayList<Records> recordList;
        if(dbCall.equals("GENRES"))
        {
            ArrayList<GenreListItem> genreList;
            genreList = dbHandler.getGenres(databaseTable);
            populateGenreList(genreList);
        }
        else {
            recordList = dbHandler.databaseToList(dbCall);
            populateList(recordList);
        }

    }
    private void populateGenreList(ArrayList<GenreListItem> genreList)
    {
        customAdapter = null;
        recordDisplayList = (ListView) findViewById(R.id.listViewMainDisplay);
        customAdapterGenre = new ListViewAdapterGenre(this, genreList);
        recordDisplayList.setAdapter(customAdapterGenre);
        customAdapterGenre.notifyDataSetChanged();
        recordDisplayList.setVerticalScrollBarEnabled(false);
    }

   private void populateList(ArrayList<Records> recordList) {
        recordDisplayList = (ListView) findViewById(R.id.listViewMainDisplay);
       if(!databaseTable.equals("lentout"))
            customAdapter = new ListViewAdapterMain(this, recordList, null);
       else{
           ArrayList<LentOut> lentOutList = dbHandler.getLentOut();
           customAdapter = new ListViewAdapterMain(this, recordList, lentOutList);
       }
        recordDisplayList.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }

    private void setListClickListener(){
        recordDisplayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Records selected = (Records) adapter.getItemAtPosition(position);
               // Intent open_EditRecords = new Intent(MainActivity.this, AddRecord.class);
                Intent open_EditRecords = new Intent(MainActivity.this, LendoreditPopup.class);
                open_EditRecords.putExtra("toEditID", String.valueOf(selected.get_imageurl()));
                startActivity(open_EditRecords);
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (arg3 == 0) {
                    populateArrayList("SELECT * FROM records ORDER BY albumname;");
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

        artist.setPaintFlags(0);
        album.setPaintFlags(0);
        genres.setPaintFlags(0);

        if(databaseTable.equals("lentout"))
        {
            populateArrayList("SELECT * FROM records INNER JOIN lentout ON  records._id=lentout.album_id ORDER BY album_id");
        }
        else if (v.getId() == artist.getId()) {
            artist.setPaintFlags(artist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            populateArrayList("SELECT * FROM "+ databaseTable +" ORDER BY bandname");
        } else if (v.getId() == album.getId()) {
            album.setPaintFlags(album.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            populateArrayList("SELECT * FROM "+ databaseTable +" ORDER BY albumname;");
        } else {
            genres.setPaintFlags(genres.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            populateArrayList("GENRES");
        }
    }

    private void underlineAlbum()
    {
        Button album = (Button) findViewById(R.id.buttonAlbums);
        album.setPaintFlags(album.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}