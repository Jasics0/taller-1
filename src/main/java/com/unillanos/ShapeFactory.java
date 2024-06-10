package com.unillanos;

import com.google.gson.JsonObject;

import java.awt.*;

public class ShapeFactory {

    public static ShapeArea createShapeArea(JsonObject shapeObj) {
        String areaType = shapeObj.get("areaType").getAsString();
        Color color = Color.decode(shapeObj.get("color").getAsString());
        int x = shapeObj.get("x").getAsInt();
        int y = shapeObj.get("y").getAsInt();
        int width = shapeObj.get("width").getAsInt();
        int height = shapeObj.get("height").getAsInt();
        String shapeType = shapeObj.get("shapeType").getAsString();
        return createShapeArea(shapeType, x, y, x + width, y + height, color, areaType);
    }
    public static ShapeArea createShapeArea(String shapeType, int x1, int y1, int x2, int y2, Color color, String areaType) {
        switch (shapeType) {
            case "Círculo":
                return new CircleArea(x1, y1, x2, y2, color, areaType);
            case "Elipse":
                return new EllipseArea(x1, y1, x2, y2, color, areaType);
            case "Rectángulo":
                return new RectangleArea(x1, y1, x2, y2, color, areaType);
            case "Cuadrado":
                return new SquareArea(x1, y1, x2, y2, color, areaType);
            default:
                return null;
        }
    }
}
