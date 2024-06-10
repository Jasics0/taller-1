package com.unillanos;

import com.google.gson.JsonObject;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public abstract class ShapeArea {
    protected Shape shape;
    protected Color color;
    protected String areaType;

    protected String shapeType;
    public ShapeArea(Shape shape, Color color, String areaType, String shapeType) {
        this.shape = shape;
        this.color = color;
        this.areaType = areaType;
        this.shapeType = shapeType;
    }

    public Color getColor() {
        return color;
    }

    public String getAreaType() {
        return areaType;
    }

    public String serializeColor() {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    // Método para deserializar una cadena RGB a un objeto Color
    public void deserializeColor(String colorStr) {
        int r = Integer.parseInt(colorStr.substring(1, 3), 16);
        int g = Integer.parseInt(colorStr.substring(3, 5), 16);
        int b = Integer.parseInt(colorStr.substring(5, 7), 16);
        this.color = new Color(r, g, b);
    }

    // Método para convertir la figura a un objeto JSON
    public JsonObject toJson() {
        JsonObject shapeObj = new JsonObject();
        shapeObj.addProperty("areaType", areaType);
        shapeObj.addProperty("color", serializeColor());
        shapeObj.addProperty("x", (int) shape.getBounds2D().getX());
        shapeObj.addProperty("y", (int) shape.getBounds2D().getY());
        shapeObj.addProperty("width", (int) shape.getBounds2D().getWidth());
        shapeObj.addProperty("height", (int) shape.getBounds2D().getHeight());
        shapeObj.addProperty("shapeType", shapeType);
        //Guardar el tipo de figura circulo, cuadrado, rectangulo, elipse



        return shapeObj;
    }

    // Método para construir una figura desde un objeto JSON


    public abstract double getArea();

    public abstract void draw(Graphics2D g2d);
}
