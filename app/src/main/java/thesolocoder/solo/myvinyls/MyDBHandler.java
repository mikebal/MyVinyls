package thesolocoder.solo.myvinyls;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "records.db";
    private static final String TABLE_RECORDS = "records";
    private static final String TABLE_GENRES = "genres";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_BANDNAME = "bandname";
    private static final String COLUMN_ALBUMNAME = "albumname";
    private static final String COLUMN_RELEASEYEAR = "releaseyear";
    private static final String COLUMN_ALBUMID ="album_id";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_URL ="imageurl";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_RECORDS + "(" + COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_ALBUMNAME + " TEXT, " +
                        COLUMN_BANDNAME + " TEXT, " +
                        COLUMN_RELEASEYEAR + " INTEGER, " +
                        COLUMN_URL +" TEXT" + ");";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_GENRES + "(" + COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ALBUMID + " INTEGER,"+
                COLUMN_GENRE + " TEXT" + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_GENRES);
        onCreate(db);
    }

    public String addRecord(Records record){
        ContentValues values = new ContentValues();
        values.put(COLUMN_BANDNAME, record.get_bandname());
        values.put(COLUMN_RELEASEYEAR, record.get_releaseyear());
        values.put(COLUMN_ALBUMNAME, record.get_albumname());
        String album_id = "-1";

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_RECORDS, null, values);

        Cursor c = db.rawQuery("SELECT _id from " + TABLE_RECORDS + " order by ROWID DESC limit 1", null);
        if (c.moveToFirst()) {
            album_id = c.getString(c.getColumnIndex("_id"));
            if(!album_id.equals("-1")) {
                values = new ContentValues();
                values.put(COLUMN_ALBUMID, album_id);
                values.put(COLUMN_GENRE, "TEST");
                db.insert(TABLE_GENRES, null, values);
                values.put(COLUMN_GENRE, "TEST2");
                db.insert(TABLE_GENRES, null, values);
            }
        }
        c.close();
        db.close();
        return album_id;
    }

    //Delete a record from the database
    public void deleteRecord(String albumName)
    {
        albumName = "The black album";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_RECORDS + " WHERE " + COLUMN_ALBUMNAME + "=\"" + albumName + "\";");
    }

    //Print out database as a string
    public  String databseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_RECORDS + " WHERE 1";

        //Cursor point to a location in your result
        Cursor c = db.rawQuery(query, null);
        Cursor genre_cursor;
        // Move to the first row in your result
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("bandname")) != null){
                dbString += c.getString(c.getColumnIndex("bandname"));
                dbString += "\n";
                dbString += c.getString(c.getColumnIndex("albumname"));
                dbString += "\n";
                dbString += c.getString(c.getColumnIndex("releaseyear"));
                dbString += "\n";

                String genre_querey = "SELECT * FROM " + TABLE_GENRES + " WHERE " +
                                COLUMN_ALBUMID +"=" + c.getString(c.getColumnIndex("_id"));
                genre_cursor = db.rawQuery(genre_querey, null);
                genre_cursor.moveToFirst();
                while (!genre_cursor.isAfterLast()){
                    if(genre_cursor.getString(genre_cursor.getColumnIndex("genre")) != null){
                        dbString += genre_cursor.getString(genre_cursor.getColumnIndex("genre"));
                        dbString += "\n";
                    }
                    genre_cursor.moveToNext();
                }
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }
    public ArrayList<Records> databaseToList(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_RECORDS + " WHERE 1";
        ArrayList<Records> recordList = new ArrayList<>();
        ArrayList<String> generes = new ArrayList<>();
        Records listEntry;
        //Cursor point to a location in your result
        Cursor c = db.rawQuery(query, null);
        Cursor genre_cursor;
        // Move to the first row in your result
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("bandname")) != null){
                listEntry = new Records();
                generes = new ArrayList<>();
                listEntry.set_bandname(c.getString(c.getColumnIndex("bandname")));
                listEntry.set_albumname(c.getString(c.getColumnIndex("albumname")));
                listEntry.set_releaseyear(c.getString(c.getColumnIndex("releaseyear")));
                listEntry.set_imageurl(c.getString(c.getColumnIndex("_id")));

                String genre_querey = "SELECT * FROM " + TABLE_GENRES + " WHERE " +
                        COLUMN_ALBUMID +"=" + c.getString(c.getColumnIndex("_id"));
                genre_cursor = db.rawQuery(genre_querey, null);
                genre_cursor.moveToFirst();
                while (!genre_cursor.isAfterLast()){
                    if(genre_cursor.getString(genre_cursor.getColumnIndex("genre")) != null){
                        generes.add(genre_cursor.getString(genre_cursor.getColumnIndex("genre")));
                    }
                    genre_cursor.moveToNext();
                }
                listEntry.set_genre(generes);
                recordList.add(listEntry);
            }
            c.moveToNext();
        }
        db.close();
        return recordList;
    }

    public Records getRecordByID(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_RECORDS + " WHERE " + COLUMN_ID + "=" + id;
        Records selectedRecord = new Records();
        ArrayList<String> generes = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        Cursor genre_cursor;
        c.moveToFirst();
        if (c.getString(c.getColumnIndex("bandname")) != null) {
            selectedRecord.set_bandname(c.getString(c.getColumnIndex("bandname")));
            selectedRecord.set_albumname(c.getString(c.getColumnIndex("albumname")));
            selectedRecord.set_releaseyear(c.getString(c.getColumnIndex("releaseyear")));
            selectedRecord.set_imageurl(c.getString(c.getColumnIndex("_id")));

            String genre_querey = "SELECT * FROM " + TABLE_GENRES + " WHERE " +
                    COLUMN_ALBUMID + "=" + c.getString(c.getColumnIndex("_id"));
            genre_cursor = db.rawQuery(genre_querey, null);
            genre_cursor.moveToFirst();
            while (!genre_cursor.isAfterLast()) {
                if (genre_cursor.getString(genre_cursor.getColumnIndex("genre")) != null) {
                    generes.add(genre_cursor.getString(genre_cursor.getColumnIndex("genre")));
                }
                genre_cursor.moveToNext();
            }
            selectedRecord.set_genre(generes);
        }
        c.moveToNext();
        db.close();

        return selectedRecord;
    }

    public void updateRecord(Records record){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c=  db.rawQuery("UPDATE " + TABLE_RECORDS + " SET " + COLUMN_BANDNAME + "='" +record.get_bandname() +
                "', " + COLUMN_ALBUMNAME + "='" + record.get_albumname() +
                "'," + COLUMN_RELEASEYEAR +"='" + record.get_releaseyear() +
                "' WHERE _id='" + record.get_id() +"'", null);
        c.moveToFirst();
        c.close();
        db.close();
    }
}
