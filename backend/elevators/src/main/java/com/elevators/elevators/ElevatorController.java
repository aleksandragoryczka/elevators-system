package com.elevators.elevators;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/elevators")
public class ElevatorController {
    private ElevatorService elevatorService;

    public ElevatorController(ElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }

    @GetMapping("")
    public List<Elevator> getAllElevators() {
        return elevatorService.getAllElevators();
    }

    @PostMapping("")
    public void initializeElevatorsNumber(@RequestParam int elevatorsNumber){  
        this.elevatorService = new ElevatorServiceImpl(elevatorsNumber);
    }

    @GetMapping("/{id}")
    public Elevator getElevatorById(@PathVariable int id) {
        return elevatorService.getElevatorById(id);
    }

    @PostMapping("/callElevator")
    public void callElevator(@RequestParam int[] floors) {
        elevatorService.callElevator(floors);
    }

    @PutMapping("/{id}")
    public void updateElevatorState(@PathVariable int id, @RequestBody Elevator elevator) {
        elevatorService.updateElevatorState(id, elevator.getCurrentFloor(), elevator.getDestinationFloors(), elevator.getDirection());
    }

    @PostMapping("/simulate")
    public void performSimulationStep() {
        elevatorService.performSimulationStep();
    }

    @GetMapping("/status")
    public List<ElevatorStatus> getElevatorStatus() {
        return elevatorService.getElevatorStatus();
    }
}
