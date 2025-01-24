package org.mql.java.reflection.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.mql.java.reflection.models.*;

public class UMLDiagramPanel extends JPanel {
    private List<CustomPackage> packages;

    public UMLDiagramPanel(List<CustomPackage> packages) {
        this.packages = packages;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int x = 50; // Position de départ en X
        int y = 50; // Position de départ en Y

        // Parcourir les packages et dessiner les classes
        for (CustomPackage customPackage : packages) {
            g2d.setColor(new Color(0, 0, 128)); // Couleur des packages
            g2d.drawString("Package: " + customPackage.getName(), x, y);
            y += 30;

            for (ClassInfo classInfo : customPackage.getClasses()) {
                drawClass(g2d, classInfo, x, y);
                y += calculateClassHeight(classInfo) + 20; // Espacement entre les classes
            }
            y += 50; // Espacement entre les packages
        }

        // Dessiner les relations entre les classes
        drawRelations(g2d);
    }

    private void drawClass(Graphics2D g2d, ClassInfo classInfo, int x, int y) {
        int classWidth = 200;
        int classHeight = calculateClassHeight(classInfo);

        // Dessiner un rectangle pour la classe
        g2d.setColor(new Color(173, 216, 230)); // Couleur de fond des classes
        g2d.fillRect(x, y, classWidth, classHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, classWidth, classHeight);

        // Afficher le nom de la classe
        g2d.setColor(new Color(0, 0, 139)); // Couleur du nom de la classe
        g2d.drawString("Class: " + classInfo.getName(), x + 10, y + 20);

        // Afficher les champs
        int fieldY = y + 40;
        for (FieldInfo fieldInfo : classInfo.getFields()) {
            g2d.setColor(new Color(0, 100, 0)); // Couleur des champs
            g2d.drawString(fieldInfo.getName() + ": " + fieldInfo.getType(), x + 10, fieldY);
            fieldY += 15;
        }

        // Afficher les méthodes
        int methodY = fieldY + 10;
        for (MethodInfo methodInfo : classInfo.getMethods()) {
            g2d.setColor(new Color(139, 0, 0)); // Couleur des méthodes
            g2d.drawString(methodInfo.getName() + "(): " + methodInfo.getReturnType(), x + 10, methodY);
            methodY += 15;
        }
    }

    private void drawRelations(Graphics2D g2d) {
        for (CustomPackage customPackage : packages) {
            for (ClassInfo classInfo : customPackage.getClasses()) {
                for (RelationInfo relation : classInfo.getRelations()) {
                    // Trouver les coordonnées des classes source et cible
                    Point source = findClassPosition(relation.getSource());
                    Point target = findClassPosition(relation.getTarget());

                    if (source != null && target != null) {
                        // Dessiner une flèche ou une ligne entre les classes
                        drawArrow(g2d, source, target, relation.getType());
                    }
                }
            }
        }
    }

    private void drawArrow(Graphics2D g2d, Point source, Point target, String relationType) {
        int startX = source.x + 100;
        int startY = source.y + 50;
        int endX = target.x + 100;
        int endY = target.y + 50;

        // Choisir la couleur en fonction du type de relation
        switch (relationType) {
            case "extends":
                g2d.setColor(Color.BLUE); // Héritage
                break;
            case "implements":
                g2d.setColor(Color.GREEN); // Implémentation d'interface
                break;
            case "composition":
                g2d.setColor(Color.RED); // Composition
                break;
            case "aggregation":
                g2d.setColor(Color.ORANGE); // Agrégation
                break;
            case "uses":
                g2d.setColor(Color.MAGENTA); // Utilisation
                break;
            case "returns":
                g2d.setColor(Color.CYAN); // Retour
                break;
            default:
                g2d.setColor(Color.BLACK); // Par défaut
                break;
        }

        // Dessiner la ligne
        g2d.drawLine(startX, startY, endX, endY);

        // Dessiner la flèche
        drawArrowHead(g2d, startX, startY, endX, endY);
    }

    private void drawArrowHead(Graphics2D g2d, int startX, int startY, int endX, int endY) {
        int arrowSize = 10; // Taille de la flèche
        double angle = Math.atan2(endY - startY, endX - startX);

        // Dessiner la pointe de la flèche
        g2d.drawLine(endX, endY,
                (int) (endX - arrowSize * Math.cos(angle - Math.PI / 6)),
                (int) (endY - arrowSize * Math.sin(angle - Math.PI / 6)));

        g2d.drawLine(endX, endY,
                (int) (endX - arrowSize * Math.cos(angle + Math.PI / 6)),
                (int) (endY - arrowSize * Math.sin(angle + Math.PI / 6)));
    }

    private Point findClassPosition(String className) {
        int x = 50;
        int y = 50;

        for (CustomPackage customPackage : packages) {
            y += 30; // Espace pour le nom du package

            for (ClassInfo classInfo : customPackage.getClasses()) {
                if (classInfo.getName().equals(className)) {
                    return new Point(x, y);
                }
                y += calculateClassHeight(classInfo) + 20; // Espacement entre les classes
            }
            y += 50; // Espacement entre les packages
        }

        return null; // Classe non trouvée
    }

    private int calculateClassHeight(ClassInfo classInfo) {
        int height = 40; // Hauteur de base pour le nom de la classe
        height += classInfo.getFields().size() * 15; // Hauteur pour les champs
        height += classInfo.getMethods().size() * 15; // Hauteur pour les méthodes
        return height;
    }
}