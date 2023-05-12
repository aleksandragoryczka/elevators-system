import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { Elevator } from './elevator.model';

const BASE_URL = 'http://localhost:8080/api/elevators';

@Component({
  selector: 'app-elevators',
  templateUrl: './elevators.component.html',
  styleUrls: ['./elevators.component.scss'],
})
export class ElevatorsComponent implements OnInit {
  elevators: Elevator[] = []; //TODO: to be changed to Elevator model
  refreshData$: Subject<void> = new Subject<void>();

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.refreshData$.subscribe(() =>
      this.http.get<Elevator[]>(BASE_URL).subscribe((data) => {
        this.elevators = data;
      })
    );
    this.refreshData$.next();

    this.performSimulationStep();
    /*
    setInterval(() => {
      this.performSimulationStep();
    }, 3000);*/
  }

  callElevator(elevatorId: number, floor: number): void {
    this.http
      .post(`${BASE_URL}/${elevatorId}/callElevator?floor=${floor}`, null)
      .subscribe(() => this.refreshData$.next());
  }

  updateElevatorState(
    elevatorId: number,
    currentFloor: number,
    destinationFloor: number,
    isMovingUp: boolean
  ): void {
    this.http
      .put(`${BASE_URL}/${elevatorId}`, {
        currentFloor: currentFloor,
        destinationFloor: destinationFloor,
        isMovingUp: isMovingUp,
      })
      .subscribe();
  }

  performSimulationStep(): void {
    this.http
      .post(`${BASE_URL}/simulate`, null)
      .subscribe(() => this.refreshData$.next());
  }

  getElevatorStatus(): void {
    this.http.get<Elevator>(`${BASE_URL}/status`).subscribe((data) => {
      console.log(data);
    });
  }
}
