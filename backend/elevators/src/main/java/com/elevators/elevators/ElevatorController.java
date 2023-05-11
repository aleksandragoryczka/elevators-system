package com.elevators.elevators;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/elevators")
public class ElevatorController {
    private ElevatorService elevatorService = new ElevatorServiceImpl();

    @GetMapping("")
    public List<Elevator> getAllElevators() {
        return elevatorService.getAllElevators();
    }

    @GetMapping("/{id}")
    public Elevator getElevatorById(@PathVariable int id) {
        return elevatorService.getElevatorById(id);
    }

    @PostMapping("/{id}/callElevator")
    public void callElevator(@PathVariable int id, @RequestParam int floor) {
        elevatorService.callElevator(id, floor);
    }

    @PutMapping("/{id}")
    public void updateElevatorState(@PathVariable int id, @RequestBody Elevator elevator) {
        elevatorService.updaeElevatorState(id, elevator.getCurrentFloor(), elevator.getDestinationFloor(),
                elevator.isMovingUp());
    }

    @PostMapping("/simulate")
    public void perormSimulationStep() {
        elevatorService.performSimulationStep();
    }

    @GetMapping("/status")
    public Map<Integer, Integer> getElevatorStatus() {
        return elevatorService.getElevatorStatus();
    }
}
