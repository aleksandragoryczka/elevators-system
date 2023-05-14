package com.elevators.elevators;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

enum DirectionsEnum {
    UP,
    DOWN,
    WAITING,
    NULL
}

@Service
public class ElevatorServiceImpl implements ElevatorService {
    private List<Elevator> elevators;
    private final int MAX_FLOORS_REQUESTS = 3;

    public ElevatorServiceImpl() {
    }

    public ElevatorServiceImpl(int elevatorsNumber) {
        this.elevators = new ArrayList<>();
        for (int i = 1; i < elevatorsNumber + 1; i++) {
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
/*
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
    }*/

    //TODO: add enum : WAITING for choose next floor
    private Elevator chooseBestElevator(int calledFloor){
        int minUp = Integer.MAX_VALUE;
        int minDown = Integer.MAX_VALUE;
        Elevator bestUpElevator = null;
        Elevator bestDownElevator = null;

        for(Elevator elevator: elevators){
            if(elevator.getDestinationFloors().size() >= MAX_FLOORS_REQUESTS){
                continue;
            }
            if(elevator.getCurrentFloor() == calledFloor){
                System.out.println("winda juz jest na pietrze " + calledFloor);
                return null;
            }
            if(elevator.getDestinationFloors().contains(calledFloor)){
                System.out.println("winda juz ejedzie na pietro: " + calledFloor);
                return null;
            }
            if((elevator.getDirection() == DirectionsEnum.UP 
            || elevator.getDirection() == DirectionsEnum.NULL) 
            && elevator.getCurrentFloor() < calledFloor){
                if(minUp > calledFloor - elevator.getCurrentFloor()){
                    minUp = calledFloor - elevator.getCurrentFloor();
                    bestUpElevator = elevator;
                }
            }else if((elevator.getDirection() == DirectionsEnum.DOWN 
            || elevator.getDirection() == DirectionsEnum.NULL) 
            && elevator.getCurrentFloor() > calledFloor){
                if(minDown > calledFloor - elevator.getCurrentFloor()){
                    minDown = calledFloor - elevator.getCurrentFloor();
                    bestDownElevator = elevator;
                }
            }
        }

        System.out.println("WYBOR WINDY");
        if(bestDownElevator != null && bestUpElevator != null){
            if(minDown < minUp){
                return this.addFloorToDestinationsFloors(calledFloor, bestDownElevator);
            }else{
                return this.addFloorToDestinationsFloors(calledFloor, bestUpElevator);
            }
        }else if(bestDownElevator != null){
            return this.addFloorToDestinationsFloors(calledFloor, bestDownElevator);
        }else if(bestUpElevator != null){
            return this.addFloorToDestinationsFloors(calledFloor, bestUpElevator);
        }
        return null;
    }

    private Elevator addFloorToDestinationsFloors(int floor, Elevator currentElevator){
        List<Integer> currentElevatorDestinationFloors = currentElevator.getDestinationFloors();
        currentElevatorDestinationFloors.add(floor);
        currentElevator.setDestinationFloors(currentElevatorDestinationFloors);
        return currentElevator;
    }


/*
        for(Elevator elevator : elevators){
            /*if(elevator.getDirection() == DirectionsEnum.NULL){

            }
            int score = Math.abs(elevator.getCurrentFloor() - calledFloor);
            //int score = Math.abs(elevator.getCurrentFloor() - calledFloor);
            if(elevator.getDirection() == DirectionsEnum.NULL){
                score = Math.abs(elevator.getCurrentFloor() - calledFloor);
            } else{
                score = Math.abs(elevator.getDestinationFloors().get(0));
            }
            if(score < bestScore){
                bestScore = score;
                bestElevator = elevator;
            }
        }

        return bestElevator;*/


    @Override
    public void callElevator(int floor){
        //wybor najlepszej windy ! by ponizej mozna sie do niej odowlac po elevatorId
        Elevator elevator = chooseBestElevator(floor);
        
        //Elevator elevator = getElevatorById(elevatorId);
        if(elevator != null ){
            System.out.println(elevator);
            System.out.println("ID wybranej windy: " + elevator.getId());
            if (elevator.getCurrentFloor() == floor) {
                return; // winda jest juz na tym pietrze
            }/*
            if (elevator.getDestinationFloors().contains(floor)) {
                return; // winda juz jedzie na to pietro
            }*/
            //List<Integer> calledElevator = new ArrayList<>();
            //calledElevator = elevator.getDestinationFloors();
            //calledElevator.add(floor);
            //elevator.setDestinationFloors(calledElevator);
            this.performSimulationStep();
        }

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
