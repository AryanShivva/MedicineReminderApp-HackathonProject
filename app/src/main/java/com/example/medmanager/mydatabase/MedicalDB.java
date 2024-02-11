package com.example.medmanager.mydatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;



// Special class used for SQLite database operations
public class MedicalDB extends SQLiteOpenHelper {
    // Creating an example for the Singleton design pattern
    public static MedicalDB sInstance;

    // Singleton design pattern: Method that returns a single instance
    public static synchronized MedicalDB getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MedicalDB(context.getApplicationContext());
        }
        return sInstance;
    }

    // Database version number
    public static final int version = 1;
    //Constructor method of MedicalDB class
    public MedicalDB(Context context) {
        super(context, "database", null, version);
    }
    // Method called when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL query that creates the MEDICINE table
        String create_med_table = "CREATE TABLE MEDICINE (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MED_NAME TEXT NOT NULL," +
                "QTY INTEGER NOT NULL," +
                "DATE_TIME TEXT NOT NULL," +
                "DAYS TEXT NOT NULL," +
                "ENABLE INT NOT NULL);";
        // Create tables by running SQL queries
        db.execSQL(create_med_table);
    }

    // Method called when the database version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       // Update operations are added to this method, but left blank in this example
    }

    // Medicine (MEDICINE) CRUD operations
    //Method to add medicine
    public void addMedicine(SQLiteDatabase db, @NonNull String med_name, @NonNull int quantity, @NonNull String date_time, @NonNull String days) {
        ContentValues values = new ContentValues();
        values.put("MED_NAME", med_name);
        values.put("QTY", quantity);
        values.put("DATE_TIME", date_time);
        values.put("DAYS", days);
        values.put("ENABLE", false);

        db.insert("MEDICINE", null, values);
    }

    //Method to delete medication
    public void deleteMedicine(SQLiteDatabase db, @NonNull int med_id) {
        db.delete("MEDICINE", "_id=?", new String[]{"" + med_id});
    }

    //Method to update drug effectiveness
    public void setEnable(SQLiteDatabase db, int id, int b) {
        ContentValues values = new ContentValues();
        values.put("ENABLE", b);
        db.update("MEDICINE", values, "_id=?", new String[]{"" + id});
    }

    //Method to retrieve the medication list of a specific user
    public Cursor getAllMedicine(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM MEDICINE;", new String[]{});
    }

    //Method to get information about a specific drug
    public Cursor getMedicine(SQLiteDatabase db, int med_id) {
        return db.rawQuery("SELECT * FROM MEDICINE WHERE _id=" + med_id + ";", new String[]{});
    }
}
