package com.elevators.elevators;

import java.util.List;
import java.util.Map;

public interface ElevatorService {
    List<Elevator> getAllElevators();

    Elevator getElevatorById(int id);

    void callElevator(int elevatorId, int floor);

    void updaeElevatorState(int elevatorId, int currentFloor, int destinationFloor, boolean isMovingUp);

    void performSimulationStep();

    Map<Integer, Integer> getElevatorStatus();
}
