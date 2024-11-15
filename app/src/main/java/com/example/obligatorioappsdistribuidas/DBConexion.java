package com.example.obligatorioappsdistribuidas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBConexion extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BooksDatabase.db";
    protected Context mContext;

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Libros";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SUBTITLE = "subtitle";
        public static final String COLUMN_AUTHORS = "authors";
        public static final String COLUMN_PUBLISHER = "publisher";
        public static final String COLUMN_PUBLISHED_DATE = "publishedDate";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PAGE_COUNT = "pageCount";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_PREVIEW_LINK = "previewLink";
        public static final String COLUMN_INFO_LINK = "infoLink";
        public static final String COLUMN_BUY_LINK = "buyLink";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                    FeedEntry.COLUMN_TITLE + " TEXT," +
                    FeedEntry.COLUMN_SUBTITLE + " TEXT," +
                    FeedEntry.COLUMN_AUTHORS + " TEXT," +
                    FeedEntry.COLUMN_PUBLISHER + " TEXT," +
                    FeedEntry.COLUMN_PUBLISHED_DATE + " TEXT," +
                    FeedEntry.COLUMN_DESCRIPTION + " TEXT," +
                    FeedEntry.COLUMN_PAGE_COUNT + " INTEGER," +
                    FeedEntry.COLUMN_THUMBNAIL + " TEXT," +
                    FeedEntry.COLUMN_PREVIEW_LINK + " TEXT," +
                    FeedEntry.COLUMN_INFO_LINK + " TEXT," +
                    FeedEntry.COLUMN_BUY_LINK + " TEXT)";

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_USER_ID = "userId";
        public static final String COLUMN_USERNAME = "username";
    }
    private static final String SQL_CREATE_USER_ENTRY =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry.COLUMN_USER_ID + " TEXT PRIMARY KEY," +
                    UserEntry.COLUMN_USERNAME + "TEXT)";
    public static class UserBooksEntry implements BaseColumns {
        public static final String TABLE_NAME = "UsersBooks";
        public static final String COLUMN_USER_ID = "userId";
        public static final String COLUMN_BOOK_ID = "bookId";
    }
    private static final String SQL_CREATE_USER_BOOKS_ENTRY =
            "CREATE TABLE " + UserBooksEntry.TABLE_NAME + " (" +
                    UserBooksEntry.COLUMN_USER_ID + " TEXT PRIMARY KEY," +
                    UserBooksEntry.COLUMN_BOOK_ID + "TEXT," +
                    "PRIMARY KEY (" + UserBooksEntry.COLUMN_USER_ID + "," + UserBooksEntry.COLUMN_BOOK_ID + ")," +
                    "FOREIGN KEY (" + UserBooksEntry.COLUMN_USER_ID + ") REFERENCES Users(" + UserEntry.COLUMN_USER_ID + ")," +
                    "FOREIGN KEY (" + UserBooksEntry.COLUMN_BOOK_ID + ") REFERENCES " + FeedEntry.TABLE_NAME + "(ID))";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    public DBConexion(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
