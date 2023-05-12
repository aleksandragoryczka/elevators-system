package com.elevators.elevators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

enum isMovingUp {
    UP,
    DOWN,
    NULL
}

@Service
public class ElevatorServiceImpl implements ElevatorService {
    private List<Elevator> elevators;
    private boolean scanDirection = true; // kierunek ruchu algorytmu SCAN - to be changed
    private final int ELEVATORS_NUM = 1;
    private final int FLOORS_NUM = 10;

    public ElevatorServiceImpl() {
        this.elevators = new ArrayList<>();
        for (int i = 1; i < ELEVATORS_NUM + 1; i++) {
            this.elevators.add(new Elevator(i, 0, new ArrayList<Integer>(0), true));
        }
    }

    @Override
    public List<Elevator> getAllElevators() {
        return elevators;
    }

    @Override
    public Elevator getElevatorById(int id) {
        return elevators.stream().filter(elevator -> elevator.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void callElevator(int elevatorId, int floor) {
        Elevator elevator = getElevatorById(elevatorId);
        if (elevator.getCurrentFloor() == floor) {
            return; // winda jest juz na tym pietrze
        }
        if (elevator.getDestinationFloors().contains(floor)) {
            return; // winda juz jedzie na to pietro
        }
        List<Integer> calledElevator = new ArrayList<>();
        calledElevator = elevator.getDestinationFloors();
        calledElevator.add(floor);
        // System.out.println(calledElevator.size());
        elevator.setDestinationFloors(calledElevator);
        elevator.setMovingUp(floor > elevator.getCurrentFloor()); // jesli destinitionFloor jest wieksze od obecnego
                                                                  // floor -> isMovingUp = true -> jedzie do gory, w
                                                                  // odwrotnym rpzypadku isMovingUp = false -> jedzie na
                                                                  // dol
        this.performSimulationStep();
    }

    @Override
    public void updateElevatorState(int elevatorId, int currentFloor, List<Integer> destinationFloor,
            boolean isMovingUp) {
        Elevator elevator = getElevatorById(elevatorId);
        elevator.setCurrentFloor(currentFloor);
        elevator.setDestinationFloors(new ArrayList<Integer>(destinationFloor));
        elevator.setMovingUp(isMovingUp);
    }

    @Override
    public void performSimulationStep() {
        for (Elevator elevator : elevators) {
            List<Integer> seekSequence = new ArrayList<Integer>();
            // int nextFloor;

            System.out.println("current: " + elevator.getCurrentFloor() + "winda: " + elevator.getId());
            if (elevator.getDestinationFloors().contains(elevator.getCurrentFloor())) {
                elevator.getDestinationFloors().remove(elevator.getCurrentFloor());
                System.out.println("winda jest juz na requestowanym pietrze");
                continue;
            }
            if (elevator.getDestinationFloors().isEmpty()) {
                System.out.println("Prosze daj pietro na ktore chcesz jechac");
                continue;
            }
            System.out.println("podane pietor: " + elevator.getDestinationFloors());

            // jesli jest na drodze to jest ok -> co jesli dostanie inne zgloszenie - "z
            // drugiej strony"
            seekSequence = this.SCANAlgoritm(elevator.getDestinationFloors(), elevator.getCurrentFloor(),
                    elevator.isMovingUp());

            while (!seekSequence.isEmpty()) {
                int move = elevator.isMovingUp() ? 1 : -1;
                for (int i = elevator.getCurrentFloor() + move; i != seekSequence.get(0) + move; i += move) {
                    elevator.setCurrentFloor(i);
                    System.out.println("Jestem na: " + elevator.getCurrentFloor());
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                seekSequence.remove(0);
                elevator.setDestinationFloors(seekSequence);
            }
        }
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

    @Override
    public List<ElevatorStatus> getElevatorStatus() {
        List<ElevatorStatus> status = new ArrayList<>();
        for (Elevator elevator : elevators) {
            status.add(new ElevatorStatus(elevator.getId(), elevator.getCurrentFloor(),
                    elevator.getDestinationFloors()));
        }
        return status;
    }
}
