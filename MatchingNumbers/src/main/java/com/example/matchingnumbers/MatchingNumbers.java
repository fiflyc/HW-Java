package com.example.matchingnumbers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.util.Optional;

public class MatchingNumbers extends Application {
    /** Size of the board. */
    private static int N;

    /** Field */
    private Button[][] buttons;
    private Logic logic;

    public static void main(String[] args) {
        if (args.length > 0) {
            N = Integer.valueOf(args[0]);
        } else {
            N = 0;
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (N < 1) {
            setBoardSize();
        }

        var gridPane = new GridPane();

        buttons = new Button[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                buttons[i][j] = new Button();

                buttons[i][j].setMaxWidth(Double.MAX_VALUE);
                buttons[i][j].setMaxHeight(Double.MAX_VALUE);
                GridPane.setHgrow(buttons[i][j], Priority.ALWAYS);
                GridPane.setVgrow(buttons[i][j], Priority.ALWAYS);

                int finalI = i;
                int finalJ = j;
                buttons[i][j].setOnAction(result -> {
                    logic.makeMove(finalI, finalJ);
                    updateButtons();
                    if (logic.isEnd()) {
                        showAlert("You win!");
                    }
                });

                gridPane.add(buttons[i][j], i, j);
            }
        }

        var columns = new ColumnConstraints[N];
        var rows = new RowConstraints[N];
        for (int i = 0; i < N; i++) {
            columns[i].setPercentWidth(100.0 / N);
            rows[i].setPercentHeight(100.0 / N);
        }
        gridPane.getColumnConstraints().addAll(columns);
        gridPane.getRowConstraints().addAll(rows);

        var finishGameButton = new Button("Exit");
        finishGameButton.setOnAction(result -> Platform.exit());
        gridPane.add(finishGameButton, N / 2, N);

        var scene = new Scene(gridPane, 500, 500);
        primaryStage.setTitle("Find Matches");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** Creates board if board size if incorrect. */
    private void setBoardSize() {
        var dialog = new TextInputDialog("4");
        dialog.setTitle("Find Matches");
        dialog.setHeaderText("Enter board size:");
        dialog.setContentText("Size");

        while (true) {
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(input -> {
                try {
                    N = Integer.valueOf(input);
                } catch (NumberFormatException e) {
                    showAlert("Invalid input!");
                }
            });

            if (N > 0 && N % 2 == 0) {
                return;
            }
        }
    }

    /**
     * Creates Alert Dialog Window with a message.
     * @param message to show in window
     */
    private void showAlert(String message) {
        var alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Find Matches");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /** Updates text on buttons. */
    private void updateButtons() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                buttons[i][j].setText(logic.getNumber(i, j));
            }
        }
    }
}
