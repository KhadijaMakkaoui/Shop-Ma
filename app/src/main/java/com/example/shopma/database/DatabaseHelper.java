package com.example.shopma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shopma.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTablePanier = "CREATE TABLE panier (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "product_id INTEGER, " +
                "title TEXT, " +
                "price REAL, " +
                "quantity INTEGER)";

        // Table 6.2 : commandes
        String createTableCommandes = "CREATE TABLE commandes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "nb_articles INTEGER, " +
                "montant_total REAL, " +
                "statut TEXT)";

        db.execSQL(createTablePanier);
        db.execSQL(createTableCommandes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS panier");
        db.execSQL("DROP TABLE IF EXISTS commandes");
        onCreate(db);
    }


    public void ajouterAuPanier(int productId, String title, double price, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT quantity FROM panier WHERE product_id = ?", new String[]{String.valueOf(productId)});

        if (cursor != null && cursor.moveToFirst()) {
            int currentQty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            ContentValues values = new ContentValues();
            values.put("quantity", currentQty + quantity);

            db.update("panier", values, "product_id = ?", new String[]{String.valueOf(productId)});
            cursor.close();
        } else {
            if (cursor != null) cursor.close();

            ContentValues values = new ContentValues();
            values.put("product_id", productId);
            values.put("title", title);
            values.put("price", price);
            values.put("quantity", quantity);

            db.insert("panier", null, values);
        }
    }


    public Cursor getPanierItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, product_id, title, price, quantity FROM panier", null);
    }


    public void viderPanier() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("panier", null, null);
    }


    public void ajouterCommande(String date, int nbArticles, double montantTotal, String statut) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("nb_articles", nbArticles);
        values.put("montant_total", montantTotal);
        values.put("statut", statut); // Ex: "en_cours" ou "livree"

        db.insert("commandes", null, values);
    }
    public Cursor getCommandes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, date, nb_articles, montant_total, statut FROM commandes ORDER BY id DESC", null);
    }
}