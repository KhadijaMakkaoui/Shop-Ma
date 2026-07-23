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
        // Table 6.1 : panier
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

    /**
     * Ajoute un produit au panier ou incrémente sa quantité s'il existe déjà
     */
    public void ajouterAuPanier(int productId, String title, double price, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Vérification de l'existence du produit dans le panier
        Cursor cursor = db.rawQuery("SELECT quantity FROM panier WHERE product_id = ?", new String[]{String.valueOf(productId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Produit existant -> Mise à jour de la quantité
            int currentQty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            ContentValues values = new ContentValues();
            values.put("quantity", currentQty + quantity);

            db.update("panier", values, "product_id = ?", new String[]{String.valueOf(productId)});
            cursor.close();
        } else {
            // Nouveau produit -> Insertion
            if (cursor != null) cursor.close();

            ContentValues values = new ContentValues();
            values.put("product_id", productId);
            values.put("title", title);
            values.put("price", price);
            values.put("quantity", quantity);

            db.insert("panier", null, values);
        }
    }

    /**
     * Récupère tous les éléments du panier
     * Note: Alias 'id AS _id' indispensable pour le CursorAdapter d'Android
     */
    public Cursor getPanierItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, product_id, title, price, quantity FROM panier", null);
    }

    /**
     * Vide complètement la table panier
     */
    public void viderPanier() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("panier", null, null);
    }

    /**
     * Enregistre une nouvelle commande
     */
    public void ajouterCommande(String date, int nbArticles, double montantTotal, String statut) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("nb_articles", nbArticles);
        values.put("montant_total", montantTotal);
        values.put("statut", statut); // Ex: "en_cours" ou "livree"

        db.insert("commandes", null, values);
    }
}