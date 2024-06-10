package com.unillanos;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class CircleArea extends ShapeArea {
    public CircleArea(int x1, int y1, int x2, int y2, Color color, String areaType) {
        super(new Ellipse2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(x2 - x1)), color, areaType,"Círculo");
    }

    @Override
    public double getArea() {
        Ellipse2D ellipse = (Ellipse2D) shape;
        double radius = ellipse.getWidth() / 2;
        return Math.PI * radius * radius;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.draw(shape);
    }
}

