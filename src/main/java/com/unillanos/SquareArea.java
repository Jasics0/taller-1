package com.unillanos;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SquareArea extends ShapeArea {
    public SquareArea(int x1, int y1, int x2, int y2, Color color, String areaType) {
        super(new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(x2 - x1)), color, areaType,"Cuadrado");
    }

    @Override
    public double getArea() {
        Rectangle2D square = (Rectangle2D) shape;
        return square.getWidth() * square.getWidth();
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.draw(shape);
    }
}
