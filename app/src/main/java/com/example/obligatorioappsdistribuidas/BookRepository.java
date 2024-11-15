package com.example.obligatorioappsdistribuidas;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BookRepository {
    private DBConexion dbConexion;
    public BookRepository(Context context){
        dbConexion = new DBConexion(context);
    }

    public void saveBook(Book book){
        SQLiteDatabase db = dbConexion.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConexion.FeedEntry.COLUMN_ID, book.getId());
        values.put(DBConexion.FeedEntry.COLUMN_TITLE, book.getTitle());
        values.put(DBConexion.FeedEntry.COLUMN_SUBTITLE, book.getSubtitle());
        values.put(DBConexion.FeedEntry.COLUMN_AUTHORS, String.join(", ", book.getAuthors()));
        values.put(DBConexion.FeedEntry.COLUMN_PUBLISHER, book.getPublisher());
        values.put(DBConexion.FeedEntry.COLUMN_PUBLISHED_DATE, book.getPublishedDate());
        values.put(DBConexion.FeedEntry.COLUMN_DESCRIPTION, book.getDescription());
        values.put(DBConexion.FeedEntry.COLUMN_PAGE_COUNT, book.getPageCount());
        values.put(DBConexion.FeedEntry.COLUMN_THUMBNAIL, book.getThumbnail());
        values.put(DBConexion.FeedEntry.COLUMN_PREVIEW_LINK, book.getPreviewLink());
        values.put(DBConexion.FeedEntry.COLUMN_INFO_LINK, book.getInfoLink());
        values.put(DBConexion.FeedEntry.COLUMN_BUY_LINK, book.getBuyLink());

        db.insert(DBConexion.FeedEntry.TABLE_NAME, null, values);
        db.close();
    }
}
