package com.elevators.elevators;

import java.util.List;

public interface ElevatorService {

    List<Elevator> getAllElevators();

    Elevator getElevatorById(int id);

    void callElevator(int floor);

    public void updateElevatorState(int elevatorId, int currentFloor, List<Integer> destinationFloor, DirectionsEnum direction);

    void performSimulationStep();

    List<ElevatorStatus> getElevatorStatus();
}
