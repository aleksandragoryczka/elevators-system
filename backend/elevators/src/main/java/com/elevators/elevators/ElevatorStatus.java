package com.elevators.elevators;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ElevatorStatus {
    private int elevatorId;
    private int currentFloor;
    private List<Integer> destinationFloors;
}
