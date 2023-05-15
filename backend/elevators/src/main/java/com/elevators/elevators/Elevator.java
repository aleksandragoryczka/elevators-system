package com.elevators.elevators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Elevator implements Runnable {
    private int id;
    private int currentFloor;
    private List<Integer> destinationFloors;
    private DirectionsEnum direction;
    private boolean running;
    private int nextFloor;

    public Elevator(int elevatorId, int initialFloor, List<Integer> destinationFloors) {
        this.id = elevatorId;
        this.currentFloor = initialFloor;
        this.destinationFloors = destinationFloors;
        this.direction = DirectionsEnum.NULL;
        this.nextFloor = currentFloor; //TODO: is it okay?
    }

    @Override
    public void run() {
        running = true;
        int move = 0;

        while (running) {
            if(destinationFloors.size() <= 2){
                if(!destinationFloors.isEmpty()){
                    System.out.println(destinationFloors.size());
                }
                if (destinationFloors.contains(currentFloor)) {
                    destinationFloors.remove(currentFloor);
                    continue;
                }
                if (destinationFloors.isEmpty()) {
                    continue;
                }
    
                if (destinationFloors.get(0) > currentFloor) {
                    direction = DirectionsEnum.UP;
                    move = 1;
                } else if (destinationFloors.get(0) < currentFloor) {
                    direction = DirectionsEnum.DOWN;
                    move = -1;
                }
                else{
                    direction = DirectionsEnum.NULL;
                }
                int i = currentFloor;

                while (!destinationFloors.isEmpty() && direction != DirectionsEnum.NULL && i != destinationFloors.get(0) + move) {
                    currentFloor = i;
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("out: " + destinationFloors);
                    if(destinationFloors.isEmpty() && nextFloor != currentFloor){
                        destinationFloors.add(nextFloor);
                        System.out.println("in: " + destinationFloors);
                    }
                    //seekSequence = destinationFloors;
                    if (!destinationFloors.isEmpty() && destinationFloors.get(0) > currentFloor) {
                        direction = DirectionsEnum.UP;
                        move = 1;
                    } else if (!destinationFloors.isEmpty() && destinationFloors.get(0) < currentFloor) {
                        direction = DirectionsEnum.DOWN;
                        move = -1;
                    }
                    else{
                        direction = DirectionsEnum.NULL;
                    }
                    if(direction == DirectionsEnum.NULL){
                        break;
                    }
                    i += move;
                }
                direction = DirectionsEnum.NULL;
                System.out.println("DF: " + destinationFloors);
                System.out.println("obecne pietor: " + currentFloor);
                System.out.println("next choice: " + nextFloor);
            // if(destinationFloors.contains(currentFloor) && )
                if(destinationFloors.contains(currentFloor)){
                    List<Integer> temp = new ArrayList<>();
                    temp.add(currentFloor);
                    destinationFloors.removeAll(temp);
                    System.out.println("krwawie");
                    if(nextFloor != currentFloor){
                        destinationFloors.add(nextFloor);
                    }
                }
            }else{
               destinationFloors = elevatorsAlgoritm(destinationFloors, currentFloor, direction);
            }
        }
    }

    private List<Integer> elevatorsAlgoritm(List<Integer> destinationFloorsList, int currentFloor, DirectionsEnum direction) {
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

        int run = 2;
        while (run-- > 0) {
            if (direction == DirectionsEnum.DOWN) {
                for (int i = down.size() - 1; i >= 0; i--) {
                    nowOnFloor = down.get(i);

                    seekSequence.add(nowOnFloor);

                    currentFloor = nowOnFloor;
                }
                direction = DirectionsEnum.UP;
            } else if(direction == DirectionsEnum.UP) {
                for (int i = 0; i < up.size(); i++) {
                    nowOnFloor = up.get(i);

                    seekSequence.add(nowOnFloor);
                    currentFloor = nowOnFloor;
                }
                direction = DirectionsEnum.DOWN;
            }
        }
        return seekSequence;
    }

}