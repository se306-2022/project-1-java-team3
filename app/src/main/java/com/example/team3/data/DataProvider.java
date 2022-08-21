package com.example.team3.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.team3.R;
import com.example.team3.models.product.Digital;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Painting;
import com.example.team3.models.product.Photo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataProvider {
    private final Context context;
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;

    public DataProvider(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
    }

    private List<String[]> getProductsRaw() {
        List<String[]> productsRaw = new ArrayList<>();

        InputStream is = context.getResources().openRawResource(R.raw.products);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",");
                productsRaw.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return productsRaw;
    }

    public void seedData() {
        List<String[]> productsRaw = getProductsRaw();
        for (String[] p : productsRaw) {
            // Extract raw values.
            String type = p[0];
            int id = Integer.parseInt(p[1]);
            String name = p[2];
            String artist = p[3];
            int year = Integer.parseInt(p[4]);
            List<String> images = new ArrayList<>();
            int price = Integer.parseInt(p[5]);
            String mainColour = p[6];
            String theme = p[7];
            String description = p[8];
            int viewCount = Integer.parseInt(p[9]);

            // Depending on type add object to database.
            switch (type) {
                case "painting":
                    IProduct painting = new Painting(id, name, artist, year, images, price, mainColour, theme, description, viewCount, type);
                    writeToDatabase(painting, "Paintings");
                    break;
                case "photo":
                    IProduct photo = new Photo(id, name, artist, year, images, price, mainColour, theme, description, viewCount, type);
                    writeToDatabase(photo, "Photos");
                    break;
                case "digital":
                    IProduct digital = new Digital(id, name, artist, year, images, price, mainColour, theme, description, viewCount, type);
                    writeToDatabase(digital, "Digital");
                    break;
            }
        }
    }

    private void writeToDatabase(IProduct product, String directory) {
        db.collection(directory).document(String.valueOf(product.getId())).set(product)
                .addOnSuccessListener(unused -> {
                    Log.d("DataProvider", "Added art: " + product.getId());
                    setImageUrls(product.getId(), directory);
                })
                .addOnFailureListener(e -> Log.d("DataProvider", "Failed to add art: " + product.getId()));
    }

    public void setImageUrls(int id, String directory) {
        StorageReference storageRef = storage.getReference();
        // Digital art don't have frames.
        if (directory.equals("Digital")) {
            storageRef.child(id + ".jpg").getDownloadUrl()
                    .addOnSuccessListener(uri -> setImageUrl(id, uri.toString(), directory));
        } else {
            for (int i = 1; i <= 3; i++) {
                storageRef.child(id + "_" + i + ".jpg").getDownloadUrl()
                        .addOnSuccessListener(uri -> setImageUrl(id, uri.toString(), directory));
            }
        }
    }

    private void setImageUrl(int id, String uri, String directory) {
        db.collection(directory).document(String.valueOf(id)).update("images", FieldValue.arrayUnion(uri))
                .addOnSuccessListener(unused -> Log.d("DataProvider", "Added image uri to art: " + id))
                .addOnFailureListener(e -> Log.d("DataProvider", "Failed to add uri to art: " + id));
    }

}

