package com.unillanos;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class RectangleArea extends ShapeArea {
    public RectangleArea(int x1, int y1, int x2, int y2, Color color, String areaType) {
        super(new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1)), color, areaType,"Rectángulo");
    }

    @Override
    public double getArea() {
        Rectangle2D rectangle = (Rectangle2D) shape;
        return rectangle.getWidth() * rectangle.getHeight();
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.draw(shape);
    }
}
