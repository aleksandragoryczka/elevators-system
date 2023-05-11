import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-elevators',
  templateUrl: './elevators.component.html',
  styleUrls: ['./elevators.component.scss'],
})
export class ElevatorsComponent implements OnInit {
  elevators: any[] = []; //TODO: to be changed to Elevator model

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http
      .get<any[]>('http://localhost:8080/api/elevators')
      .subscribe((data) => {
        this.elevators = data;
      });
  }

  callElevator(elevatorId: number, floor: number): void {
    this.http
      .put(
        'http://localhost:8080/api/elevators' +
          elevatorId +
          '/callElevator/' +
          floor,
        null
      )
      .subscribe();
  }

  updateElevatorState(
    elevatorId: number,
    currentFloor: number,
    destinationFloor: number,
    isMovingUp: boolean
  ): void {
    this.http
      .put('http://localhost:8080/api/elevators/' + elevatorId + '/state', {
        currentFloor: currentFloor,
        destinationFloor: destinationFloor,
        isMovingUp: isMovingUp,
      })
      .subscribe();
  }

  performSimulationState(): void {
    this.http
      .put('https://localhost:8080/api/elevators/simulate', null)
      .subscribe();
  }

  getElevatorStatus(): void {
    this.http
      .get<any>('http://localhost:8080/api/elevators/status')
      .subscribe((data) => {
        console.log(data);
      });
  }
}
