package org.mql.java.reflection.test;

import java.util.List;
import java.util.Vector;

import org.mql.java.reflection.examples.Commande;
import org.mql.java.reflection.examples.Facture;
import org.mql.java.reflection.examples.Produit;
import org.mql.java.reflection.examples.Utilisateur;
import org.mql.java.reflection.models.ClassInfo;
import org.mql.java.reflection.xml.XMLGenerator;

public class Example3 {

    public Example3()  {
        exp01();
    }
    void exp01() {
        Utilisateur utilisateur = new Utilisateur("Malki Hanane", "malki.hanane@gmail.com");
        Produit produit1 = new Produit("PC HP", 3000.50);
        Produit produit2 = new Produit("LINOVO", 2000.00);
        Commande commande = new Commande("C001", "2025-01-22", utilisateur);
        commande.ajouterProduit(produit1);
        commande.ajouterProduit(produit2);
        Facture facture = new Facture(35.50, "2025-01-23", utilisateur, commande);
        utilisateur.ajouterCommande(commande);
        System.out.println(utilisateur);
        System.out.println(facture);


    }
    public static void main(String[] args) {
        new Example3();
    }
}
