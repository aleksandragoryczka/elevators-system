package com.elevators.elevators;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Elevator {
    private int id;
    private int currentFloor;
    private List<Integer> destinationFloors;
    private boolean isMovingUp;

}
