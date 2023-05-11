package com.elevators.elevators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ElevatorServiceImpl implements ElevatorService {
    private List<Elevator> elevators;
    private boolean scanDirection = true; // kierunek ruchu algorytmu SCAN - to be changed
    private final int ELEVATORS_NUM = 16;

    public ElevatorServiceImpl() {
        this.elevators = new ArrayList<>();
        for (int i = 1; i < ELEVATORS_NUM; i++) {
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
    public void updaeElevatorState(int elevatorId, int currentFloor, int destinationFloor, boolean isMovingUp) {
        Elevator elevator = getElevatorById(elevatorId);
        elevator.setCurrentFloor(currentFloor);
        elevator.setDestinationFloor(destinationFloor);
        elevator.setMovingUp(isMovingUp);
    }

    @Override
    public void performSimulationStep() {
        for (Elevator elevator : elevators) {
            if (elevator.getCurrentFloor() == elevator.getDestinationFloor()) {
                return; // widna jest juz na pietrze docelowym
            }
            int direction = elevator.isMovingUp() ? 1 : -1; // esli winda jedzie do gory -> direciton = 1; jesli na dol:
                                                            // direction = -1
            int nextFloor = elevator.getDestinationFloor() + direction;
            if (nextFloor == elevator.getDestinationFloor()) {
                // winda dotarla do pietra docelowego
                elevator.setCurrentFloor(nextFloor);
            } else {
                // winda jest wlasnie w ruchu
                elevator.setCurrentFloor(nextFloor);
                // zmiana kierunku ruchu SCAN
                if (nextFloor == 16 && scanDirection) {
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
    public Map<Integer, Integer> getElevatorStatus() {
        Map<Integer, Integer> status = new HashMap<>();
        for (Elevator elevator : elevators) {
            status.put(elevator.getCurrentFloor(), elevator.getDestinationFloor());
        }
        return status;
    }
}
