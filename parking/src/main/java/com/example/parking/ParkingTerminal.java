package com.example.parking;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingTerminal {

    /** Current number of free places. */
    private AtomicInteger free;
    /** Maximum of free places. */
    private AtomicInteger max;

    /**
     * Constructor.
     * @param places maximum of free places
     */
    ParkingTerminal(int places) {
        free = new AtomicInteger(0);
        max = new AtomicInteger(places);
    }

    boolean driveIn() {
        AtomicBoolean result = new AtomicBoolean(false);
        free.updateAndGet(x -> {
           if (x > 0) {
               result.set(true);
               return x - 1;
           } else {
               return 0;
           }
        });
        return result.get();
    }

    void driveOut() {
        free.updateAndGet(x -> {
           if (x < max.get()) {
               return x + 1;
           } else {
               return max.get();
            }
        });
    }
}
