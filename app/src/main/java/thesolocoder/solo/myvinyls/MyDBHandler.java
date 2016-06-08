package thesolocoder.solo.myvinyls;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
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

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String queryTableRecords = "CREATE TABLE records (_id INTEGER PRIMARY KEY, albumname TEXT, bandname TEXT, releaseyear INTEGER, hasimage TEXT);";
        db.execSQL(queryTableRecords);
        final String queryTableGenres = "CREATE TABLE recordsgenres (_id INTEGER PRIMARY KEY, album_id INTEGER, genre TEXT, subgenre TEXT);";
        db.execSQL(queryTableGenres);
        final String queryTableWishlist = "CREATE TABLE wishlist (_id INTEGER PRIMARY KEY, albumname TEXT, bandname TEXT, releaseyear INTEGER, hasimage TEXT);";
        db.execSQL(queryTableWishlist);
        final String queryTableLentout = "CREATE TABLE lentout (_id INTEGER PRIMARY KEY, album_id INTEGER, lentout TEXT, dateout TEXT, dueback TEXT);";
        db.execSQL(queryTableLentout);
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
        db.execSQL("DROP TABLE IF EXISTS records");
        db.execSQL("DROP TABLE IF EXISTS genres");
        db.execSQL("DROP TABLE IF EXISTS wishlist");
        db.execSQL("DROP TABLE IF EXISTS lentout");
        onCreate(db);
    }

    public String addRecord(Records record, String recordTableName, String genreTableName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BANDNAME, record.get_bandname());
        values.put(COLUMN_RELEASEYEAR, record.get_releaseyear());
        values.put(COLUMN_ALBUMNAME, record.get_albumname());
        values.put(COLUMN_HASIMAGE, record.get_hasimage());
        String album_id = "-1";
        SQLiteDatabase db = getWritableDatabase();
        db.insert(recordTableName, null, values);

        Cursor c = db.rawQuery("SELECT _id from " + recordTableName + " order by ROWID DESC limit 1", null);
        if (c.moveToFirst()) {
            album_id = c.getString(c.getColumnIndex("_id"));
            for (int i = 0; i < record.get_genre().size(); i += 2) {
                values = new ContentValues();
                values.put(COLUMN_ALBUMID, album_id);
                values.put(COLUMN_GENRE, record.get_genre().get(i));
                values.put("subgenre", record.get_genre().get(i + 1));
                db.insert(genreTableName, null, values);
            }
        }
        c.close();
        db.close();
        return album_id;
    }

    public ArrayList<Records> databaseToList(String query) {
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
                String genre_query = "SELECT * FROM " + TABLE_RECORDS + TABLE_GENRES + " WHERE " +
                        COLUMN_ALBUMID + "=" + c.getString(c.getColumnIndex("_id"));
                genre_cursor = db.rawQuery(genre_query, null);
                genre_cursor.moveToFirst();
                while (!genre_cursor.isAfterLast()) {
                    if (genre_cursor.getString(genre_cursor.getColumnIndex("genre")) != null) {
                        generes.add(genre_cursor.getString(genre_cursor.getColumnIndex("genre")));
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

    public Records getRecordByID(String id, String TABLE_NAME) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_RECORDS + " WHERE " + COLUMN_ID + "=" + id;
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

            String genre_query = "SELECT * FROM " + TABLE_NAME + TABLE_GENRES + " WHERE " +
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

    public ArrayList<GenreListItem> getGenres(String callingTable) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT DISTINCT " + COLUMN_GENRE + " FROM " + callingTable + TABLE_GENRES + " WHERE album_id!='-1'";
        ArrayList<GenreListItem> genres = new ArrayList<>();
        GenreListItem item;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            item = new GenreListItem();
            if (c.getString(c.getColumnIndex("genre")) != null) {
                item.genre = c.getString(c.getColumnIndex("genre"));
                item = getImagesForGenreItem(item, true, db, item.genre);
                if (!c.isLast() || (c.getString(c.getColumnIndex("genre")) != null)) {
                    c.moveToNext();
                    if (!c.isAfterLast()) {
                        item.genreRight = c.getString(c.getColumnIndex("genre"));
                        item = getImagesForGenreItem(item, false, db, item.genreRight);
                    }
                }
            }
            genres.add(item);
            c.moveToNext();
        }
        c.close();
        db.close();
        return genres;
    }

    private GenreListItem getImagesForGenreItem(GenreListItem item, boolean isLeftSide, SQLiteDatabase db, String genre) {
        final String MY_QUERY = "SELECT * FROM records INNER JOIN recordsgenres ON  records._id=recordsgenres.album_id WHERE  records.hasimage='true' AND genre='" + genre + "'";
        Cursor imageCursor = db.rawQuery(MY_QUERY, null);
        imageCursor.moveToFirst();
        while (!imageCursor.isAfterLast() && (item.albumArt.size() < 3 || item.albumArtRight.size() < 3)) {
            if (isLeftSide)
                item.albumArt.add(imageCursor.getString(imageCursor.getColumnIndex("_id")));
            else
                item.albumArtRight.add(imageCursor.getString(imageCursor.getColumnIndex("_id")));
            imageCursor.moveToNext();
        }
        imageCursor.close();
        return item;
    }

    public void updateRecord(Records record) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("UPDATE " + TABLE_RECORDS + " SET " + COLUMN_BANDNAME + "='" + record.get_bandname() + "', " + COLUMN_ALBUMNAME + "='" + record.get_albumname() + "'," + COLUMN_RELEASEYEAR + "='" + record.get_releaseyear() + "' WHERE _id='" + record.get_id() + "'", null);
        c.moveToFirst();
        c.close();
        db.close();
    }

    //Delete a record from the database
    public void deleteRecord(String _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_RECORDS + " WHERE " + COLUMN_ID + "=\"" + _id + "\";");
        db.execSQL("DELETE FROM " +"recordsgenres WHERE album_id=\"" + _id + "\";");
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
                int[] date = getdate(dateString);
                Date calDate = new GregorianCalendar(date[2], date[1], date[0]).getTime();
                item.dueBack.setTime(calDate);

                dateString = c.getString(c.getColumnIndex("dateout"));
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
}
