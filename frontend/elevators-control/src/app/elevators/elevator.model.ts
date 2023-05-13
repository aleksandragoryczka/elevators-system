import { DirectionsEnum } from "./directions-enum";

export class Elevator {
  id?: number;
  currentFloor?: number;
  destinationFloors?: number[];
  direction?: DirectionsEnum;
}
