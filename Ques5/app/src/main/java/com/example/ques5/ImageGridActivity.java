package com.example.ques5;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.io.File;
import java.util.ArrayList;

public class ImageGridActivity extends AppCompatActivity implements ImageAdapter.OnImageClickListener {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar progressBar;
    private ArrayList<File> imageFiles;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.tvEmptyView);
        progressBar = findViewById(R.id.progressBar);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        loadImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImages(); // Refresh when returning
    }

    private void loadImages() {
        progressBar.setVisibility(View.VISIBLE);
        File folder = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
                ? getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                : Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        imageFiles = getImagesFromFolder(folder);
        progressBar.setVisibility(View.GONE);

        if (imageFiles.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            imageAdapter = new ImageAdapter(this, imageFiles, this);
            recyclerView.setAdapter(imageAdapter);
        }
    }

    private ArrayList<File> getImagesFromFolder(File folder) {
        ArrayList<File> images = new ArrayList<>();
        if (folder != null && folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && (file.getName().endsWith(".jpg")
                            || file.getName().endsWith(".jpeg")
                            || file.getName().endsWith(".png"))) {
                        images.add(file);
                    }
                }
            }
        }
        return images;
    }

    @Override
    public void onImageClick(File image, int position) {
        Intent intent = new Intent(this, ImageDetailActivity.class);
        intent.putExtra("imagePath", image.getAbsolutePath());
        startActivity(intent);
    }
}
