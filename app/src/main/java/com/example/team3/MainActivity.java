package com.example.team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.team3.data.DataProvider;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Painting;
import com.example.team3.models.product.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Uncomment if you want to seed data to db.
//        DataProvider dp = new DataProvider(this);
//        dp.seedData();

        fetchProductsData();


    }

    private void fetchProductsData() {
        List<IProduct> productsList = new ArrayList<IProduct>();

        // Getting Paintings collection from Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Paintings").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot results = task.getResult();
                for (IProduct aProduct : task.getResult().toObjects(Product.class)) {
                    productsList.add(aProduct);
                    Log.i("Parsing products", aProduct.getId() + " loaded.");
                }
                if (productsList.size() > 0) {
                    Log.i("Getting products", "Success");
                    // Once the task is successful and data is fetched, propagate the adaptor.
                    //propagateAdaptor(productsList)
                    //System.out.println(productsList);
                    Toast.makeText(getBaseContext(), "Loading products successful.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getBaseContext(), "Loading products failed.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "Loading products failed.", Toast.LENGTH_LONG).show();
            }
        });
    }
}