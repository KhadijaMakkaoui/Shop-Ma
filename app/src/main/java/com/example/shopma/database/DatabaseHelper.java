package com.example.shopma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ShopMa.db";
    private static final int DB_VERSION = 2; // Version augmentée pour la mise à jour de la clé _id

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table Panier (Utilisation impérative de _id pour le CursorAdapter)
        String createPanier = "CREATE TABLE panier (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "product_id INTEGER, " +
                "title TEXT, " +
                "price REAL, " +
                "quantity INTEGER)";
        db.execSQL(createPanier);

        // Table Commandes
        String createCommandes = "CREATE TABLE commandes (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "nb_articles INTEGER, " +
                "montant_total REAL, " +
                "statut TEXT)";
        db.execSQL(createCommandes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS panier");
        db.execSQL("DROP TABLE IF EXISTS commandes");
        onCreate(db);
    }

    // --- MÉTHODES DU PANIER ---

    public boolean ajouterAuPanier(int productId, String title, double price, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_id", productId);
        values.put("title", title);
        values.put("price", price);
        values.put("quantity", quantity);

        long result = db.insert("panier", null, values);
        return result != -1;
    }

    public Cursor getPanierItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM panier", null);
    }

    public void viderPanier() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM panier");
    }

    // --- MÉTHODES DES COMMANDES ---

    public boolean ajouterCommande(String date, int nbArticles, double total, String statut) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("nb_articles", nbArticles);
        values.put("montant_total", total);
        values.put("statut", statut);

        long result = db.insert("commandes", null, values);
        return result != -1;
    }

    public Cursor getCommandes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM commandes ORDER BY _id DESC", null);
    }
}