package com.example.obligatorioappsdistribuidas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> bookList;

    public BookAdapter(List<Book> books){
        this.bookList = books;
    }


    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorsTextView.setText(String.join(", ", book.getAuthors()));
    }
    @Override
    public int getItemCount() {
        return bookList.size();

    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView, authorsTextView;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorsTextView = itemView.findViewById(R.id.authorsTextView);
        }
    }
}

