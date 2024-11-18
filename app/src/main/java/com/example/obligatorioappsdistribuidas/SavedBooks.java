package com.example.obligatorioappsdistribuidas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.List;

public class SavedBooks extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    FirebaseUser user;
    TextView textView;
    private RecyclerView recyclerViewResults;
    private Button btnVolver;

    @SuppressLint("MissingInflatedId")
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

        button = findViewById(R.id.logoutButton);
        btnVolver = findViewById(R.id.btnVolver);
        recyclerViewResults = findViewById(R.id.history_recycler_view);
        textView = findViewById(R.id.header);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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

    }

    private void loadBooks (){
        BookAdapter bookAdapter;
        BookRepository repository = new BookRepository(this);
        String userID = repository.getUserId();
        List<Book> bookList = repository.searchBookByUserId(userID);

        //Config del recycler
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(bookList);
        recyclerViewResults.setAdapter(bookAdapter);

    }
}
