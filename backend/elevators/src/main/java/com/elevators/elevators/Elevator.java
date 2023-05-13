package com.elevators.elevators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Elevator implements Runnable {
    private int id;
    private int currentFloor;
    private List<Integer> destinationFloors;
    private boolean isMovingUp;
    private boolean running;

    public Elevator(int elevatorId, int initialFloor, List<Integer> destinationFloors, boolean isMovingUp) {
        this.id = elevatorId;
        this.currentFloor = initialFloor;
        this.destinationFloors = destinationFloors;
        this.running = false;
        this.isMovingUp = isMovingUp;
    }

    @Override
    public void run() {
        running = true;
        List<Integer> seekSequence = new ArrayList<Integer>();
        // int nextFloor;

        // System.out.println("current: " + elevator.getCurrentFloor() + "winda: " +
        // elevator.getId());
        while (running) {
            if (destinationFloors.contains(currentFloor)) {
                destinationFloors.remove(currentFloor);
                // elevator.getDestinationFloors().remove(elevator.getCurrentFloor());
                System.out.println("winda jest juz na requestowanym pietrze");
                continue;
            }
            if (destinationFloors.isEmpty()) {
                // System.out.println("Prosze daj pietro na ktore chcesz jechac");
                continue;
            }
            System.out.println("podane piera: " + destinationFloors);

            // jesli jest na drodze to jest ok -> co jesli dostanie inne zgloszenie - "z
            // drugiej strony"
            seekSequence = this.SCANAlgoritm(destinationFloors, currentFloor, isMovingUp);

            while (!seekSequence.isEmpty()) {
                int move = isMovingUp ? 1 : -1;
                for (int i = currentFloor + move; i != seekSequence.get(0) + move; i += move) {
                    currentFloor = i;
                    System.out.println(id + " jest na: " + currentFloor);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                seekSequence.remove(0);
                destinationFloors = seekSequence;
                // elevator.setDestinationFloors(seekSequence);
            }
        }
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    private List<Integer> SCANAlgoritm(List<Integer> destinationFloorsList, int currentFloor, boolean isMovingUp) {
        Vector<Integer> up = new Vector<Integer>(), down = new Vector<Integer>();
        int nowOnFloor = 0;
        Vector<Integer> seekSequence = new Vector<Integer>();

        for (int i = 0; i < destinationFloorsList.size(); i++) {
            if (destinationFloorsList.get(i) < currentFloor) {
                down.add(destinationFloorsList.get(i));
            }
            if (destinationFloorsList.get(i) > currentFloor) {
                up.add(destinationFloorsList.get(i));
            }
        }

        Collections.sort(down);
        Collections.sort(up);

        // napisac wybor czy ma najpierw zaczac poruszac sie w prawo czy w lewo
        int run = 2;
        while (run-- > 0) {
            if (!isMovingUp) {
                for (int i = down.size() - 1; i >= 0; i--) {
                    nowOnFloor = down.get(i);

                    // do pozniejszego wypisywania
                    seekSequence.add(nowOnFloor);

                    currentFloor = nowOnFloor;
                }
                isMovingUp = true;
            } else {
                for (int i = 0; i < up.size(); i++) {
                    nowOnFloor = up.get(i);

                    seekSequence.add(nowOnFloor);
                    currentFloor = nowOnFloor;
                }
                isMovingUp = false;
            }
        }
        System.out.println("seekSequence" + seekSequence);
        return seekSequence;
    }

}
