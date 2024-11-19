package com.example.obligatorioappsdistribuidas;

import android.content.Intent;
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

    private TextView bookTitle, bookAuthor, bookDescription, bookSubtitle, bookPublisher, bookPublishedDate, bookPageCount, currentPageText;
    private EditText pageNumberInput;
    private Button savePageButton, volverButton, deleteBookButton;
    private ImageView imageView;
    private String bookId;
    private int totalPageCount;
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
        bookSubtitle = findViewById(R.id.bookSubtitle);
        bookPublisher = findViewById(R.id.bookPublisher);
        bookPublishedDate = findViewById(R.id.bookPublishedDate);
        bookPageCount = findViewById(R.id.bookPageCount);
        volverButton = findViewById(R.id.volverBtn);
        currentPageText = findViewById(R.id.currentPageText);
        deleteBookButton = findViewById(R.id.deleteBookButton);

        //Obtengo el libro del intent
       String bookId = getIntent().getStringExtra("book_id");

       //cargo los detalles del libro
        loadBookDetails(bookId);

        savePageButton.setOnClickListener(v -> {
            //Consigo el libro con la id y le paso los datos a los textView y otros
            BookRepository repository = new BookRepository(this);
            Book book = repository.getBookById(bookId);
            totalPageCount = book.getPageCount();
            String pageNumberStr = pageNumberInput.getText().toString();
            if(!pageNumberStr.isEmpty()){
                int pageNumber = Integer.parseInt(pageNumberStr);

                //Condiciones para que no se ingresen valores no deseados
                if(pageNumber < 1){
                    Toast.makeText(this, "El numero de pagina no puede ser menor a 1",  Toast.LENGTH_SHORT).show();
                }else if(pageNumber > totalPageCount){
                    Toast.makeText(this, "El numero de pagina no puede ser mayor a" + totalPageCount, Toast.LENGTH_SHORT).show();
                } else{
                    savePageNumber(bookId, pageNumber);
                    currentPageText.setText(getString(R.string.pagina_actual_2, pageNumber));
                }
            }else{
                Toast.makeText(this, "Por favor, ingrese un número de página", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBookButton.setOnClickListener(v -> {
            deleteBook(bookId);
        });

        volverButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void loadBookDetails(String bookId){
        //Consigo el libro con la id y le paso los datos a los textView y otros
        BookRepository repository = new BookRepository(this);
        Book book = repository.getBookById(bookId);
        String thumbnailUrl = book.getThumbnail().replace("http:", "https:");
        bookTitle.setText(book.getTitle());
        bookSubtitle.setText(book.getSubtitle());
        String authorsText = TextUtils.join(", ", book.getAuthors());
        bookAuthor.setText(getString(R.string.book_author, authorsText));
        bookPublisher.setText(getString(R.string.book_publisher, book.getPublisher()));
        bookPublishedDate.setText(getString(R.string.book_published_date, book.getPublishedDate()));
        bookPageCount.setText(getString(R.string.book_page_count, book.getPageCount()));

        int currentPage = repository.getCurrentPageForUser(bookId, repository.getUserId());
        currentPageText.setText(getString(R.string.pagina_actual_2, currentPage));
        pageNumberInput.setText(String.valueOf(currentPage));

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
    private void deleteBook (String bookId){
        BookRepository repository = new BookRepository(this);
        repository.deleteBookById(bookId);

        Toast.makeText(this, "Libro eliminado exitosamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,SavedBooks.class);
        startActivity(intent);
        finish();
    }
}