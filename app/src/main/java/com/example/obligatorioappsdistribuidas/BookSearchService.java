package com.example.obligatorioappsdistribuidas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookSearchService {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    //Url base de la api
    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    //Interfaz onResult que recibe una lista de libros
    public interface BookSearchCallback {
        void onResult(List<Book> books);
    }

    //Interfaz para metodo de error
    public interface ErrorCallback {
        void onError(Exception e);
    }

    //Parametros son la query del user y las 2 interfaces
    public void searchBooks(String query, BookSearchCallback successCallback, ErrorCallback errorCallback) {
        executorService.execute(() -> {
            try {
                //Se construye la url con la consulta del usuario
                String urlString = GOOGLE_BOOKS_API_URL + query.replace(" ", "+");
                URL url = new URL(urlString);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                //Se obtienen los datos que se envian como respuesta a mi get
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                //Mientras haya lineas disponibles segui ejecutando el while
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                //Se transforman los JSON a una lista de libros
                List<Book> books = parseBooksFromJson(response.toString());
                successCallback.onResult(books);
            } catch (Exception e) {
                e.printStackTrace();
                errorCallback.onError(e);
            }
        });
    }

    private List<Book> parseBooksFromJson(String json) throws JSONException {
        List<Book> books = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        //La respuesta de esta API tiene la info que necesito dentro de items, por eso primero checkeo si existe antes de hacer nada
        if(jsonObject.has("items")){
            //Array con todos los libros
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            //bucle for para recorrer todos los objetos dentro del array de items
            for(int i= 0; i< itemsArray.length(); i++){
                JSONObject bookJson = itemsArray.getJSONObject(i);
                //volumeInfo tiene la info de los libros
                JSONObject volumeInfo = bookJson.getJSONObject("volumeInfo");

                //Consigo los datos que necesito
                String id = volumeInfo.optString("id", "No hay id");
                String title = volumeInfo.optString("title", "No hay titulo");
                String subtitle = volumeInfo.optString("subtitle", "No hay subtitle");

                //Lista que almacena los autores del libro
                List<String> authors = new ArrayList<>();
                if(volumeInfo.has("authors")){
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    //Por cada autor añadi ese autor a la lista authors
                    for(int j = 0; j< authorsArray.length(); j++){
                        authors.add(authorsArray.getString(j));
                    }
                }else{
                    authors.add("No hay autor");
                }

                String publisher = volumeInfo.optString("publisher", "No hay publisher");
                String publishedDate = volumeInfo.optString("publishedDate", "No hay publishedDate");
                String description = volumeInfo.optString("description", "No hay descripcion");
                int pageCount = volumeInfo.optInt("pageCount", 0);

                String thumbnail = "No hay thumbnail";

                //Se consigue el thumbnail del imageLinks
                JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
                if(imageLinks != null){
                    thumbnail = imageLinks.optString("thumbnail", "no hay thumbnail");
                }

                String previewLink = volumeInfo.optString("previewLink", "No hay previewLink");
                String infoLink = volumeInfo.optString("infoLink", "No hay infoLink");

                JSONObject saleInfo = bookJson.optJSONObject("saleInfo");
                String buyLink = saleInfo != null ? saleInfo.optString("buyLink", "No hay buyLink") : "No hay buyLink";

                //Creo el objeto Book y lo añado a la lista
                books.add(new Book(id, title, subtitle, authors, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink));

            }
        }
        return books;
   }
}


