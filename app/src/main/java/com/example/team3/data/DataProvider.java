package com.example.team3.data;

import android.content.Context;
import android.util.Log;


import com.example.team3.R;
import com.example.team3.models.product.Digital;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Painting;
import com.example.team3.models.product.Photo;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataProvider {
    private final Context context;
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;

    /**
     * Data provider class used to populate Firestore database with initial data.
     * Call from the startup main activity.
     *
     * @param context application context.
     */
    public DataProvider(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
    }

    /**
     * Reads csv file stored in raw resource folder. Extracts and stores in a string array.
     *
     * @return List of string arrays.
     */
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

    /**
     * Seeds Firestore database with data prepared in csv file.
     */
    public void seedData() {
        List<String[]> productsRaw = getProductsRaw();
        for (String[] p : productsRaw) {
            String type = p[0], name = p[2], artist = p[3], mainColour = p[6], theme = p[7], description = p[8], extra = p[9];
            int id = Integer.parseInt(p[1]), year = Integer.parseInt(p[4]), price = Integer.parseInt(p[5]);

            List<String> images = new ArrayList<>();
            String tokenId = String.valueOf(randomToken());
            int viewCount = 0;

            switch (type) {
                case "painting":
                    IProduct painting = new Painting(id, name, artist, year, images, price,
                            mainColour, theme, description, viewCount, type, false, extra);
                    writeToDatabase(painting, "Paintings");
                    break;
                case "photo":
                    IProduct photo = new Photo(id, name, artist, year, images, price,
                            mainColour, theme, description, viewCount, type, false, extra);
                    writeToDatabase(photo, "Photos");
                    break;
                case "digital":
                    IProduct digital = new Digital(id, name, artist, year, images, price,
                            mainColour, theme, description, viewCount, type, false, extra, tokenId);
                    writeToDatabase(digital, "Digital");
                    break;
            }
        }
    }

    /**
     * Helper method to write product data to Firestore.
     *
     * @param product   a product object.
     * @param directory string directory of where to store the product in.
     */
    private void writeToDatabase(IProduct product, String directory) {
        db.collection(directory).document(String.valueOf(product.getId())).set(product)
                .addOnSuccessListener(unused -> {
                    Log.d("DataProvider", "Added art: " + product.getId());
                    setImageUrls(product.getId(), directory);
                })
                .addOnFailureListener(e -> Log.d("DataProvider", "Failed to add art: " + product.getId()));
    }

    /**
     * Helper method to grab image urls from Firestore.
     *
     * @param id        image id.
     * @param directory directory the image belongs to.
     */
    public void setImageUrls(int id, String directory) {
        StorageReference storageRef = storage.getReference();
        for (int i = 1; i <= 3; i++) {
            storageRef.child(id + "_" + i + ".jpg").getDownloadUrl()
                    .addOnSuccessListener(uri -> setImageUrl(id, uri.toString(), directory));
        }
    }

    /**
     * Takes the image url and adds it to the document images field.
     *
     * @param id        image id.
     * @param uri       Firestore image url.
     * @param directory directory the image belongs to.
     */
    private void setImageUrl(int id, String uri, String directory) {
        db.collection(directory).document(String.valueOf(id)).update("images", FieldValue.arrayUnion(uri))
                .addOnSuccessListener(unused -> Log.d("DataProvider", "Added image uri to art: " + id))
                .addOnFailureListener(e -> Log.d("DataProvider", "Failed to add uri to art: " + id));
    }

    /**
     * Random integer generator helper method.
     *
     * @return random 6 digit integer.
     */
    private int randomToken() {
        Random rnd = new Random();
        return 100000 + rnd.nextInt(900000);
    }

}

