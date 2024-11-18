package com.example.obligatorioappsdistribuidas;

import android.content.Intent;
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

public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.MyBookViewHolder> {

    private List<Book> bookList;

    public MyBookAdapter(List<Book> books){this.bookList = books;}

    @NonNull
    @Override
    public MyBookAdapter.MyBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_book, parent, false);
        return new MyBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookAdapter.MyBookViewHolder holder, int position) {
    Book book = bookList.get(position);

    holder.bookTitle.setText(book.getTitle());
        String bookId = book.getId();
        String thumbnailUrl = book.getThumbnail().replace("http:", "https:");

        Glide.with(holder.itemView.getContext())
                .load(thumbnailUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.bookCover);

        holder.viewDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MyBookDetails.class);
            intent.putExtra("book_id", bookId);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class MyBookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        Button viewDetailsButton;

        public MyBookViewHolder(View itemView){
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }
    }
}
