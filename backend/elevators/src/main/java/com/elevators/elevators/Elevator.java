package com.elevators.elevators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.springframework.web.servlet.tags.form.SelectTag;

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

        while (running) {
            if (destinationFloors.contains(currentFloor)) {
                destinationFloors.remove(currentFloor);
                seekSequence.remove(currentFloor);
                System.out.println("obecnie na pietrze hmmmm: " + currentFloor);
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
            if (destinationFloors.get(0) > currentFloor) {
                isMovingUp = true;
            } else if (destinationFloors.get(0) < currentFloor) {
                isMovingUp = false;
            }
            seekSequence = this.SCANAlgoritm(destinationFloors, currentFloor, isMovingUp);
            System.out.println(seekSequence + "tutaj!!");
            int move = isMovingUp ? 1 : -1;
            int i = currentFloor;

            while (!seekSequence.isEmpty() && i != seekSequence.get(0) + move) {
                currentFloor = i;
                System.out.println(id + " jest na: " + currentFloor);
                System.out.println(isMovingUp);
                try {
                    Thread.sleep(1200);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!destinationFloors.isEmpty()) {
                    // Recompute seek sequence and continue looping
                    if (destinationFloors.contains(currentFloor)) {
                        destinationFloors.remove(destinationFloors.indexOf(currentFloor));
                        seekSequence.remove(seekSequence.indexOf(currentFloor));
                        System.out.println("obecnie na pietrze: " + currentFloor);
                        // System.out.println("w seek sequence[0]: " + seekSequence.get(0));
                        if (!seekSequence.isEmpty() && seekSequence.get(0) > currentFloor) {
                            isMovingUp = true;
                        } else {
                            isMovingUp = false;
                        }
                    }
                    seekSequence = this.SCANAlgoritm(destinationFloors, currentFloor, isMovingUp);
                }
                // elevator.setDestinationFloors(seekSequence);
                move = isMovingUp ? 1 : -1;
                i += move;
            }
            System.out.println("po dojechaniu: " + seekSequence);
        }
        System.out.println("wyszla z petli");
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
