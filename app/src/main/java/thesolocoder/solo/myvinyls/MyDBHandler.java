package thesolocoder.solo.myvinyls;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "records.db";
    public static final String TABLE_RECORDS = "records";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_BANDNAME = "bandname";
    private static final String COLUMN_ALBUMNAME = "albumname";
    private static final String COLUMN_RELEASEYEAR = "releaseyear";
    private static final String COLUMN_GENRE = "genre";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     //   String query = "CREATE TABLE " + TABLE_RECORDS + "(" +
      //  COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
       //  COLUMN_BANDNAME + " TEXT " +
        // ");";
        String query = "CREATE TABLE " + TABLE_RECORDS + "(" + COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_ALBUMNAME + " TEXT, " +
                        COLUMN_BANDNAME + " TEXT, " +
                        COLUMN_RELEASEYEAR + " INTEGER, " +
                        COLUMN_GENRE + " TEXT" + ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_RECORDS);
        onCreate(db);
    }

    // Add new row to the database
    public void addRecord(Records record){
        ContentValues values = new ContentValues();
      /*  values.put(COLUMN_BANDNAME, record.get_bandname());
        values.put(COLUMN_ALBUMNAME, record.get_albumname());
        values.put(COLUMN_RELEASEYEAR, record.get_releaseyear());
        values.put(COLUMN_GENRE, record.get_genre());*/

        values.put(COLUMN_BANDNAME, "Jay-z");
        values.put(COLUMN_ALBUMNAME, "The black album");
        values.put(COLUMN_RELEASEYEAR, "2001");
        values.put(COLUMN_GENRE, "RAP");

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_RECORDS, null, values);
        db.close();
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
                dbString += c.getString(c.getColumnIndex("genre"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }
}
