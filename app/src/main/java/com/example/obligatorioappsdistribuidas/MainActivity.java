package com.example.obligatorioappsdistribuidas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    Button button_history;
    TextView textView;
    FirebaseUser user;
    private BookSearchService bookSearchService;
    private EditText searchEditText;
    private Button searchBtn;
    private RecyclerView recyclerViewResults;
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        searchEditText = findViewById(R.id.searchEditText);
        searchBtn = findViewById(R.id.searchBtn);
        recyclerViewResults = findViewById(R.id.recyclerViewResults);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        button_history = findViewById(R.id.ver_historial);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        textView.setText(user.getEmail());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        button_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SavedBooks.class);
                startActivity(intent);
                finish();

            }
        });
       //Config del recycler
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(bookList);
        recyclerViewResults.setAdapter(bookAdapter);

        bookSearchService = new BookSearchService();

        searchBtn.setOnClickListener(v -> searchBooks());
    }

    private void searchBooks(){
        //Input del user
        String query = searchEditText.getText().toString();
        bookSearchService.searchBooks(query,
                books -> runOnUiThread(() -> {
                    bookList.clear();
                    //Agrego los libros a bookList
                    bookList.addAll(books);
                    bookAdapter.notifyDataSetChanged();
                }),
                e -> {
                    e.printStackTrace();
                }
        );
    }
}