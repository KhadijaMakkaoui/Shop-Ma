package com.example.shopma.models;


public class Commande {
    private int id;
    private String date;
    private int nbArticles;
    private double montantTotal;
    private String statut;

    public Commande(int id, String date, int nbArticles, double montantTotal, String statut) {
        this.id = id;
        this.date = date;
        this.nbArticles = nbArticles;
        this.montantTotal = montantTotal;
        this.statut = statut;
    }

    public int getId() { return id; }
    public String getDate() { return date; }
    public int getNbArticles() { return nbArticles; }
    public double getMontantTotal() { return montantTotal; }
    public String getStatut() { return statut; }
}