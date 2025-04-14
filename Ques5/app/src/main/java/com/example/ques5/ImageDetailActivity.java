package com.example.ques5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;

public class ImageDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button deleteButton;
    private Button backButton;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        imageView = findViewById(R.id.imageViewDetail);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);

        imagePath = getIntent().getStringExtra("imagePath");

        // Load image with Glide
        Glide.with(this)
                .load(new File(imagePath))
                .fitCenter()
                .into(imageView);

        deleteButton.setOnClickListener(v -> confirmDelete());

        backButton.setOnClickListener(v -> finish());
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Delete", (dialog, which) -> deleteImage())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteImage() {
        File file = new File(imagePath);
        if (file.exists() && file.delete()) {
            Toast.makeText(this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
        }
    }
}