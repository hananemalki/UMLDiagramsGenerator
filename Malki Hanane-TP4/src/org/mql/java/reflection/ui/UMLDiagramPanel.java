package org.mql.java.reflection.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;

import org.mql.java.reflection.models.*;

public class UMLDiagramPanel extends JPanel {
    private List<CustomPackage> packages;
    private List<Rectangle> classRectangles = new ArrayList<>();

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

        for (CustomPackage customPackage : packages) {
            // Draw package title
            g2d.setColor(new Color(70, 130, 180));
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("Package: " + customPackage.getName(), x, y);
            y += 30;

            // Draw classes
            for (ClassInfo classInfo : customPackage.getClasses()) {
                Rectangle classRect = drawClass(g2d, classInfo, x, y);
                classRectangles.add(classRect);
                y += classRect.height + 30;
            }

            y += 50;
        }

        // Draw relations after drawing all classes
        drawRelations(g2d);
    }

    private Rectangle drawClass(Graphics2D g2d, ClassInfo classInfo, int x, int y) {
        int classWidth = 250;
        int headerHeight = 40;
        int fieldHeight = classInfo.getFields().size() * 20;
        int methodHeight = classInfo.getMethods().size() * 20;
        int classHeight = headerHeight + fieldHeight + methodHeight + 20;

        // Class rectangle
        g2d.setColor(new Color(240, 248, 255));
        g2d.fillRoundRect(x, y, classWidth, classHeight, 15, 15);
        g2d.setColor(new Color(100, 149, 237));
        g2d.drawRoundRect(x, y, classWidth, classHeight, 15, 15);

        // Class name
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString(classInfo.getName(), x + 10, y + 25);

        // Fields
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        int fieldY = y + headerHeight;
        for (FieldInfo fieldInfo : classInfo.getFields()) {
            g2d.drawString(fieldInfo.getName() + ": " + fieldInfo.getType(), x + 10, fieldY);
            fieldY += 20;
        }

        // Methods
        g2d.setColor(new Color(0, 100, 0));
        int methodY = fieldY + 10;
        for (MethodInfo methodInfo : classInfo.getMethods()) {
            g2d.drawString(methodInfo.getName() + "(): " + methodInfo.getReturnType(), x + 10, methodY);
            methodY += 20;
        }

        return new Rectangle(x, y, classWidth, classHeight);
    }

    private void drawRelations(Graphics2D g2d) {
        for (CustomPackage customPackage : packages) {
            for (ClassInfo classInfo : customPackage.getClasses()) {
                for (RelationInfo relation : classInfo.getRelations()) {
                    Rectangle sourceRect = findClassRectangle(relation.getSource());
                    Rectangle targetRect = findClassRectangle(relation.getTarget());

                    if (sourceRect != null && targetRect != null) {
                        drawRelationArrow(g2d, sourceRect, targetRect, relation.getType());
                    }
                }
            }
        }
    }

    private Rectangle findClassRectangle(String className) {
        for (Rectangle rect : classRectangles) {
            // Implement logic to match class name with rectangle
            // This might require storing class names with rectangles
            // For now, returning null as a placeholder
            return null;
        }
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

        g2d.drawLine(targetX, targetY,
            (int) (targetX - arrowSize * Math.cos(angle - Math.PI / 6)),
            (int) (targetY - arrowSize * Math.sin(angle - Math.PI / 6)));

        g2d.drawLine(targetX, targetY,
            (int) (targetX - arrowSize * Math.cos(angle + Math.PI / 6)),
            (int) (targetY - arrowSize * Math.sin(angle + Math.PI / 6)));
    }

    private int calculateClassHeight(ClassInfo classInfo) {
        return 40 + (classInfo.getFields().size() * 20) + (classInfo.getMethods().size() * 20) + 20;
    }
}