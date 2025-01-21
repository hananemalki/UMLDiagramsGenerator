package org.mql.java.reflection.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.mql.java.reflection.models.PackageExplorer;
import org.mql.java.reflection.models.*;

public class PackageExplorerFrame extends JFrame {
    private JTextField projectNameField;
    private JTextArea resultTextArea;
    private JButton exploreButton;
    private JButton resetButton;
    private JLabel statusLabel;

    public PackageExplorerFrame() {
        setTitle("Explorateur de Packages");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.setBackground(new Color(240, 248, 255));

        JLabel projectNameLabel = new JLabel("Entrez le nom du projet : ");
        projectNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        projectNameLabel.setForeground(new Color(34, 34, 34));
        inputPanel.add(projectNameLabel);

        projectNameField = new JTextField(30);
        projectNameField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        projectNameField.setBackground(Color.WHITE);
        projectNameField.setForeground(Color.BLACK);
        inputPanel.add(projectNameField);

        exploreButton = new JButton("Explorer");
        exploreButton.setBackground(new Color(34, 139, 34));
        exploreButton.setForeground(Color.WHITE);
        exploreButton.setFont(new Font("Arial", Font.BOLD, 14));
        exploreButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        inputPanel.add(exploreButton);

        resetButton = new JButton("Réinitialiser");
        resetButton.setBackground(new Color(255, 69, 0));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        inputPanel.add(resetButton);

        statusLabel = new JLabel("Entrez le nom d'un projet et appuyez sur 'Explorer' ");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(85, 107, 47));
        add(statusLabel, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultTextArea.setBackground(new Color(245, 245, 245));
        resultTextArea.setForeground(Color.BLACK);
        resultTextArea.setBorder(BorderFactory.createLineBorder(new Color(169, 169, 169), 1));
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        add(scrollPane, BorderLayout.CENTER);

        exploreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                explorePackages();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFrame();
            }
        });
    }

    private void explorePackages() {
        String projectName = projectNameField.getText().trim();
        if (projectName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nom de projet.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            PackageExplorer packageExplorer = new PackageExplorer("D:\\Master MQL M1\\data");
            Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = packageExplorer.getPackagesAndTypes(projectName);

            StringBuilder details = new StringBuilder();
            resultTextArea.setText("");
            resultTextArea.append("\n\n------------------ Structure du Projet ------------------\n");

            if (packagesAndTypes.isEmpty()) {
                details.append("Aucun package trouvé dans le projet : ").append(projectName).append("\n");
            } else {
                for (Map.Entry<String, Map<String, List<TypeInfo>>> packageEntry : packagesAndTypes.entrySet()) {
                    String packageName = packageEntry.getKey();
                    Map<String, List<TypeInfo>> types = packageEntry.getValue();
                    
                    details.append("\n\n=== Package : ").append(packageName).append(" ===\n");
                    
                    displayTypeCategoryWithRelations(details, types, "Classes");
                    displayTypeCategoryWithRelations(details, types, "Interfaces");
                    displayTypeCategoryWithRelations(details, types, "Enums");
                    displayTypeCategoryWithRelations(details, types, "Annotations");
                }
            }

            resultTextArea.append(details.toString());
            statusLabel.setText("Structure du projet affichée avec succès.");
            statusLabel.setForeground(new Color(34, 139, 34));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'exploration du projet : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Erreur : " + ex.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }

    private void displayTypeCategoryWithRelations(StringBuilder details, Map<String, List<TypeInfo>> types, String category) {
        List<TypeInfo> typeList = types.get(category);
        if (typeList != null && !typeList.isEmpty()) {
            details.append("\n").append(category).append(" :\n");
            for (TypeInfo type : typeList) {
                details.append("  - ").append(type.toString()).append("\n");
                displayRelations(details, type);
            }
        }
    }

    private void displayRelations(StringBuilder details, TypeInfo type) {
        details.append("    Relations :\n");

        if (type instanceof ClassInfo) {
            ClassInfo classInfo = (ClassInfo) type;

            if (classInfo.getSuperClass() != null) {
                details.append("      Extends : ").append(classInfo.getSuperClass()).append("\n");
            }

            for (String iface : classInfo.getImplementedInterfaces()) {
                details.append("      Implements : ").append(iface).append("\n");
            }

            for (RelationInfo relation : classInfo.getRelations()) {
                details.append("      Uses : ").append(relation.getType()).append("\n");
            }

            for (Method method : classInfo.getClass().getDeclaredMethods()) {
                details.append("      Method : ").append(method.getName()).append("\n");
                for (Class<?> paramType : method.getParameterTypes()) {
                    details.append("        Param : ").append(paramType.getName()).append("\n");
                }
            }
        }
    }

    private void resetFrame() {
        projectNameField.setText("");
        resultTextArea.setText("");
        statusLabel.setText("Entrez le nom d'un projet et appuyez sur 'Explorer'");
        statusLabel.setForeground(new Color(85, 107, 47));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PackageExplorerFrame().setVisible(true);
            }
        });
    }
}
