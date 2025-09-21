package com.example.clinicaoasis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "System_GALY.db";
    private static final int DATABASE_VERSION = 6;

    // Tabla de Usuarios
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_IMAGE = "image";

    // Tabla de Dispositivos
    private static final String TABLE_DEVICES = "Devices";
    private static final String COLUMN_DEVICE_ID = "id";
    private static final String COLUMN_DEVICE_NAME = "name";
    private static final String COLUMN_DEVICE_LOCATION = "location";

    // Tabla de Habitaciones
    private static final String TABLE_ROOMS = "Rooms";
    private static final String COLUMN_ROOM_ID = "id";
    private static final String COLUMN_ROOM_NAME = "name";

    // SQL para crear tabla de usuarios
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_PASSWORD + " TEXT, " +
            COLUMN_IMAGE + " TEXT);";

    // SQL para crear tabla de dispositivos
    private static final String CREATE_TABLE_DEVICES = "CREATE TABLE " + TABLE_DEVICES + " (" +
            COLUMN_DEVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DEVICE_NAME + " TEXT, " +
            COLUMN_DEVICE_LOCATION + " TEXT);";

    // SQL para crear tabla de habitaciones
    private static final String CREATE_TABLE_ROOMS = "CREATE TABLE " + TABLE_ROOMS + " (" +
            COLUMN_ROOM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ROOM_NAME + " TEXT);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_DEVICES);
        db.execSQL(CREATE_TABLE_ROOMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        onCreate(db);
    }

    // -------------------- CRUD USUARIOS --------------------

    public long addUser(String username, String password, String imageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_IMAGE, imageName);
        return db.insert(TABLE_USERS, null, values);
    }

    public Cursor getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null,
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
    }


    public Cursor getUserByUsername(String username) {
        if (username == null) return null;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null,
                COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null);
    }


    public int updateUserPasswordByUsername(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);
        return db.update("users", values, "username = ?", new String[]{username});
    }

    public int updateUserImageByUsername(String username, String imageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imageName);
        db.update("users", values, "username = ?", new String[]{username});
        return 0;
    }


    public Cursor getImageByUsername(String username) {
        if (username == null) return null;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, new String[]{COLUMN_IMAGE}, // Solo obtenemos la columna "image"
                COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, null, null, null, null, null);
    }

    public int updateUserPassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        return db.update(TABLE_USERS, values, COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)});
    }

    public int updateUserImage(int userId, String imageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, imageName);
        return db.update(TABLE_USERS, values, COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)});
    }

    public int deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)});
    }

}
