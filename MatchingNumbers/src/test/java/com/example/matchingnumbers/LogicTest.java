package com.example.matchingnumbers;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class LogicTest {
    private Logic logic;
    private final int N = 10;

    @BeforeEach
    void initialize() {
        logic = new Logic(N);
    }

    @Test
    void isCreatedBoardCorrect() {
        var map = new HashMap<Integer, Integer>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                logic.makeMove(i, j);
                int number = Integer.valueOf(logic.getNumber(i, j));
                if (map.get(number) == null) {
                    map.put(number, 1);
                } else {
                    int n = map.get(number);
                    map.put(number, n + 1);
                }
            }
        }

        for (var entry: map.entrySet()) {
            assertTrue(entry.getValue() % 2 == 0);
        }
    }
}