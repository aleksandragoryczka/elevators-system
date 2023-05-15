package com.elevators.elevators;

import java.util.ArrayList;
import java.util.Currency;
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
    private final int MAX_FLOORS_REQUESTS = 4;

    public ElevatorServiceImpl() {
    }

    public ElevatorServiceImpl(int elevatorsNumber) {
        this.elevators = new ArrayList<>();
        for (int i = 0; i < elevatorsNumber; i++) {
            Elevator elevator = new Elevator(i, 0, new ArrayList<Integer>());
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
    public void callElevator(int[] floors){
        if(floors[0] != floors[1]){
            //wybor najlepszej windy ! by ponizej mozna sie do niej odowlac po elevatorId
            Elevator elevator = chooseBestElevator(floors);
         //   System.out.println("ID WYBRANEJ WINDY: " + elevator.getId());
            //Elevator elevator = this.chooseBestElevator(floors[0]);
            
            if(elevator != null){
                System.out.println("pierw");
                if(elevator.getCurrentFloor() == floors[0]){
                    List<Integer> tempList = elevator.getDestinationFloors();
                    tempList.remove(tempList.indexOf(floors[0]));
                    tempList.add(floors[1]);
                    
                    //currentElevator.setDestinationFloors(currentElevatorDestinationFloors);
                    elevator.setDestinationFloors(tempList);

                  System.out.println("po ustawieniu templist: " + elevator.getDestinationFloors());
                  elevator.setNextFloor(floors[1]);
                }
                else{
                    elevator.setNextFloor(floors[1]);
                }
            }
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


    private Elevator addFloorToDestinationsFloors(int floor, Elevator currentElevator){
        List<Integer> currentElevatorDestinationFloors = currentElevator.getDestinationFloors();
        currentElevatorDestinationFloors.add(floor);
        currentElevator.setDestinationFloors(currentElevatorDestinationFloors);
        return currentElevator;
    }

    private Elevator chooseBestElevator(int [] floors){
        int minUp = Integer.MAX_VALUE;
        int minDown = Integer.MAX_VALUE;
        Elevator bestUpElevator = null;
        Elevator bestDownElevator = null;

        for(Elevator elevator: elevators){
            /*
            if(elevator.getDestinationFloors().size() >= MAX_FLOORS_REQUESTS){
                continue;
            }*/
            //dwa ponizsze 100% pokrywaja sie liniami kursow
            if(elevator.getCurrentFloor() <= floors[0] && elevator.getCurrentFloor() <= floors[1] 
            && (elevator.getDirection() == DirectionsEnum.UP || elevator.getDirection() == DirectionsEnum.NULL)){
                if(minUp > floors[0] - elevator.getCurrentFloor()){
                    minUp = floors[0] - elevator.getCurrentFloor();
                    bestUpElevator = elevator;
                }
            }
            else if(elevator.getCurrentFloor() >= floors[0] && elevator.getCurrentFloor() >= floors[1] 
            && (elevator.getDirection() == DirectionsEnum.DOWN || elevator.getDirection() == DirectionsEnum.NULL)){
                if(minDown > Math.abs(floors[0] - elevator.getCurrentFloor())){
                    minDown = Math.abs(floors[0] - elevator.getCurrentFloor());
                    bestDownElevator = elevator;
                }
            }
        }

        System.out.println("bestUp: " + minUp);
        System.out.println("bestDown: " + minDown);
        if(bestDownElevator != null && bestUpElevator != null){
            if(minDown < minUp){
                return this.addFloorToDestinationsFloors(floors[0], bestDownElevator);
            }else{
                return this.addFloorToDestinationsFloors(floors[0], bestUpElevator);
            }
        }
        else if(bestDownElevator != null){
            return this.addFloorToDestinationsFloors(floors[0], bestDownElevator);
        }else if(bestUpElevator != null){
            return this.addFloorToDestinationsFloors(floors[0], bestUpElevator);
        }
        return null;
    }

    
}