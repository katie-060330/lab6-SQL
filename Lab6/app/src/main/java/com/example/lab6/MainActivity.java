package com.example.lab6;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView pid_textview;
    EditText pname_edittext, sku_edittext;
    Button addBtn, findBtn, dltBtn, dltAllBtn;
    ListView products_listview;

    DatabaseHelper dbhelper;

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



        dbhelper = new DatabaseHelper(this);

        initViews();
        updateDisplayedProducts();
        setEventListeners();
    }

    private void setEventListeners() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = pname_edittext.getText().toString();
                int sku = Integer.parseInt(sku_edittext.getText().toString());

                ProductModel product = new ProductModel(name, sku);
                dbhelper.addProduct(product);
                clearEditText();

                updateDisplayedProducts();
            }
        });

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = pname_edittext.getText().toString();
                String sku_str = sku_edittext.getText().toString();
                int sku = (sku_str.isEmpty()) ? -1 : Integer.parseInt(sku_str);
                List<ProductModel> products = dbhelper.findProduct(name, sku);
                updateDisplayedProducts(products);

                clearEditText();

            }
        });
        dltBtn.setOnClickListener(v -> {
            String name = pname_edittext.getText().toString();
            String sku_str = sku_edittext.getText().toString();
            int sku = (sku_str.isEmpty()) ? -1 : Integer.parseInt(sku_str);
            dbhelper.deleteProduct(name, sku);
            updateDisplayedProducts();
            clearEditText();
            clearTextView();
        });

        dltAllBtn.setOnClickListener(v -> {
            dbhelper.deleteAllProducts();
           Toast.makeText(MainActivity.this, "All products deleted", Toast.LENGTH_SHORT).show();
            updateDisplayedProducts();
            clearEditText();
            clearTextView();
                });

        products_listview.setOnItemClickListener((parent, view, position, id) -> {
            ProductModel product = (ProductModel) products_listview.getItemAtPosition(position);
            pid_textview.setText(String.valueOf(product.getProductId()));
            pname_edittext.setText(product.getProductName());
            sku_edittext.setText(String.valueOf(product.getProductSKU()));
        });

    }

    private void clearTextView() {
        pid_textview.setText("");
    }

    private void clearEditText() {
        pname_edittext.setText("");
        sku_edittext.setText("");
    }

    private void updateDisplayedProducts() {
        List<ProductModel> products = dbhelper.getAllProducts();
        updateDisplayedProducts(products);
    }

    private void updateDisplayedProducts(List<ProductModel> products) {
        ArrayAdapter<ProductModel> adapter = new ArrayAdapter<ProductModel>(this,
                android.R.layout.simple_list_item_1, products);
        products_listview.setAdapter(adapter);
    }

    private void initViews() {
        pid_textview = findViewById(R.id.pId_textview);

        pname_edittext = findViewById(R.id.pName_edittext);
        sku_edittext = findViewById(R.id.sku_edittext);

        addBtn = findViewById(R.id.addBtn);
        findBtn = findViewById(R.id.findBtn);
        dltBtn = findViewById(R.id.dltBtn);
        dltAllBtn = findViewById(R.id.dltAllBtn);

        products_listview = findViewById(R.id.productsListView);
    }
}