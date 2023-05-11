package com.elevators.elevators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ElevatorServiceImpl implements ElevatorService {
    private List<Elevator> elevators;
    private boolean scanDirection = true; // kierunek ruchu algorytmu SCAN - to be changed
    private final int ELEVATORS_NUM = 4;

    public ElevatorServiceImpl() {
        this.elevators = new ArrayList<>();
        for (int i = 1; i < ELEVATORS_NUM + 1; i++) {
            this.elevators.add(new Elevator(i, 1, 1, true));
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
        if (elevator.getDestinationFloor() == floor) {
            return; // winda juz jedzie na to pietro
        }
        elevator.setDestinationFloor(floor);
        elevator.setMovingUp(floor > elevator.getCurrentFloor()); // jesli destinitionFloor jest wieksze od obecnego
                                                                  // floor -> isMovingUp = true -> jedzie do gory, w
                                                                  // odwrotnym rpzypadku isMovingUp = false -> jedzie na
                                                                  // dol
    }

    @Override
    public void updateElevatorState(int elevatorId, int currentFloor, int destinationFloor, boolean isMovingUp) {
        Elevator elevator = getElevatorById(elevatorId);
        elevator.setCurrentFloor(currentFloor);
        elevator.setDestinationFloor(destinationFloor);
        elevator.setMovingUp(isMovingUp);
    }

    @Override
    public void performSimulationStep() {
        // this.callElevator(9, 5);
        for (Elevator elevator : elevators) {
            System.out.println("current: " + elevator.getCurrentFloor() + "winda: " + elevator.getId());
            if (elevator.getCurrentFloor() == elevator.getDestinationFloor()) {
                System.out.println("jest na pietre docelowym");
                continue;
                // widna jest juz na pietrze docelowym
            }
            int direction = elevator.isMovingUp() ? 1 : -1; // esli winda jedzie do gory -> direciton = 1; jesli na dol:
                                                            // direction = -1
            int nextFloor = elevator.getCurrentFloor() + direction;
            if (nextFloor == elevator.getDestinationFloor()) {
                // winda dotarla do pietra docelowego
                elevator.setCurrentFloor(nextFloor);
            } else {
                // winda jest wlasnie w ruchu
                elevator.setCurrentFloor(nextFloor);
                // zmiana kierunku ruchu SCAN
                if (nextFloor == 10 && scanDirection) { // todo: user podaje liczbe pieter
                    elevator.setMovingUp(false);
                    scanDirection = false;
                } else if (nextFloor == 1 && !scanDirection) {
                    elevator.setMovingUp(true);
                    scanDirection = true;
                }
            }
        }
    }

    @Override
    public List<ElevatorStatus> getElevatorStatus() {
        List<ElevatorStatus> status = new ArrayList<>();
        for (Elevator elevator : elevators) {
            status.add(new ElevatorStatus(elevator.getId(), elevator.getCurrentFloor(),
                    elevator.getDestinationFloor()));
        }
        return status;
    }
}
