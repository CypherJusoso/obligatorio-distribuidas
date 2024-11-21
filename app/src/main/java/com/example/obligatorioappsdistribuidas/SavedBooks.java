package com.example.obligatorioappsdistribuidas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.bumptech.glide.Glide;

import java.util.List;

public class SavedBooks extends AppCompatActivity {

    FirebaseAuth auth;
    Button button, btnVolver, viewActiveBookDetailsButton;
    FirebaseUser user;
    TextView textView, activeBookTitle;
    private RecyclerView recyclerViewResults;
    private ImageView activeBookImage;

    private String idBookActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_savedbooks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.savedbooks), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activeBookImage = findViewById(R.id.activeBookImage);
        activeBookTitle = findViewById(R.id.activeBookTitle);
        viewActiveBookDetailsButton = findViewById(R.id.viewActiveBookDetailsButton);

        button = findViewById(R.id.logoutButton);
        btnVolver = findViewById(R.id.btnVolver);
        recyclerViewResults = findViewById(R.id.history_recycler_view);
        textView = findViewById(R.id.header);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        viewActiveBookDetailsButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), MyBookDetails.class);
                    intent.putExtra("book_id", idBookActive);
                    startActivity(intent);
                } catch (Exception ex) {

                }
            }
        }));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loadBooks();
        loadActiveBook();
    }

    //Metodo para cargar los libros
    private void loadBooks (){

        BookRepository repository = new BookRepository(this);
        String userID = repository.getUserId();
        List<Book> bookList = repository.searchBookByUserId(userID);

        Log.d("LOAD_BOOKS", "Número de libros: " + bookList.size());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewResults.setLayoutManager(horizontalLayoutManager);
        //Config del recycler
        MyBookAdapter myBookAdapter = new MyBookAdapter(bookList);
        recyclerViewResults.setAdapter(myBookAdapter);
    }

    private void loadActiveBook () {

            SharedPreferences sharedPref = getSharedPreferences("libroActivo", MODE_PRIVATE);
            String tituloBook = sharedPref.getString("tituloBook","");
            String imageBook = sharedPref.getString("imageBook","");
            idBookActive = sharedPref.getString("IdBook","");
        if (!tituloBook.isEmpty()) {
            activeBookTitle.setText(tituloBook);
            activeBookTitle.setVisibility(View.VISIBLE);
        } else {
            activeBookTitle.setVisibility(View.INVISIBLE);
        }

        if (!imageBook.isEmpty()) {
            String imageBookStr = imageBook.replace("http:", "https:");
            Glide.with(this)
                    .load(imageBookStr)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(activeBookImage);
            activeBookImage.setVisibility(View.VISIBLE);
        } else {
            activeBookImage.setVisibility(View.INVISIBLE);
        }

        if (!idBookActive.isEmpty()) {
            viewActiveBookDetailsButton.setVisibility(View.VISIBLE);
        } else {
            viewActiveBookDetailsButton.setVisibility(View.INVISIBLE);
        }
    }

}
