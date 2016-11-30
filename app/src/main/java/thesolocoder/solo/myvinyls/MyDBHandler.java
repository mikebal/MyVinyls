package thesolocoder.solo.myvinyls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "records.db";
    private static final String TABLE_RECORDS = "records";
    private static final String TABLE_GENRES = "genres";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_BANDNAME = "bandname";
    private static final String COLUMN_ALBUMNAME = "albumname";
    private static final String COLUMN_RELEASEYEAR = "releaseyear";
    private static final String COLUMN_ALBUMID = "album_id";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_HASIMAGE = "hasimage";
    private static final String COLUMN_NOTES = "notes";
    private Context appContext;

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String queryTableRecords = "CREATE TABLE records (_id INTEGER PRIMARY KEY, albumname TEXT, bandname TEXT, releaseyear INTEGER, hasimage TEXT, notes TEXT);";
        db.execSQL(queryTableRecords);
        final String queryTableGenres = "CREATE TABLE recordsgenres (_id INTEGER PRIMARY KEY, album_id INTEGER, genre TEXT, subgenre TEXT);";
        db.execSQL(queryTableGenres);
        final String queryTableWishlist = "CREATE TABLE wishlist (_id INTEGER PRIMARY KEY, albumname TEXT, bandname TEXT, releaseyear INTEGER, hasimage TEXT, notes TEXT);";
        db.execSQL(queryTableWishlist);
        final String queryTableLentout = "CREATE TABLE lentout (_id INTEGER PRIMARY KEY, album_id INTEGER, lentout TEXT, dateout TEXT, dueback TEXT);";
        db.execSQL(queryTableLentout);
        final String queryTableLentoutG = "CREATE TABLE lentoutgenres (_id INTEGER PRIMARY KEY, album_id INTEGER, lentout TEXT, dateout TEXT, dueback TEXT);";
        db.execSQL(queryTableLentoutG);
        final String queryTableWishlistGenre = "CREATE TABLE wishlistgenres (_id INTEGER PRIMARY KEY, album_id INTEGER, genre TEXT, subgenre TEXT);";
        db.execSQL(queryTableWishlistGenre);
        db.execSQL("insert into recordsgenres (_id,album_id,genre,subgenre) values (1,-1,'Classical','');");
        db.execSQL("insert into recordsgenres (_id,album_id,genre,subgenre) values (2,-1,'Electronica','');");
        db.execSQL("insert into recordsgenres (_id,album_id,genre,subgenre) values (3,-1,'Hip-Hop','');");
        db.execSQL("insert into recordsgenres (_id,album_id,genre,subgenre) values (4,-1,'Jazz','');");
        db.execSQL("insert into recordsgenres (_id,album_id,genre,subgenre) values (5,-1,'Latin','');");
        db.execSQL("insert into recordsgenres (_id,album_id,genre,subgenre) values (6,-1,'Metal','');");
        db.execSQL("insert into recordsgenres (_id,album_id,genre,subgenre) values (7,-1,'Pop','');");
        db.execSQL("insert into recordsgenres (_id,album_id,genre,subgenre) values (8,-1,'R&B','');");
        db.execSQL("insert into recordsgenres (_id,album_id,genre,subgenre) values (9,-1,'Rock','');");
        db.execSQL("insert into recordsgenres (_id,album_id,genre,subgenre) values (10,-1,'World','');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2)
        {
            db.execSQL("ALTER TABLE records ADD COLUMN notes TEXT");
        }
        if(oldVersion < 3)
        {
            db.execSQL("ALTER TABLE wishlist ADD COLUMN notes TEXT");
        }
    }

    public void dropAndRemake()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS records");
        db.execSQL("DROP TABLE IF EXISTS recordsgenres");
        db.execSQL("DROP TABLE IF EXISTS wishlist");
        db.execSQL("DROP TABLE IF EXISTS wishlistgenres");
        db.execSQL("DROP TABLE IF EXISTS lentout");
        db.execSQL("DROP TABLE IF EXISTS lentoutgenres");
        onCreate(db);
    }

    public String addRecord(Records record, String recordTableName, String genreTableName, boolean hasID) {
        String album_id = "-1";
        ContentValues values = new ContentValues();
        values.put(COLUMN_BANDNAME, record.get_bandname());
        values.put(COLUMN_RELEASEYEAR, record.get_releaseyear());
        values.put(COLUMN_ALBUMNAME, record.get_albumname());
        values.put(COLUMN_HASIMAGE, record.get_hasimage());
        values.put(COLUMN_NOTES, record.get_notes());
        if(hasID)
           values.put(COLUMN_ID, Integer.valueOf(record.get_imageurl()));

        SQLiteDatabase db = getWritableDatabase();
        db.insert(recordTableName, null, values);

        Cursor c = db.rawQuery("SELECT _id from " + recordTableName + " order by ROWID DESC limit 1", null);
        if (c.moveToFirst()) {
            album_id = c.getString(c.getColumnIndex("_id"));
            for (int i = 0; i < record.get_genre().size(); i ++) {
                values = new ContentValues();
                values.put(COLUMN_ALBUMID, album_id);
                values.put(COLUMN_GENRE, record.get_genre().get(i));
                db.insert(genreTableName, null, values);
            }
        }
        c.close();
        db.close();
        return album_id;
    }

    public ArrayList<Records> databaseToList(String query, String table) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Records> recordList = new ArrayList<>();
        ArrayList<String> generes;
        Records listEntry;
        Cursor c = db.rawQuery(query, null);
        Cursor genre_cursor;
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("bandname")) != null) {
                listEntry = new Records();
                generes = new ArrayList<>();
                listEntry.set_bandname(c.getString(c.getColumnIndex("bandname")));
                listEntry.set_albumname(c.getString(c.getColumnIndex("albumname")));
                listEntry.set_releaseyear(c.getString(c.getColumnIndex("releaseyear")));
                listEntry.set_imageurl(c.getString(c.getColumnIndex("_id")));
                listEntry.set_hasimage(c.getString(c.getColumnIndex("hasimage")));  // Might not need this in ListView object
                listEntry.set_notes(c.getString(c.getColumnIndex(COLUMN_NOTES)));
                String genre_query = "SELECT * FROM " + table + TABLE_GENRES + " WHERE " +
                        COLUMN_ALBUMID + "=" + c.getString(c.getColumnIndex("_id"));
                genre_cursor = db.rawQuery(genre_query, null);
                genre_cursor.moveToFirst();
                while (!genre_cursor.isAfterLast()) {
                    if (genre_cursor.getString(genre_cursor.getColumnIndex("genre")) != null) {
                        generes.add(genre_cursor.getString(genre_cursor.getColumnIndex("genre")));
                        generes.add(genre_cursor.getString(genre_cursor.getColumnIndex("subgenre")));
                    }
                    genre_cursor.moveToNext();
                }
                genre_cursor.close();
                listEntry.set_genre(generes);
                recordList.add(listEntry);
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return recordList;
    }

    public Records getRecordByID(String id, String callingTable) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + callingTable + " WHERE " + COLUMN_ID + "='" + id +"'";
        Records selectedRecord = new Records();
        ArrayList<String> genres = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        Cursor genre_cursor;
        c.moveToFirst();
        if (c.getString(c.getColumnIndex("bandname")) != null) {
            selectedRecord.set_bandname(c.getString(c.getColumnIndex("bandname")));
            selectedRecord.set_albumname(c.getString(c.getColumnIndex("albumname")));
            selectedRecord.set_releaseyear(c.getString(c.getColumnIndex("releaseyear")));
            selectedRecord.set_imageurl(c.getString(c.getColumnIndex("_id")));
            selectedRecord.set_notes(c.getString(c.getColumnIndex(COLUMN_NOTES)));
            String genre_query = "SELECT * FROM " + callingTable + TABLE_GENRES + " WHERE " +
                    COLUMN_ALBUMID + "=" + c.getString(c.getColumnIndex("_id"));
            genre_cursor = db.rawQuery(genre_query, null);
            genre_cursor.moveToFirst();
            while (!genre_cursor.isAfterLast()) {
                if (genre_cursor.getString(genre_cursor.getColumnIndex("genre")) != null) {
                    genres.add(genre_cursor.getString(genre_cursor.getColumnIndex("genre")));
                }
                genre_cursor.moveToNext();
            }
            genre_cursor.close();
            selectedRecord.set_genre(genres);
        }
        c.moveToNext();
        c.close();
        db.close();

        return selectedRecord;
    }

    public ArrayList<Records> getGenres(String callingTable) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT DISTINCT " + COLUMN_GENRE + " FROM " + callingTable + TABLE_GENRES + " WHERE album_id!='-1'";
        ArrayList<Records> genres = new ArrayList<>();
        Records item;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            item = new Records();
            if (c.getString(c.getColumnIndex("genre")) != null) {
                item.set_bandname(c.getString(c.getColumnIndex("genre")));
                item = getImagesForGenreItem(item, true, db, item.get_albumname());

            }
            genres.add(item);
            c.moveToNext();
        }
        c.close();
        db.close();
        return genres;
    }

    private Records getImagesForGenreItem(Records item, boolean isLeftSide, SQLiteDatabase db, String genre) {
        final String MY_QUERY = "SELECT * FROM records INNER JOIN recordsgenres ON  records._id=recordsgenres.album_id WHERE  records.hasimage='true' AND genre='" + genre + "'";
        Cursor imageCursor = db.rawQuery(MY_QUERY, null);
        imageCursor.moveToFirst();
        while (!imageCursor.isAfterLast()) {
            item.set_imageurl(imageCursor.getString(imageCursor.getColumnIndex("_id")));
        }
        imageCursor.close();
        return item;
    }

    public void updateRecord(Records record) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("UPDATE " + TABLE_RECORDS + " SET " + COLUMN_BANDNAME + "='" + record.get_bandname() + "', " + COLUMN_ALBUMNAME + "='" + record.get_albumname() + "'," + COLUMN_RELEASEYEAR + "='" + record.get_releaseyear() + "'," + COLUMN_NOTES + "='" + record.get_notes() + "' WHERE _id='" + record.get_id() + "'", null);
        c.moveToFirst();
        c.close();
        db.close();
    }

    //Delete a record from the database
    public void deleteRecord(String _id, String table) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + table + " WHERE " + COLUMN_ID + "=\"" + _id + "\";");
        db.execSQL("DELETE FROM " + table + "genres WHERE album_id=\"" + _id + "\";");
        db.execSQL("DELETE FROM " + "lentout WHERE album_id=\"" + _id + "\";");
    }

    public ArrayList<String> dbReturnListStrings(String query, String targetCol) {
        ArrayList<String> queryResponse = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            queryResponse.add(cursor.getString(cursor.getColumnIndex(targetCol)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return queryResponse;
    }

    public void runRawQueryNoResult(String query) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        c.close();
        db.close();
    }
    public String runRawQueryIfExists(String query, String target) {
        String result;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if(c.getCount() == 0)
            result = "-1";
        else
            result = c.getString(c.getColumnIndex(target));
        c.close();
        db.close();
        return result;
    }

    public ArrayList<LentOut> getLentOut(String query) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<LentOut> lentOutList = new ArrayList<>();
        LentOut item;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            item = new LentOut();
            if (c.getString(c.getColumnIndex("lentout")) != null) {
                item.name = c.getString(c.getColumnIndex("lentout"));
                item.id = c.getString(c.getColumnIndex("album_id"));
                String dateString = c.getString(c.getColumnIndex("dueback"));
                item.duebackStr = dateString;
                int[] date = getdate(dateString);
                Date calDate = new GregorianCalendar(date[2], date[1], date[0]).getTime();
                item.dueBack.setTime(calDate);

                dateString = c.getString(c.getColumnIndex("dateout"));
                item.dateOutStr = dateString;
                date = getdate(dateString);
                calDate = new GregorianCalendar(date[2], date[1], date[0]).getTime();
                item.lentout.setTime(calDate);
            }
            lentOutList.add(item);
            c.moveToNext();
        }
        c.close();
        db.close();
        return lentOutList;
    }
    private int[] getdate(String dateString)
    {
        int[] date = new int[3];
        int count = 0;
        StringTokenizer tokens = new StringTokenizer(dateString, "/");
        while(tokens.hasMoreTokens()) {
            String current = tokens.nextToken();
            date[count] = Integer.valueOf(current);
            count++;
        }
        return date;
    }

    public String getDBpath(){
        String path = appContext.getDatabasePath(DATABASE_NAME).getAbsolutePath().toString();
        SQLiteDatabase db = getWritableDatabase();
        return path;
    }
}
