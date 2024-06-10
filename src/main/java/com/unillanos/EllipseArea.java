package com.unillanos;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class EllipseArea extends ShapeArea {
    public EllipseArea(int x1, int y1, int x2, int y2, Color color, String areaType) {
        super(new Ellipse2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1)), color, areaType,"Elipse");
    }

    @Override
    public double getArea() {
        Ellipse2D ellipse = (Ellipse2D) shape;
        double a = ellipse.getWidth() / 2;
        double b = ellipse.getHeight() / 2;
        return Math.PI * a * b;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.draw(shape);
    }
}
