package com.elevators.elevators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Elevator {
    private int id;
    private int currentFloor;
    private int destinationFloor;
    private boolean isMovingUp;

}
