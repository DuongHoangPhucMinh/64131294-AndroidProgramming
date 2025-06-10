package com.example.minh_book_library;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Variables for networking and UI components
    private RequestQueue mRequestQueue;
    private ArrayList<BookInfo> bookInfoArrayList;
    private ProgressBar progressBar;
    private EditText searchEdt;
    private ImageButton searchBtn;
    private TextView welcomeText, savedBooksTitle;
    private RecyclerView recyclerView, savedBooksRecyclerView;
    private DatabaseHelper db;
    private boolean showingSavedBooks = true;
    private Button logoutBtn;

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

        // Initialize database helper
        db = new DatabaseHelper(this);

        // Initializing UI components
        progressBar = findViewById(R.id.progressBar);
        searchEdt = findViewById(R.id.searchEditText);
        searchBtn = findViewById(R.id.searchButton);
        welcomeText = findViewById(R.id.welcomeText);
        savedBooksTitle = findViewById(R.id.savedBooksTitle);
        recyclerView = findViewById(R.id.rv);
        savedBooksRecyclerView = findViewById(R.id.savedBooksRv);
        logoutBtn = findViewById(R.id.logoutButton);

        // Set up RecyclerViews
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set welcome message
        String username = DatabaseHelper.getCurrentUser();
        welcomeText.setText("Welcome, " + username + "!");

        // Display saved books
        displaySavedBooks();

        // Setting click listener on the search button
        searchBtn.setOnClickListener(v -> {
            String query = searchEdt.getText().toString().trim();
            if (query.isEmpty()) {
                searchEdt.setError("Please enter search query");
                return;
            }

            // Show progress bar while searching
            progressBar.setVisibility(View.VISIBLE);
            getBooksInfo(query);
            showingSavedBooks = false;
            savedBooksTitle.setText("Search Results:");
        });

        // Set click listener for logout button
        logoutBtn.setOnClickListener(v -> performLogout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh saved books when returning to this activity
        if (showingSavedBooks) {
            displaySavedBooks();
        }
    }

    // Display saved books
    private void displaySavedBooks() {
        ArrayList<BookInfo> savedBooks = db.getSavedBooks();
        if (savedBooks.isEmpty()) {
            savedBooksTitle.setText("You haven't saved any books yet");
            savedBooksRecyclerView.setVisibility(View.GONE);
        } else {
            savedBooksTitle.setText("Your Saved Books:");
            savedBooksRecyclerView.setVisibility(View.VISIBLE);
            BookAdapter adapter = new BookAdapter(savedBooks, this);
            savedBooksRecyclerView.setAdapter(adapter);
        }
    }

    // Create menu with logout option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            performLogout();
            return true;
        } else if (id == R.id.menu_show_saved) {
            showingSavedBooks = true;
            recyclerView.setVisibility(View.GONE);
            savedBooksRecyclerView.setVisibility(View.VISIBLE);
            displaySavedBooks();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Improved logout function
    private void performLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");

        builder.setPositiveButton("Có", (dialog, which) -> {
            DatabaseHelper.setCurrentUser("");
            if (bookInfoArrayList != null) {
                bookInfoArrayList.clear();
            }
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(request -> true);
            }
            Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Function to fetch book data from Google Books API
    private void getBooksInfo(String query) {
        bookInfoArrayList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.getCache().clear();

        // Construct API URL using the search query
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=20";

        Log.d(TAG, "API URL: " + url);

        JsonObjectRequest booksObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "API Response received");

                    try {
                        if (!response.has("items")) {
                            Toast.makeText(this, "No books found for this search", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray itemsArray = response.getJSONArray("items");
                        Log.d(TAG, "Found " + itemsArray.length() + " books");

                        for (int i = 0; i < itemsArray.length(); i++) {
                            JSONObject itemsObj = itemsArray.getJSONObject(i);
                            JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");

                            // Extract book details with default values
                            String title = volumeObj.optString("title", "Unknown Title");
                            String subtitle = volumeObj.optString("subtitle", "");
                            JSONArray authorsArray = volumeObj.optJSONArray("authors");
                            String publisher = volumeObj.optString("publisher", "Unknown Publisher");
                            String publishedDate = volumeObj.optString("publishedDate", "Unknown Date");
                            String description = volumeObj.optString("description", "No description available");
                            int pageCount = volumeObj.optInt("pageCount", 0);

                            // Get book thumbnail if available
                            JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                            String thumbnail = "";
                            if (imageLinks != null) {
                                // Try to get the best quality image available
                                if (imageLinks.has("thumbnail")) {
                                    thumbnail = imageLinks.optString("thumbnail", "");
                                } else if (imageLinks.has("smallThumbnail")) {
                                    thumbnail = imageLinks.optString("smallThumbnail", "");
                                }
                            }

                            Log.d(TAG, "Book " + i + " - Title: " + title + ", Thumbnail: " + thumbnail);

                            // Get book preview and info links
                            String previewLink = volumeObj.optString("previewLink", "");
                            String infoLink = volumeObj.optString("infoLink", "");

                            // Convert authors JSONArray to ArrayList<String>
                            ArrayList<String> authorsArrayList = new ArrayList<>();
                            if (authorsArray != null) {
                                for (int j = 0; j < authorsArray.length(); j++) {
                                    authorsArrayList.add(authorsArray.optString(j, "Unknown Author"));
                                }
                            } else {
                                authorsArrayList.add("Unknown Author");
                            }

                            // Create BookInfo object with fetched details
                            BookInfo bookInfo = new BookInfo(
                                    title, subtitle, authorsArrayList, publisher, publishedDate,
                                    description, pageCount, thumbnail, previewLink, infoLink, ""
                            );

                            bookInfoArrayList.add(bookInfo);
                        }

                        // Show search results
                        savedBooksRecyclerView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        // Set up RecyclerView with BookAdapter to display the book list
                        BookAdapter adapter = new BookAdapter(bookInfoArrayList, this);
                        recyclerView.setAdapter(adapter);

                        Log.d(TAG, "RecyclerView adapter set with " + bookInfoArrayList.size() + " books");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        Toast.makeText(this, "Error parsing book data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "API request error: " + error.getMessage());
                    Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        mRequestQueue.add(booksObjRequest);
    }
}