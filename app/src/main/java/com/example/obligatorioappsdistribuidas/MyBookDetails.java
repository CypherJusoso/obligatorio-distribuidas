package com.example.obligatorioappsdistribuidas;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class MyBookDetails extends AppCompatActivity {

    private TextView bookTitle, bookAuthor, bookDescription;
    private EditText pageNumberInput;
    private Button savePageButton;
    private ImageView imageView;
    private String bookId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_book_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookDescription = findViewById(R.id.bookDescription);
        pageNumberInput = findViewById(R.id.pageNumberInput);
        savePageButton = findViewById(R.id.savePageButton);
        imageView = findViewById(R.id.bookImage);

        //Obtengo el libro del intent
       String bookId = getIntent().getStringExtra("book_id");

       //cargo los detalles del libro
        loadBookDetails(bookId);

        savePageButton.setOnClickListener(v -> {
            String pageNumber = pageNumberInput.getText().toString();
            if(!pageNumber.isEmpty()){
                savePageNumber(bookId, Integer.parseInt(pageNumber));
            }
        });
    }
    private void loadBookDetails(String bookId){
        BookRepository repository = new BookRepository(this);
        //Consigo el libro con la id y le paso los datos a los textView y otros
        Book book = repository.getBookById(bookId);
        String thumbnailUrl = book.getThumbnail().replace("http:", "https:");
        bookTitle.setText(book.getTitle());
        String authorsText = TextUtils.join(", ", book.getAuthors());
        bookAuthor.setText(authorsText);
        bookDescription.setText(book.getDescription());
        Glide.with(this)
                .load(thumbnailUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView);

    }
    //Se actualiza la página en la que quedo el usuario con un input.
    private void savePageNumber(String bookId, int pageNumber){
        BookRepository repository = new BookRepository(this);
        String userId = repository.getUserId();
        repository.savePageNumber(userId, bookId, pageNumber);
        Toast.makeText(this, "Numero de pagina guardado!", Toast.LENGTH_SHORT).show();
    }
}