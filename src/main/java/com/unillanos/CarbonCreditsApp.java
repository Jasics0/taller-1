package com.unillanos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CarbonCreditsApp extends JFrame {
    private JLabel imageLabel;
    private JPanel controlPanel;
    private JLabel areaLabel;
    private JComboBox<String> shapeComboBox;
    private JComboBox<String> areaTypeComboBox;
    private List<ShapeArea> drawnShapes = new ArrayList<>();
    private BufferedImage image;
    private int startX, startY, endX, endY;
    private boolean isDragging = false;
    private ShapeArea currentShapeArea = null;

    private Color selectedColor = Color.BLUE;

    public CarbonCreditsApp() {
        setTitle("Créditos de Carbono");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
                }
                Graphics2D g2d = (Graphics2D) g;
                for (ShapeArea shapeArea : drawnShapes) {
                    g2d.setColor(shapeArea.getColor());
                    g2d.setStroke(new BasicStroke(2));
                    shapeArea.draw(g2d);
                }
                if (currentShapeArea != null) {
                    g2d.setColor(currentShapeArea.getColor());
                    g2d.setStroke(new BasicStroke(2));
                    currentShapeArea.draw(g2d);
                }
            }
        };
        imageLabel.setPreferredSize(new Dimension(800, 600));
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                isDragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    endX = e.getX();
                    endY = e.getY();
                    createShapeArea(startX, startY, endX, endY);
                    currentShapeArea = null;
                    isDragging = false;
                }
            }
        });

        imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    endX = e.getX();
                    endY = e.getY();
                    updateCurrentShapeArea(startX, startY, endX, endY);
                    imageLabel.repaint();
                }
            }
        });

        add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        areaLabel = new JLabel("Área nativa: 0% de Área Evaluada");
        controlPanel.add(areaLabel);

        shapeComboBox = new JComboBox<>(new String[]{"Círculo", "Elipse", "Rectángulo", "Cuadrado"});
        controlPanel.add(shapeComboBox);

        areaTypeComboBox = new JComboBox<>(new String[]{"Área Evaluada", "Área Nativa"});
        areaTypeComboBox.addActionListener(e -> selectColor());
        controlPanel.add(areaTypeComboBox);

        JButton saveButton = new JButton("Guardar Figuras");
        saveButton.addActionListener(e -> {
            try {
                saveShapes();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        controlPanel.add(saveButton);

        JButton loadButton = new JButton("Cargar Figuras");
        loadButton.addActionListener(e -> loadShapes());
        controlPanel.add(loadButton);

        JButton loadImageButton = new JButton("Cargar Imagen");
        loadImageButton.addActionListener(e -> loadImage());
        controlPanel.add(loadImageButton);

        add(controlPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                image = ImageIO.read(fileChooser.getSelectedFile());
                imageLabel.setIcon(new ImageIcon(image));
                drawnShapes.clear();
                areaLabel.setText("Área nativa: 0% de Área Evaluada");
                pack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createShapeArea(int x1, int y1, int x2, int y2) {
        if (image != null) {
            String shapeType = (String) shapeComboBox.getSelectedItem();
            String areaType = (String) areaTypeComboBox.getSelectedItem();
            ShapeArea shapeArea = ShapeFactory.createShapeArea(shapeType, x1, y1, x2, y2, selectedColor, areaType);
            if (shapeArea != null) {
                drawnShapes.add(shapeArea);
                imageLabel.repaint();
                updateAreaLabel();
            }
        }
    }

    private void updateCurrentShapeArea(int x1, int y1, int x2, int y2) {
        String shapeType = (String) shapeComboBox.getSelectedItem();
        String areaType = (String) areaTypeComboBox.getSelectedItem();
        currentShapeArea = ShapeFactory.createShapeArea(shapeType, x1, y1, x2, y2, selectedColor, areaType);
    }

    private void updateAreaLabel() {
        if (image != null) {
            double totalEvaluatedArea = 0;
            double totalNativeArea = 0;

            for (ShapeArea shapeArea : drawnShapes) {
                double area = shapeArea.getArea();
                if ("Área Evaluada".equals(shapeArea.getAreaType())) {
                    totalEvaluatedArea += area;
                } else if ("Área Nativa".equals(shapeArea.getAreaType())) {
                    totalNativeArea += area;
                }
            }

            double percentage = (totalNativeArea / totalEvaluatedArea) * 100;
            areaLabel.setText(String.format("Área nativa: %.2f%% de Área Evaluada", percentage));
        }
    }

    private void selectColor() {
        String areaType = (String) areaTypeComboBox.getSelectedItem();
        if ("Área Evaluada".equals(areaType)) {
            selectedColor = Color.BLUE;
        } else if ("Área Nativa".equals(areaType)) {
            selectedColor = Color.RED;
        }
    }

    private void saveShapes() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            List<JsonObject> jsonObject = new ArrayList<>();
            for (ShapeArea shape : drawnShapes) {
                jsonObject.add(shape.toJson());
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(jsonObject, writer);
            }catch (IOException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "Figuras guardadas exitosamente");
        }
    }

    private void loadShapes() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (Reader reader = new FileReader(file)) {
                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<List<JsonObject>>(){}.getType();
                List<JsonObject> jsonObjects = gson.fromJson(reader, listType);                drawnShapes.clear();
                ShapeArea shapeArea;
                for (int i = 0; i < jsonObjects.size(); i++) {

                    shapeArea = ShapeFactory.createShapeArea(jsonObjects.get(i));
                    drawnShapes.add(shapeArea);
                }
                imageLabel.repaint();
                updateAreaLabel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CarbonCreditsApp app = new CarbonCreditsApp();
            app.setVisible(true);
        });
    }
}
