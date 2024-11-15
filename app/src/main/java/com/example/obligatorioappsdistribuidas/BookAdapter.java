package com.example.obligatorioappsdistribuidas;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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

        holder.addBookBtn.setOnClickListener(v -> {
            BookRepository repository = new BookRepository(v.getContext());
            repository.saveBook(book);
        });
        /*Android Studio no permite conexiones http y por
        alguna razón el link de la api me devuelve http
        asi que lo cambio a https manualmente*/
        String thumbnailUrl = book.getThumbnail().replace("http:", "https:");

        //Uso de librería glide para cargar la imagen
        Glide.with(holder.itemView.getContext())
                .load(thumbnailUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.thumbnailImageView);
    }
    @Override
    public int getItemCount() {
        return bookList.size();

    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{
        public ImageView thumbnailImageView;
        TextView titleTextView, authorsTextView;
        Button addBookBtn;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorsTextView = itemView.findViewById(R.id.authorsTextView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            addBookBtn = itemView.findViewById(R.id.btn_add_book);
        }
    }
}

