package com.example.obligatorioappsdistribuidas;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookRepository {
    private DBConexion dbConexion;
    public BookRepository(Context context){
        dbConexion = new DBConexion(context);
    }

    public void saveBook(Book book, String userId){
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


        ContentValues userBook = new ContentValues();
        userBook.put(DBConexion.UserBooksEntry.COLUMN_USER_ID, userId);
        userBook.put(DBConexion.UserBooksEntry.COLUMN_BOOK_ID, book.getId());



        db.insert(DBConexion.FeedEntry.TABLE_NAME, null, values);
        db.close();
    }

    @SuppressLint("Range")
    private Book cursorToObject(Cursor cursor) {
        Book book = new Book();
        book.setId(
                String.valueOf(cursor.getInt(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_ID))));
        book.setTitle(
                cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_TITLE)));
        book.setAuthors(
                Collections.singletonList(cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_AUTHORS))));
        book.setBuyLink(
                cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_BUY_LINK)));
        book.setDescription(
                cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_DESCRIPTION)));
        book.setInfoLink(
                cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_INFO_LINK)));
        book.setPageCount(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_PAGE_COUNT))));
        book.setPreviewLink(
                cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_PREVIEW_LINK)));
        book.setPublisher(
                cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_PUBLISHER)));
        book.setSubtitle(
                cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_SUBTITLE)));
        book.setThumbnail(
                cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_THUMBNAIL)));
        book.setPublishedDate(
                cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_PUBLISHED_DATE)));

        return book;
    }


    public ArrayList<Book> searchBookByUserId (String userId){


        String query = "SELECT * FROM " + DBConexion.FeedEntry.TABLE_NAME +
                " INNER JOIN " + DBConexion.UserBooksEntry.TABLE_NAME + " on " + DBConexion.FeedEntry.COLUMN_ID + " = " + DBConexion.UserBooksEntry.COLUMN_BOOK_ID +
                " WHERE " + DBConexion.UserBooksEntry.COLUMN_USER_ID + " = " + userId;

        ArrayList<Book> listBooks = new ArrayList<Book>();
        SQLiteDatabase db = dbConexion.getReadableDatabase();
        Cursor cursor = null;
        try{

            cursor = db.rawQuery(query, new String[]{userId});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Convertir cada registro en un objeto Book
                    Book book = cursorToObject(cursor);
                    listBooks.add(book);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Registrar el error para depuración
        } finally {
            if (cursor != null) {
                cursor.close(); // Cerrar el cursor
            }
            db.close(); // Cerrar la conexión
        }

        return listBooks;


    }
}
