package org.mql.java.reflection.examples;


public class Facture {
	private double montantTotal;
    private String date;
    private Utilisateur utilisateur;
    private Commande commande;

    public Facture(double montantTotal, String date, Utilisateur utilisateur, Commande commande) {
        this.montantTotal = montantTotal;
        this.date = date;
        this.utilisateur = utilisateur;
        this.commande = commande;
    }
    
	public double getMontantTotal() {
		return montantTotal;
	}

	public void setMontantTotal(double montantTotal) {
		this.montantTotal = montantTotal;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Commande getCommande() {
		return commande;
	}

	public void setCommande(Commande commande) {
		this.commande = commande;
	}

	@Override
	public String toString() {
		return "Facture [montantTotal=" + montantTotal + ", date=" + date + ", utilisateur=" + utilisateur
				+ ", commande=" + commande + "]";
	}
    

}
