package com.example.team3;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import com.example.team3.models.product.IProduct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Filter {
    Spinner spinner;

    public Filter(Spinner spinner) {
        this.spinner = spinner;
    }

    /**
     * Sets filter spinner drop down values to set values
     *
     * @param  c  current context
     * @param  options static list of options to populate - resource file
     */
    public void setFilterSpinner(Context c, int options) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(c,
                options, R.layout.filter_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    /**
     * Sets filter spinner drop down values dynamically based on the available products
     *
     * @param  c  current context
     * @param  productsList List of products currently loaded for category
     * @param  type filter type - implemented for theme and colour
     */
    public void setFilterSpinnerDynamic(Context c, List<IProduct> productsList, String type) {
        Set<String> set = new HashSet<String>();
        String currentProduct="";

        for (IProduct product : productsList) {
            if (type.equals("theme")) {
                currentProduct = product.getTheme();
            } else if (type.equals("mainColour")) {
                currentProduct = product.getMainColour();
            }
            set.add(currentProduct.substring(0, 1).toUpperCase() + currentProduct.substring(1));
        }

        ArrayList<String> options = new ArrayList<>(set);
        if (type.equals("theme")) {
            options.add(0, c.getString(R.string.theme_title));
        } else if (type.equals("mainColour")) {
            options.add(0, c.getString(R.string.colour_title));
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<> (c, R.layout.filter_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }
}
