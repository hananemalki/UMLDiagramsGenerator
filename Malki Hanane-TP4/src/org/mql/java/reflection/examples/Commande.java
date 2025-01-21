package org.mql.java.reflection.examples;


import java.util.List;
import java.util.Vector;

public class Commande {
	private String id;
    private String date;
    private List<Produit> produits;
    private Utilisateur utilisateur;

    public Commande(String id, String date, Utilisateur utilisateur) {
        this.id = id;
        this.date = date;
        this.produits = new Vector<>();
        this.utilisateur = utilisateur;
    }

    public void ajouterProduit(Produit produit) {
        produits.add(produit);
    }
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Produit> getProduits() {
		return produits;
	}

	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	@Override
	public String toString() {
		return "Commande [id=" + id + ", date=" + date + ", produits=" + produits + ", utilisateur=" + utilisateur
				+ "]";
	}
	
}

