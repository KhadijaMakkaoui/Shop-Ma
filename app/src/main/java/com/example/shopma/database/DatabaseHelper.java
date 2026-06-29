package com.example.shopma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.shopma.models.CartItem;
import com.example.shopma.models.Commande;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ShopMa.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE panier (id INTEGER PRIMARY KEY AUTOINCREMENT, product_id INTEGER, title TEXT, price REAL, quantity INTEGER)");
        db.execSQL("CREATE TABLE commandes (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, nb_articles INTEGER, montant_total REAL, statut TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS panier");
        db.execSQL("DROP TABLE IF EXISTS commandes");
        onCreate(db);
    }

    public void ajouterAuPanier(int prodId, String title, double price, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("product_id", prodId);
        cv.put("title", title);
        cv.put("price", price);
        cv.put("quantity", qty);
        db.insert("panier", null, cv);
    }

    public List<CartItem> getItemsPanier() {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM panier", null);
        if (c.moveToFirst()) {
            do {
                list.add(new CartItem(c.getInt(0), c.getInt(1), c.getString(2), c.getDouble(3), c.getInt(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void modifierQuantite(int id, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("quantity", qty);
        db.update("panier", cv, "id=?", new String[]{String.valueOf(id)});
    }

    public void supprimerItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("panier", "id=?", new String[]{String.valueOf(id)});
    }

    public void viderPanier() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("panier", null, null);
    }

    public void enregistrerCommande(String date, int nbArt, double total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", date);
        cv.put("nb_articles", nbArt);
        cv.put("montant_total", total);
        cv.put("statut", "En cours de livraison");
        db.insert("commandes", null, cv);
    }

    public List<Commande> getHistoriqueCommandes() {
        List<Commande> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM commandes ORDER BY id DESC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new Commande(c.getInt(0), c.getString(1), c.getInt(2), c.getDouble(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}