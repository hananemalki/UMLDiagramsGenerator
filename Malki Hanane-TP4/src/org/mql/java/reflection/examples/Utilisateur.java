package org.mql.java.reflection.examples;


import java.util.List;
import java.util.Vector;

public class Utilisateur {
    private String nom;
    private String email;
    private List<Commande> commandes;

    public Utilisateur(String nom, String email) {
        this.nom = nom;
        this.email = email;
        this.commandes = new Vector<>();
    }

    public void ajouterCommande(Commande commande) {
        commandes.add(commande);
    }

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Commande> getCommandes() {
		return commandes;
	}

	public void setCommandes(List<Commande> commandes) {
		this.commandes = commandes;
	}

	@Override
	public String toString() {
		return "Utilisateur [nom=" + nom + ", email=" + email + ", commandes=" + commandes + "]";
	}
	
	
    
}
