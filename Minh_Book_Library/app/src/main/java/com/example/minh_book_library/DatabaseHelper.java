package com.example.minh_book_library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Library.db";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_BOOKS = "saved_books";
    public static final int DATABASE_VERSION = 2;

    // Current logged in user
    private static String currentUser = "";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_BOOKS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, title TEXT, " +
                "subtitle TEXT, authors TEXT, publisher TEXT, description TEXT, pageCount INTEGER, " +
                "thumbnail TEXT, previewLink TEXT, infoLink TEXT, publishedDate TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    // Set current user
    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    // Get current user
    public static String getCurrentUser() {
        return currentUser;
    }

    // Register user
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);

        long result = db.insert(TABLE_USERS, null, cv);
        return result != -1;
    }

    // Check login
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE username=? AND password=?",
                new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // Delete user
    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_USERS, "username=?", new String[]{username});
        return result > 0;
    }

    // Save book for current user
    public boolean saveBook(BookInfo bookInfo) {
        if (currentUser.isEmpty()) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", currentUser);
        cv.put("title", bookInfo.getTitle());
        cv.put("subtitle", bookInfo.getSubtitle());
        cv.put("authors", bookInfo.getAuthors() != null ? String.join(",", bookInfo.getAuthors()) : "");
        cv.put("publisher", bookInfo.getPublisher());
        cv.put("description", bookInfo.getDescription());
        cv.put("pageCount", bookInfo.getPageCount());
        cv.put("thumbnail", bookInfo.getThumbnail());
        cv.put("previewLink", bookInfo.getPreviewLink());
        cv.put("infoLink", bookInfo.getInfoLink());
        cv.put("publishedDate", bookInfo.getPublishedDate());

        long result = db.insert(TABLE_BOOKS, null, cv);
        return result != -1;
    }

    // Check if book is already saved
    public boolean isBookSaved(String title) {
        if (currentUser.isEmpty()) return false;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS +
                        " WHERE username=? AND title=?",
                new String[]{currentUser, title});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // Get all saved books for current user
    public ArrayList<BookInfo> getSavedBooks() {
        ArrayList<BookInfo> savedBooks = new ArrayList<>();
        if (currentUser.isEmpty()) return savedBooks;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS + " WHERE username=?",
                new String[]{currentUser});

        int titleIndex = cursor.getColumnIndexOrThrow("title");
        int subtitleIndex = cursor.getColumnIndexOrThrow("subtitle");
        int authorsIndex = cursor.getColumnIndexOrThrow("authors");
        int publisherIndex = cursor.getColumnIndexOrThrow("publisher");
        int descriptionIndex = cursor.getColumnIndexOrThrow("description");
        int pageCountIndex = cursor.getColumnIndexOrThrow("pageCount");
        int thumbnailIndex = cursor.getColumnIndexOrThrow("thumbnail");
        int previewLinkIndex = cursor.getColumnIndexOrThrow("previewLink");
        int infoLinkIndex = cursor.getColumnIndexOrThrow("infoLink");
        int publishedDateIndex = cursor.getColumnIndexOrThrow("publishedDate");

        while (cursor.moveToNext()) {
            String title = cursor.getString(titleIndex);
            String subtitle = cursor.getString(subtitleIndex);
            String authorsStr = cursor.getString(authorsIndex);
            String publisher = cursor.getString(publisherIndex);
            String description = cursor.getString(descriptionIndex);
            int pageCount = cursor.getInt(pageCountIndex);
            String thumbnail = cursor.getString(thumbnailIndex);
            String previewLink = cursor.getString(previewLinkIndex);
            String infoLink = cursor.getString(infoLinkIndex);
            String publishedDate = cursor.getString(publishedDateIndex);

            ArrayList<String> authorsList = new ArrayList<>();
            if (authorsStr != null && !authorsStr.isEmpty()) {
                String[] authors = authorsStr.split(",");
                for (String author : authors) {
                    authorsList.add(author);
                }
            }

            BookInfo book = new BookInfo(title, subtitle, authorsList, publisher, publishedDate,
                    description, pageCount, thumbnail, previewLink,
                    infoLink, "");
            savedBooks.add(book);
        }
        cursor.close();
        return savedBooks;
    }

    // Remove saved book
    public boolean removeSavedBook(String title) {
        if (currentUser.isEmpty()) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BOOKS, "username=? AND title=?",
                new String[]{currentUser, title});
        return result > 0;
    }
}