package com.example.cannon;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

class Cannon {

    private double xPos;
    private double yPos;
    private double minX;
    private double maxX;
    private double angle;

    private Line line;
    private Circle circle;

    Cannon(double xPos, double yPos, double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
        this.xPos = xPos;
        this.yPos = yPos;
        angle = Math.PI / 4;
    }

    void draw(Group group) {
        line = new Line(xPos, yPos, xPos + 20 * Math.cos(angle), yPos - 20 * Math.sin(angle));
        line.setStroke(Color.DARKRED);
        line.setStrokeWidth(4.0);

        circle = new Circle(xPos, yPos, 10);
        circle.setFill(Color.DARKRED);

        group.getChildren().add(line);
        group.getChildren().add(circle);
    }

    void changeAngle(double delta) {
        if (delta + angle > Math.PI / 2) {
            angle = Math.PI / 2;
        } else if (delta + angle < 0) {
            angle = 0;
        }

        angle += delta;


        line.setEndX(xPos + 20 * Math.cos(angle));
        line.setEndY(yPos - 20 * Math.sin(angle));
    }

    void moveTo(double newX, double newY) {
        if (newX > maxX) {
            return;
        } else if (newX < minX) {
            return;
        }

        xPos = newX;
        yPos = newY;

        circle.setCenterX(newX);
        circle.setCenterY(newY);
        line.setStartX(xPos);
        line.setStartY(yPos);
        line.setEndX(xPos + 20 * Math.cos(angle));
        line.setEndY(yPos - 20 * Math.sin(angle));
    }

    double getXPos() {
        return xPos;
    }

    double getYPos() {
        return yPos;
    }
}
