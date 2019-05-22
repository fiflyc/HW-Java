package com.example.cannon;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

class Terrain {

    private double height;
    private double width;
    private double segmentWidth;

    private double[] xPoints;
    private double[] yPoints;

    Terrain(double[] xPoints, double[] yPoints) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        height = yPoints[xPoints.length - 2];
        width = xPoints[yPoints.length - 2];

        segmentWidth = width / (xPoints.length - 3);
    }

    void draw(Group group) {
        double[] points = new double[xPoints.length * 2];
        for (int i = 0; i < xPoints.length; i++){
            points[i * 2] = xPoints[i];
            points[i * 2 + 1] = yPoints[i];
        }

        var polygon = new Polygon(points);
        polygon.setFill(Color.DARKGREEN);
        group.getChildren().add(polygon);
    }

    double heightFromWidth(double width) {
        if (width > this.width) {
            return yPoints[8];
        } else if (width < 0) {
            return yPoints[0];
        }

        int segment = (int) (width / this.width * 8);
        double part = width / segmentWidth - segment;
        double segmentHeight = yPoints[segment + 1] - yPoints[segment];

        return part * segmentHeight + yPoints[segment];
    }
}
