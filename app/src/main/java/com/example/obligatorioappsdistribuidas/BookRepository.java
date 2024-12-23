package com.example.obligatorioappsdistribuidas;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookRepository {
    private DBConexion dbConexion;
    public BookRepository(Context context){
        dbConexion = new DBConexion(context);
    }

    /*public void saveBook(Book book, String userId){
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
    }*/
    public String saveBook(Book book, String userId) {
        SQLiteDatabase db = dbConexion.getWritableDatabase();
        try {

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

            // Inserta el libro en la tabla principal
             db.insertOrThrow(DBConexion.FeedEntry.TABLE_NAME, null, values);

            ContentValues userBook = new ContentValues();
            userBook.put(DBConexion.UserBooksEntry.COLUMN_USER_ID, userId);
            userBook.put(DBConexion.UserBooksEntry.COLUMN_BOOK_ID, book.getId());
            Log.d("BookRepository", "User ID: " + userId + ", Book ID: " + book.getId());

            // Inserta la relación usuario-libro
             db.insertOrThrow(DBConexion.UserBooksEntry.TABLE_NAME, null, userBook);

            return null; // Null indica éxito
        } catch (SQLiteException e) {
            return e.getMessage(); // Devuelve el mensaje de error original
        } finally {
            db.close();
        }
    }




    @SuppressLint("Range")
    private Book cursorToObject(Cursor cursor) {
        Book book = new Book();
        book.setId(
                cursor.getString(cursor.getColumnIndex(DBConexion.FeedEntry.COLUMN_ID)));
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

        //Había un error en la syntaxis de esta consulta, hacía que no se mostraran los libros en el historial
        String query = "SELECT * FROM " + DBConexion.FeedEntry.TABLE_NAME +
                " INNER JOIN " + DBConexion.UserBooksEntry.TABLE_NAME + " on " + DBConexion.FeedEntry.COLUMN_ID + " = " + DBConexion.UserBooksEntry.COLUMN_BOOK_ID +
                " WHERE " + DBConexion.UserBooksEntry.COLUMN_USER_ID + " = '" + userId + "'";

        ArrayList<Book> listBooks = new ArrayList<Book>();
        SQLiteDatabase db = dbConexion.getReadableDatabase();
        Cursor cursor = null;
        try{

            //Antes habían argumentos en este cursor, eso provocaba un error con la consulta que ya tenía argumentos.
            cursor = db.rawQuery(query, null);
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

    public Book getBookById(String bookId){
        String query = "SELECT * FROM " + DBConexion.FeedEntry.TABLE_NAME +
                " WHERE " + DBConexion.FeedEntry.COLUMN_ID + " = ?";
        Book book = null;
        SQLiteDatabase db = dbConexion.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{bookId});
        if(cursor!=null && cursor.moveToFirst()){
            book = cursorToObject(cursor);
        }
        if(cursor!=null){
            cursor.close();
        }
        db.close();
        return book;
    }

    public void savePageNumber(String userId, String bookId, int pageNumber){
        SQLiteDatabase db = dbConexion.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBConexion.UserBooksEntry.COLUMN_PAGE_NUMBER, pageNumber);
        String selection = DBConexion.UserBooksEntry.COLUMN_USER_ID + " = ? AND " +
                DBConexion.UserBooksEntry.COLUMN_BOOK_ID + " = ?";
        String[] selectionArgs = { userId, bookId };

        int count = db.update(
                DBConexion.UserBooksEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        if(count == 0){
            values.put(DBConexion.UserBooksEntry.COLUMN_USER_ID, userId);
            values.put(DBConexion.UserBooksEntry.COLUMN_BOOK_ID, bookId);
            db.insert(DBConexion.UserBooksEntry.TABLE_NAME, null, values);
        }
        db.close();

    }

    public int getCurrentPageForUser(String bookId, String userId){
        SQLiteDatabase db = dbConexion.getReadableDatabase();
        Cursor cursor = db.query(
                DBConexion.UserBooksEntry.TABLE_NAME,
                new String[]{DBConexion.UserBooksEntry.COLUMN_PAGE_NUMBER},
                DBConexion.UserBooksEntry.COLUMN_USER_ID + " = ? AND " + DBConexion.UserBooksEntry.COLUMN_BOOK_ID + " = ?",
                new String[]{userId, bookId},
                null, null, null);

        if(cursor != null && cursor.moveToFirst()){
            int currentPage = cursor.getInt(cursor.getColumnIndexOrThrow(DBConexion.UserBooksEntry.COLUMN_PAGE_NUMBER));
            cursor.close();
            return currentPage;
        }else{
            //Si no existe una current page te devuelve 1
            return 1;
        }

    }

    public String getUserId(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = null;

        if (user != null) {
            userId = user.getUid();
            Log.d("Firebase", "User ID: " + userId);
        } else {
            Log.d("Firebase", "No user is currently signed in.");
        }

        return userId;
    }

    public void deleteBookById(String bookId) {
        SQLiteDatabase db = dbConexion.getWritableDatabase();
        db.beginTransaction();
        try{
            db.delete(DBConexion.UserBooksEntry.TABLE_NAME, DBConexion.UserBooksEntry.COLUMN_BOOK_ID + " = ?", new String[]{bookId});
            db.delete(DBConexion.FeedEntry.TABLE_NAME, DBConexion.FeedEntry.COLUMN_ID + " = ?", new String[]{bookId});

            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        db.close();
    }

}
