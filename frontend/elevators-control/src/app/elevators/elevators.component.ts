import { Component, Input, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject} from 'rxjs';
import { Elevator } from './elevator.model';
import { environment } from 'src/environments/environment';
import { DirectionsEnum } from './directions-enum';


const BASE_URL = 'http://localhost:8080/api/elevators';

@Component({
  selector: 'app-elevators',
  templateUrl: './elevators.component.html',
  styleUrls: ['./elevators.component.scss'],
})
export class ElevatorsComponent implements OnInit {
  elevators: Elevator[] = []; //TODO: to be changed to Elevator model
  refreshData$: Subject<void> = new Subject<void>();
  DirectionsEnum = DirectionsEnum;
  floors = 10;
  floorsArray: number[] = Array.from(Array(this.floors).keys());
  @Input() elevatorsListNumber!: number;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.refreshData$.next();
    //console.log(this.elevators);

    setInterval(() => {
      this.getElevatorsStatuses();
    }, 800);
  }

  callElevator(elevatorId: number, floor: number): void {
    this.http
      .post(`${BASE_URL}/${elevatorId}/callElevator?floor=${floor}`, null)
      .subscribe(() => this.refreshData$.next());
    //this.updateElevatorState(elevatorId, floor);
    //console.log(this.getElevatorStatus());
  }

  updateElevatorState(
    elevatorId: number,
    //currentFloor: number,
    destinationFloor: number
    //isMovingUp: boolean
  ): void {
    this.http
      .put(`${BASE_URL}/${elevatorId}`, {
        // currentFloor: currentFloor,
        destinationFloor: destinationFloor,
        // isMovingUp: isMovingUp,
      })
      .subscribe();
  }

  performSimulationStep(): void {
    this.http
      .post(`${BASE_URL}/simulate`, null)
      .subscribe(() => this.refreshData$.next());
  }

  getElevatorsStatuses(): void {
    this.http.get<Elevator[]>(BASE_URL).subscribe((data) => {
      this.elevators = data;
      console.log(data);
    });
  }
}
