package org.mql.java.reflection.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

import org.mql.java.reflection.models.*;

public class UMLDiagramPanel extends JPanel {
    private List<CustomPackage> packages;
    private List<Rectangle> classRectangles = new Vector<>();
    
    public UMLDiagramPanel(List<CustomPackage> packages) {
        this.packages = packages;
        setBackground(Color.WHITE);
        setPreferredSize(calculatePreferredSize());
    }

    private Dimension calculatePreferredSize() {
        int width = 1200;
        int height = calculateTotalHeight();
        return new Dimension(width, height);
    }

    private int calculateTotalHeight() {
        int height = 100;
        for (CustomPackage customPackage : packages) {
            height += 50;
            for (ClassInfo classInfo : customPackage.getClasses()) {
                height += calculateClassHeight(classInfo) + 30;
            }
            height += 50;
        }
        return height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        classRectangles.clear();
        int x = 50;
        int y = 50;
        int maxHeight = 0;
        int maxClassesPerRow = 5; 
        int classesInCurrentRow = 0;
        for (CustomPackage customPackage : packages) {
            for (ClassInfo classInfo : customPackage.getClasses()) {
                Rectangle classRect = drawClass(g2d, classInfo, x, y);
                classRectangles.add(classRect);
                x += classRect.width + 20; 
                maxHeight = Math.max(maxHeight, classRect.height);
                classesInCurrentRow++;
                if (classesInCurrentRow >= maxClassesPerRow) {
                    x = 50;
                    y += maxHeight + 40;
                    maxHeight = 0;
                    classesInCurrentRow = 0;
                }
            }
            x = 50;
            y += maxHeight + 70;
            maxHeight = 0;
            classesInCurrentRow = 0;
        }
        drawRelations(g2d);
    }
    
    private Rectangle drawClass(Graphics2D g2d, ClassInfo classInfo, int x, int y) {
        int classWidth = 200; 
        int headerHeight = 30;
        int fieldHeight = Math.min(classInfo.getFields().size() * 15, 60); 
        int methodHeight = Math.min(classInfo.getMethods().size() * 15, 90); 
        int classHeight = headerHeight + fieldHeight + methodHeight + 20;
        Color backgroundColor = new Color(255, 255, 225);
        g2d.setColor(backgroundColor);
        g2d.fillRect(x, y, classWidth, classHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, classWidth, classHeight);
        g2d.drawLine(x, y + headerHeight, x + classWidth, y + headerHeight);
        g2d.drawLine(x, y + headerHeight + fieldHeight + 10, x + classWidth, y + headerHeight + fieldHeight + 10);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(classInfo.getName(), x + 10, y + 20);
    
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        int fieldY = y + headerHeight + 15;
        for (FieldInfo fieldInfo : classInfo.getFields()) {
            g2d.drawString(fieldInfo.getName() + ": " + fieldInfo.getType(), x + 10, fieldY);
            fieldY += 15;
            if (fieldY - y > headerHeight + fieldHeight) break; 
        }

        g2d.setColor(Color.BLACK);
        int methodY = fieldY + 10;
        for (MethodInfo methodInfo : classInfo.getMethods()) {
            g2d.drawString(methodInfo.getName() + "(): " + methodInfo.getReturnType(), x + 10, methodY);
            methodY += 15;
            if (methodY - y > classHeight) break; 
        }
    
        Rectangle classRect = new Rectangle(x, y, classWidth, classHeight);
        classRectangles.add(classRect); 
        return classRect;
    }

    private void drawRelations(Graphics2D g2d) {
        for (CustomPackage customPackage : packages) {
            for (ClassInfo classInfo : customPackage.getClasses()) {
                System.out.println("Total relations: " + classInfo.getRelations().size());
                for (RelationInfo relation : classInfo.getRelations()) {
                    System.out.println("Relation details: " + relation);

                    if (relation.getSource().startsWith("java.") || relation.getTarget().startsWith("java.")) {
                        System.out.println("Ignorer la relation externe : " + relation);
                        continue;
                    }
                    System.out.println("Relation: " + relation);
                    Rectangle sourceRect = findClassRectangle(relation.getSource());
                    Rectangle targetRect = findClassRectangle(relation.getTarget());
                    System.out.println("Source Rectangle: " + sourceRect);
                    System.out.println("Target Rectangle: " + targetRect);
                    if (sourceRect != null && targetRect != null) {
                        drawRelationArrow(g2d, sourceRect, targetRect, relation.getType());
                    } else {
                        System.out.println("Could not find rectangles for relation: " + relation);
                    }
                }
            }
        }
    }

    private Rectangle findClassRectangle(String className) {
        System.out.println("Searching for class: " + className);
        for (CustomPackage customPackage : packages) {
            
            for (ClassInfo classInfo : customPackage.getClasses()) {
                if (classInfo.getName().equals(className) || 
                    (customPackage.getName() + "." + classInfo.getName()).equals(className)) {
                    int index = customPackage.getClasses().indexOf(classInfo);
                    if (index >= 0 && index < classRectangles.size()) {
                        return classRectangles.get(index);
                    }
                }
            }
        }
        System.out.println("Classe non trouvée : " + className);
        return null;
    }

