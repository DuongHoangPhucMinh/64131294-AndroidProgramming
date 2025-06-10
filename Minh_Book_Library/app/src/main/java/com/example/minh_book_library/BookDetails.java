package com.example.minh_book_library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class BookDetails extends AppCompatActivity {

    // Declaring variables for book details
    private String title, subtitle, publisher, publishedDate, description, thumbnail, previewLink, infoLink;
    private int pageCount;
    private ArrayList<String> authors;

    // UI components
    private TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV;
    private Button previewBtn, saveBtn;
    private ImageView bookIV;

    // Database helper
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database helper
        db = new DatabaseHelper(this);

        // Initializing views
        titleTV = findViewById(R.id.bookTitle);
        subtitleTV = findViewById(R.id.bookSubTitle);
        publisherTV = findViewById(R.id.publisher);
        descTV = findViewById(R.id.bookDescription);
        pageTV = findViewById(R.id.pageCount);
        publishDateTV = findViewById(R.id.publishedDate);
        previewBtn = findViewById(R.id.idBtnPreview);
        saveBtn = findViewById(R.id.idBtnBuy); // Reusing the Buy button for Save functionality
        bookIV = findViewById(R.id.bookImage);

        // Change button text from "Buy" to "Save"
        saveBtn.setText("Save Book");

        // Getting data passed from the adapter class
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        subtitle = intent.getStringExtra("subtitle");
        publisher = intent.getStringExtra("publisher");
        publishedDate = intent.getStringExtra("publishedDate");
        description = intent.getStringExtra("description");
        pageCount = intent.getIntExtra("pageCount", 0);
        thumbnail = intent.getStringExtra("thumbnail");
        previewLink = intent.getStringExtra("previewLink");
        infoLink = intent.getStringExtra("infoLink");

        // Create authors ArrayList from the passed string array
        authors = intent.getStringArrayListExtra("authors");
        if (authors == null) {
            authors = new ArrayList<>();
        }

        // Setting data to UI components
        titleTV.setText(title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Published On : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("Pages : " + pageCount);

        // Load book image
        loadBookImage();

        // Check if book is already saved and update button text
        if (db.isBookSaved(title)) {
            saveBtn.setText("Remove Book");
        }

        // Adding click listener for preview button
        previewBtn.setOnClickListener(v -> {
            if (previewLink == null || previewLink.isEmpty()) {
                Toast.makeText(BookDetails.this, "No preview link present", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(previewLink));
            startActivity(i);
        });

        // Adding click listener for save button
        saveBtn.setOnClickListener(v -> {
            if (db.isBookSaved(title)) {
                // Remove book if already saved
                if (db.removeSavedBook(title)) {
                    Toast.makeText(BookDetails.this, "Book removed from your library", Toast.LENGTH_SHORT).show();
                    saveBtn.setText("Save Book");
                } else {
                    Toast.makeText(BookDetails.this, "Failed to remove book", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Save book
                BookInfo bookInfo = new BookInfo(
                        title, subtitle, authors, publisher, publishedDate,
                        description, pageCount, thumbnail, previewLink, infoLink, ""
                );

                if (db.saveBook(bookInfo)) {
                    Toast.makeText(BookDetails.this, "Book saved to your library", Toast.LENGTH_SHORT).show();
                    saveBtn.setText("Remove Book");
                } else {
                    Toast.makeText(BookDetails.this, "Failed to save book", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadBookImage() {
        String imageUrl = thumbnail;

        // Convert HTTP to HTTPS if needed for security
        if (imageUrl != null && !imageUrl.isEmpty() && imageUrl.startsWith("http://")) {
            imageUrl = imageUrl.replace("http://", "https://");
        }

        // Load image with Glide - simple configuration
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(bookIV);
    }
}