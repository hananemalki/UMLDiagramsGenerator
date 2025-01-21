package org.mql.java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.mql.java.exemple.Commande;
import org.mql.java.exemple.Facture;
import org.mql.java.exemple.Produit;
import org.mql.java.exemple.Utilisateur;
import org.mql.java.models.ClassInfo;
import org.mql.java.models.RelationInfo;
import org.mql.java.models.TypeInfo;
import org.mql.java.xml.XMIExporter;

public class Exemple2 {
	 public static void main(String[] args) {
        // Créer des objets de classes à exporter
        Utilisateur utilisateur = new Utilisateur("John Doe", "john.doe@example.com");
        
        Produit produit1 = new Produit("Laptop", 1200.50);
        Produit produit2 = new Produit("Smartphone", 800.30);

        Commande commande = new Commande("C123", "2025-01-21", utilisateur);
        commande.ajouterProduit(produit1);
        commande.ajouterProduit(produit2);
        utilisateur.ajouterCommande(commande);

        Facture facture = new Facture(2000.80, "2025-01-22", utilisateur, commande);

        // Créer les ClassInfo pour XMI
        List<TypeInfo> utilisateurs = new Vector<>();
        List<TypeInfo> produits = new Vector<>();
        List<TypeInfo> commandes = new Vector<>();
        List<TypeInfo> factures = new Vector<>();

        ClassInfo utilisateurInfo = new ClassInfo(Utilisateur.class);
        ClassInfo produitInfo = new ClassInfo(Produit.class);
        ClassInfo commandeInfo = new ClassInfo(Commande.class);
        ClassInfo factureInfo = new ClassInfo(Facture.class);

        utilisateurs.add(utilisateurInfo);
        produits.add(produitInfo);
        commandes.add(commandeInfo);
        factures.add(factureInfo);

        // Ajouter des relations entre les classes
        utilisateurInfo.addRelation(new RelationInfo("Utilisateur", "Commande", "has"));
        commandeInfo.addRelation(new RelationInfo("Commande", "Produit", "contains"));
        commandeInfo.addRelation(new RelationInfo("Commande", "Facture", "isInvoicedBy"));

        // Organiser les données dans une structure adaptée
        Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = new HashMap<>();
        Map<String, List<TypeInfo>> typesInPackage = new HashMap<>();
        typesInPackage.put("Classes", new Vector<>(Arrays.asList(utilisateurInfo, produitInfo, commandeInfo, factureInfo)));
        packagesAndTypes.put("Model", typesInPackage);

        // Exporter en XMI
        XMIExporter exporter = new XMIExporter();
        exporter.exportToXMI(packagesAndTypes, "outputProject.xmi");

        System.out.println("XMI export completed!");
    }
}
