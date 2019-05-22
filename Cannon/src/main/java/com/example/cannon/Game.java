package com.example.cannon;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Game extends Application {

    private Group group;

    private double width;
    private double height;

    private Terrain terrain;
    private Cannon cannon;
    private Rectangle target;
    private double xTarget;
    private double yTarget;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var bounds = Screen.getPrimary().getBounds();
        width = bounds.getWidth() / 3;
        height = bounds.getHeight() / 2;
        terrain = generateTerrain();
        setTarget();
        cannon = new Cannon(0, terrain.heightFromWidth(0), 0, width);

        primaryStage.setTitle("Cannon");
        group = new Group();
        drawGameObjects();

        Scene scene = new Scene(group, width, height, Color.CADETBLUE);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    cannon.moveTo(cannon.getXPos() - 10, terrain.heightFromWidth(cannon.getXPos() - 10));
                    break;
                case RIGHT:
                    cannon.moveTo(cannon.getXPos() + 10, terrain.heightFromWidth(cannon.getXPos() + 10));
                    break;
                case UP:
                    cannon.changeAngle(Math.PI / 12);
                    break;
                case DOWN:
                    cannon.changeAngle(-Math.PI / 12);
                    break;
                case ENTER:
                    double xPos = terrain.findIntersection(cannon.getXPos(), cannon.getYPos(), cannon.getAngle());
                    if (xPos == -1) {
                        break;
                    }
                    if (Math.abs(xPos - xTarget) < 20) {
                        var text = new Text();
                        text.setX(50);
                        text.setY(50);
                        text.setFont(Font.font("Arial", 20));
                        text.setFill(Color.ORANGERED);
                        text.setText("YOU WIN");
                        group.getChildren().add(text);
                        group.getChildren().remove(target);
                        group.getChildren().add(new Circle(xPos, terrain.heightFromWidth(xPos), 10, Color.CADETBLUE));
                    } else {
                        group.getChildren().add(new Circle(xPos, terrain.heightFromWidth(xPos), 10, Color.CADETBLUE));
                    }
                    break;
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void drawGameObjects() {
        terrain.draw(group);
        cannon.draw(group);
        group.getChildren().add(target);
    }

    private void setTarget() {
        target = new Rectangle(xTarget - 10, yTarget - 10, 20, 20);
        target.setFill(Color.BLACK);
    }

    private Terrain generateTerrain() {
        double[] xPoints = new double[11];
        double[] yPoints = new double[11];
        xPoints[0] = 0.0;
        yPoints[0] = height * 4 / 5;

        for (int i = 1; i <= 8; i++) {
            xPoints[i] = i * width / 8;

            double y = yPoints[i - 1] + (Math.random() * 2 - 1) * height / 4;
            if (y >= height * 9 / 10) {
                y = height * 9 / 10;
            } else if (y <= height / 10) {
                y = height / 10;
            }

            yPoints[i] = y;
        }
        xTarget = xPoints[7];
        yTarget = yPoints[7];

        xPoints[9] = width;
        yPoints[9] = height;
        xPoints[10] = 0.0;
        yPoints[10] = height;

        return new Terrain(xPoints, yPoints);
    }
}
