package com.elevators.elevators;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

enum DirectionsEnum {
    UP,
    DOWN,
    NULL
}

@Service
public class ElevatorServiceImpl implements ElevatorService {
    private List<Elevator> elevators;
    private final int ELEVATORS_NUM = 3; //TODO: to be changed with value from frontend

    public ElevatorServiceImpl() {
        this.elevators = new ArrayList<>();
        for (int i = 1; i < ELEVATORS_NUM + 1; i++) {
            Elevator elevator = new Elevator(i, 0, new ArrayList<Integer>(0));
            this.elevators.add(elevator);
            new Thread(elevator).start();
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
        this.performSimulationStep();
    }

    @Override
    public void updateElevatorState(int elevatorId, int currentFloor, List<Integer> destinationFloor, DirectionsEnum direction) {
        Elevator elevator = getElevatorById(elevatorId);
        elevator.setCurrentFloor(currentFloor);
        elevator.setDestinationFloors(new ArrayList<Integer>(destinationFloor));
        elevator.setDirection(direction);
    }

    @Override
    public void performSimulationStep() {
        for (Elevator elevator : elevators) {
            if (!elevator.isRunning()) {
                elevator.run();
            }
        }
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
