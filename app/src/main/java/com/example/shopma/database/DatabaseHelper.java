package com.example.shopma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ShopMa.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE panier (_id INTEGER PRIMARY KEY AUTOINCREMENT, product_id INTEGER, title TEXT, price REAL, quantity INTEGER)");
        db.execSQL("CREATE TABLE commandes (_id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, nb_articles INTEGER, montant_total REAL, statut TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS panier");
        db.execSQL("DROP TABLE IF EXISTS commandes");
        onCreate(db);
    }

    public boolean ajouterAuPanier(int productId, String title, double price, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("product_id", productId);
        v.put("title", title);
        v.put("price", price);
        v.put("quantity", qty);
        return db.insert("panier", null, v) != -1;
    }

    public Cursor getPanierItems() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM panier", null);
    }

    public void viderPanier() {
        this.getWritableDatabase().execSQL("DELETE FROM panier");
    }

    public boolean ajouterCommande(String date, int nb, double tot, String stat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("date", date);
        v.put("nb_articles", nb);
        v.put("montant_total", tot);
        v.put("statut", stat);
        return db.insert("commandes", null, v) != -1;
    }

    public Cursor getCommandes() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM commandes ORDER BY _id DESC", null);
    }
}