    private void drawRelationArrow(Graphics2D g2d, Rectangle source, Rectangle target, String relationType) {
        int sourceX = source.x + source.width / 2;
        int sourceY = source.y + source.height / 2;
        int targetX = target.x + target.width / 2;
        int targetY = target.y + target.height / 2;
        g2d.setStroke(new BasicStroke(2.0f));
        switch (relationType.toLowerCase()) {
            case "extends":
                g2d.setColor(Color.RED);
                drawArrowWithHollowTriangle(g2d, sourceX, sourceY, targetX, targetY);
                break;
            case "implements":
                g2d.setColor(Color.GREEN);
                drawDashedArrow(g2d, sourceX, sourceY, targetX, targetY, true);
                break;
            case "composition":
                g2d.setColor(Color.BLUE);
                drawArrowWithFilledDiamond(g2d, sourceX, sourceY, targetX, targetY);
                break;
            case "aggregation":
                g2d.setColor(Color.ORANGE);
                drawArrowWithHollowDiamond(g2d, sourceX, sourceY, targetX, targetY);
                break;
            case "uses":
                g2d.setColor(Color.MAGENTA);
                drawDashedArrow(g2d, sourceX, sourceY, targetX, targetY, false);
                break;
            default:
                g2d.setColor(Color.BLACK);
                g2d.drawLine(sourceX, sourceY, targetX, targetY);
        }
    }

    private void drawArrowWithHollowTriangle(Graphics2D g2d, int sourceX, int sourceY, int targetX, int targetY) {
        g2d.drawLine(sourceX, sourceY, targetX, targetY);
        double angle = Math.atan2(targetY - sourceY, targetX - sourceX);
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        xPoints[0] = targetX;
        yPoints[0] = targetY;
        xPoints[1] = (int) (targetX - 10 * Math.cos(angle - Math.PI / 6));
        yPoints[1] = (int) (targetY - 10 * Math.sin(angle - Math.PI / 6));
        xPoints[2] = (int) (targetX - 10 * Math.cos(angle + Math.PI / 6));
        yPoints[2] = (int) (targetY - 10 * Math.sin(angle + Math.PI / 6));
        g2d.drawPolygon(xPoints, yPoints, 3);
    }

    private void drawDashedArrow(Graphics2D g2d, int sourceX, int sourceY, int targetX, int targetY, boolean isHollowTriangle) {
        Stroke oldStroke = g2d.getStroke();
        float[] dash = {5.0f, 5.0f};
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
        g2d.drawLine(sourceX, sourceY, targetX, targetY);
        double angle = Math.atan2(targetY - sourceY, targetX - sourceX);
        if (isHollowTriangle) {
            int[] xPoints = new int[3];
            int[] yPoints = new int[3];
            xPoints[0] = targetX;
            yPoints[0] = targetY;
            xPoints[1] = (int) (targetX - 10 * Math.cos(angle - Math.PI / 6));
            yPoints[1] = (int) (targetY - 10 * Math.sin(angle - Math.PI / 6));
            xPoints[2] = (int) (targetX - 10 * Math.cos(angle + Math.PI / 6));
            yPoints[2] = (int) (targetY - 10 * Math.sin(angle + Math.PI / 6));
            g2d.drawPolygon(xPoints, yPoints, 3);
        } else {
            drawArrowHead(g2d, sourceX, sourceY, targetX, targetY);
        }
        
        g2d.setStroke(oldStroke);
    }

    private void drawArrowWithFilledDiamond(Graphics2D g2d, int sourceX, int sourceY, int targetX, int targetY) {
        g2d.drawLine(sourceX, sourceY, targetX, targetY);
        double angle = Math.atan2(targetY - sourceY, targetX - sourceX);
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        xPoints[0] = targetX;
        yPoints[0] = targetY;
        xPoints[1] = (int) (targetX - 10 * Math.cos(angle - Math.PI / 4));
        yPoints[1] = (int) (targetY - 10 * Math.sin(angle - Math.PI / 4));
        xPoints[2] = (int) (targetX - 20 * Math.cos(angle));
        yPoints[2] = (int) (targetY - 20 * Math.sin(angle));
        xPoints[3] = (int) (targetX - 10 * Math.cos(angle + Math.PI / 4));
        yPoints[3] = (int) (targetY - 10 * Math.sin(angle + Math.PI / 4));
        g2d.fillPolygon(xPoints, yPoints, 4);
    }

    private void drawArrowWithHollowDiamond(Graphics2D g2d, int sourceX, int sourceY, int targetX, int targetY) {
        g2d.drawLine(sourceX, sourceY, targetX, targetY);
        double angle = Math.atan2(targetY - sourceY, targetX - sourceX);
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        xPoints[0] = targetX;
        yPoints[0] = targetY;
        xPoints[1] = (int) (targetX - 10 * Math.cos(angle - Math.PI / 4));
        yPoints[1] = (int) (targetY - 10 * Math.sin(angle - Math.PI / 4));
        xPoints[2] = (int) (targetX - 20 * Math.cos(angle));
        yPoints[2] = (int) (targetY - 20 * Math.sin(angle));
        xPoints[3] = (int) (targetX - 10 * Math.cos(angle + Math.PI / 4));
        yPoints[3] = (int) (targetY - 10 * Math.sin(angle + Math.PI / 4));
        g2d.drawPolygon(xPoints, yPoints, 4);
    }

    private void drawArrowHead(Graphics2D g2d, int sourceX, int sourceY, int targetX, int targetY) {
        double angle = Math.atan2(targetY - sourceY, targetX - sourceX);
        int arrowSize = 10;
        g2d.drawLine(targetX, targetY,(int) (targetX - arrowSize * Math.cos(angle - Math.PI / 6)),(int) (targetY - arrowSize * Math.sin(angle - Math.PI / 6)));
        g2d.drawLine(targetX, targetY,(int) (targetX - arrowSize * Math.cos(angle + Math.PI / 6)),(int) (targetY - arrowSize * Math.sin(angle + Math.PI / 6)));
    }

    private int calculateClassHeight(ClassInfo classInfo) {
        return 40 + (classInfo.getFields().size() * 20) + (classInfo.getMethods().size() * 20) + 20;
    }
}