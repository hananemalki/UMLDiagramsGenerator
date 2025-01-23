package org.mql.java.reflection.ui;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.Map;
import java.util.List;

import org.mql.java.reflection.models.*;
import org.mql.java.reflection.xml.*;

public class UMLGeneratorApp extends JFrame {
    private JTextField workspacePathField;
    private JTextField projectNameField;
    private JButton selectWorkspaceButton;
    private JButton generateButton;
    private JTextArea progressArea;
    private JProgressBar progressBar;

    public UMLGeneratorApp() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Générateur de Diagrammes UML");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panneau de configuration
        JPanel configPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        configPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Chemin du workspace
        configPanel.add(new JLabel("Chemin du Workspace:"));
        workspacePathField = new JTextField();
        configPanel.add(workspacePathField);

        selectWorkspaceButton = new JButton("Sélectionner");
        selectWorkspaceButton.addActionListener(e -> selectWorkspace());
        configPanel.add(selectWorkspaceButton);

        // Nom du projet
        configPanel.add(new JLabel("Nom du Projet:"));
        projectNameField = new JTextField();
        configPanel.add(projectNameField);

        // Bouton de génération
        generateButton = new JButton("Générer Diagramme UML");
        generateButton.addActionListener(e -> generateUMLDiagram());
        configPanel.add(generateButton);

        // Zone de progression
        progressBar = new JProgressBar(0, 100);
        configPanel.add(progressBar);

        // Zone de log
        progressArea = new JTextArea();
        progressArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(progressArea);

        add(configPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void selectWorkspace() {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setDialogTitle("Sélectionner le dossier Workspace");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = chooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = chooser.getSelectedFile();
            workspacePathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void generateUMLDiagram() {
        String workspacePath = workspacePathField.getText();
        String projectName = projectNameField.getText();

        if (workspacePath.isEmpty() || projectName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un workspace et un nom de projet", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Exécution dans un thread séparé pour ne pas bloquer l'interface
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // Étape 1 : Extraction des packages et classes
                    publish("Extraction des packages et classes...");
                    progressBar.setValue(20);
                    PackageExplorer explorer = new PackageExplorer(workspacePath);
                    Map<String, Map<String, List<TypeInfo>>> packagesAndTypes = 
                        explorer.getPackagesAndTypes(projectName);

                    // Étape 2 : Exportation XML
                    publish("Exportation XML...");
                    progressBar.setValue(40);
                    XMLExporter xmlExporter = new XMLExporter();
                    xmlExporter.exportToXML(packagesAndTypes, 
                        workspacePath + "/" + projectName + "_structure.xml");

                    // Étape 3 : Exportation XMI
                    publish("Conversion en XMI...");
                    progressBar.setValue(60);
                    XMIExporter xmiExporter = new XMIExporter();
                    xmiExporter.exportToXMI(packagesAndTypes, 
                        workspacePath + "/" + projectName + "_model.xmi");

                    // Étape 4 : Parsing XMI (optionnel)
                    publish("Parsing XMI...");
                    progressBar.setValue(80);
                    XMIParser xmiParser = new XMIParser(
                        workspacePath + "/" + projectName + "_model.xmi");

                    // Étape finale
                    publish("Génération terminée !");
                    progressBar.setValue(100);

                } catch (Exception e) {
                    publish("Erreur : " + e.getMessage());
                    progressBar.setValue(0);
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String message : chunks) {
                    progressArea.append(message + "\n");
                }
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UMLGeneratorApp().setVisible(true);
        });
    }
}