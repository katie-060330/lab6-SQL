package com.example.lab6;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "product";
    public static final String ID_COLUMN = "productID";
    public static final String NAME_COLUMN = "name";
    public static final String SKU_COLUMN = "sku";

    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME + "s.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableCreationStatement = "CREATE TABLE " +
                TABLE_NAME + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                " " + NAME_COLUMN + " TEXT, " +
                SKU_COLUMN + " INTEGER);";

        db.execSQL(tableCreationStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void addProduct(ProductModel product) {
        // getting db instance
        SQLiteDatabase db = this.getWritableDatabase();

        // compiling the payload to be inserted in the db
        ContentValues cv = new ContentValues();
        cv.put(NAME_COLUMN, product.getProductName());
        cv.put(SKU_COLUMN, product.getProductSKU());
        if (product.getProductId() != ProductModel.UNASSIGNED_ID) {
            cv.put(ID_COLUMN, product.getProductId());
        }

        // inserting the product in the db
        db.insert(TABLE_NAME, null, cv);
    }


    public List<ProductModel> getProducts(String query) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<ProductModel> products = new ArrayList<ProductModel>();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ProductModel product = new ProductModel(
                        cursor.getInt(0), // getting ID
                        cursor.getString(1), // getting Name
                        cursor.getInt(2) // getting SKU
                );
                products.add(product);
            } while (cursor.moveToNext());
        }

        return products;
    }

    public List<ProductModel> getAllProducts() {
        String query = "SELECT * FROM " + TABLE_NAME;
        return getProducts(query);
    }

    public List<ProductModel> findProduct(String name, int sku) {
        String query = "SELECT * FROM " + TABLE_NAME;
        boolean selectByName = false;
        if (!name.isEmpty()) {
            // to check if a value contains a certain string
            // you write : COLUMN LIKE %<put the value here>%
            // we also add --case-insensitive to make it, well, case-insensitive!
            query += " WHERE " + NAME_COLUMN + " LIKE \"%" + name + "%\"" +
                    "--case-insensitive";
            ;
            selectByName = true;
        }
        if (sku != -1) {
            String prefix = (selectByName) ? " AND " : " WHERE ";
            query += prefix + SKU_COLUMN + "=" + sku;
        }
        return getProducts(query);
    }

    public List<ProductModel> findProductByName(String name) {
        return findProduct(name, -1);
    }

    public List<ProductModel> findProductBySku(int sku) {
        return findProduct("", sku);
    }


    public void deleteProduct(String name, int sku) {
        SQLiteDatabase db = this.getWritableDatabase();

       if(sku == -1){
           db.delete(TABLE_NAME, NAME_COLUMN + " = ?", new String[]{name});
       }else{
           db.delete(TABLE_NAME, NAME_COLUMN + " = ? AND " + SKU_COLUMN + " = ?", new String[]{name, String.valueOf(sku)});

       }

        db.close();


    }

   public void deleteAllProducts(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
   }


}