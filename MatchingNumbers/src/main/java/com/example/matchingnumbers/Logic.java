package com.example.matchingnumbers;

import java.util.ArrayList;
import java.util.Collections;

public class Logic {

    /** Size of the board */
    private int N;
    /** Board */
    private int[][] numbers;
    /** Move type */
    private int move;
    /** States of fields of the board */
    private FieldState[][] states;

    private int xFirst;
    private int yFirst;
    private int xSecond;
    private int ySecond;

    /** Number of permanently opened fields */
    private int opened;

    public Logic(int N) {
        this.N = N;
        numbers = new int[N][N];
        states = new FieldState[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                states[i][j] = FieldState.HIDE;
            }
        }
        move = 0;
        opened = 0;

        var list = new ArrayList<Integer>();
        while (list.size() < N * N) {
            int number = (int) (Math.random() * N * N / 2);
            list.add(number);
            list.add(number);
        }
        Collections.shuffle(list);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                numbers[i][j] = list.get(i * N + j);
            }
        }
    }

    public void makeMove(int i, int j) {
        if (states[i][j] == FieldState.SHOW && move != 2) {
            return;
        }

        states[i][j] = FieldState.SHOW;
        if (move == 0) {
            xFirst = i;
            yFirst = j;
            move = 1;
        } else if (move == 1) {
            xSecond = i;
            ySecond = j;
            move = 2;

            if (numbers[xFirst][yFirst] == numbers[xSecond][ySecond]) {
                opened += 2;
            }
        } else {
            if (numbers[xFirst][yFirst] != numbers[xSecond][ySecond]) {
                states[xFirst][yFirst] = FieldState.HIDE;
                states[xSecond][ySecond] = FieldState.HIDE;
            }

            move = 0;
        }
    }

    /** Returns a string for a text in a button on position (i, j). */
    public String getNumber(int i, int j) {
        if (states[i][j] == FieldState.HIDE) {
            return "";
        } else {
            return Integer.valueOf(numbers[i][j]).toString();
        }
    }

    public boolean isEnd() {
        return opened == N * N;
    }
}
