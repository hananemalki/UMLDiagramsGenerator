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
            g2d.setColor(Color.BLACK);
            g2d.drawString("Package: " + customPackage.getName(), x, y);
            y += 30;

            for (ClassInfo classInfo : customPackage.getClasses()) {
                drawClass(g2d, classInfo, x, y);
                y += 150; // Espacement entre les classes
            }
            y += 50; // Espacement entre les packages
        }

        // Dessiner les relations entre les classes
        drawRelations(g2d);
    }

    private void drawClass(Graphics2D g2d, ClassInfo classInfo, int x, int y) {
        // Dessiner un rectangle pour la classe
        g2d.setColor(Color.BLUE);
        g2d.drawRect(x, y, 200, 100);

        // Afficher le nom de la classe
        g2d.setColor(Color.BLACK);
        g2d.drawString("Class: " + classInfo.getName(), x + 10, y + 20);

        // Afficher les champs
        int fieldY = y + 40;
        for (FieldInfo fieldInfo : classInfo.getFields()) {
            g2d.drawString(fieldInfo.getName() + ": " + fieldInfo.getType(), x + 10, fieldY);
            fieldY += 15;
        }

        // Afficher les méthodes
        int methodY = fieldY + 10;
        for (MethodInfo methodInfo : classInfo.getMethods()) {
            g2d.drawString(methodInfo.getName() + "(): " + methodInfo.getReturnType(), x + 10, methodY);
            methodY += 15;
        }
    }

    private void drawRelations(Graphics2D g2d) {
        g2d.setColor(Color.RED); // Couleur des relations

        for (CustomPackage customPackage : packages) {
            for (ClassInfo classInfo : customPackage.getClasses()) {
                for (RelationInfo relation : classInfo.getRelations()) {
                    // Trouver les coordonnées des classes source et cible
                    Point source = findClassPosition(relation.getSource());
                    Point target = findClassPosition(relation.getTarget());

                    if (source != null && target != null) {
                        // Dessiner une flèche ou une ligne entre les classes
                        g2d.drawLine(source.x + 100, source.y + 50, target.x + 100, target.y + 50);

                        // Ajouter un libellé pour le type de relation
                        g2d.drawString(relation.getType(), (source.x + target.x) / 2, (source.y + target.y) / 2);
                    }
                }
            }
        }
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
                y += 150; // Espacement entre les classes
            }
            y += 50; // Espacement entre les packages
        }

        return null; // Classe non trouvée
    }
}