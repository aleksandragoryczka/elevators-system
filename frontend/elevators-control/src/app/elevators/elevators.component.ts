import { Component, Input, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';
import { Elevator } from './elevator.model';
import { environment } from 'src/environments/environment';
import { DirectionsEnum } from './directions-enum';
import { ActivatedRoute } from '@angular/router';

const BASE_URL = environment.apiUrl + '/elevators';

@Component({
  selector: 'app-elevators',
  templateUrl: './elevators.component.html',
  styleUrls: ['./elevators.component.scss'],
})
export class ElevatorsComponent implements OnInit {
  elevators: Elevator[] = []; //TODO: to be changed to Elevator model
  refreshData$: Subject<void> = new Subject<void>();
  DirectionsEnum = DirectionsEnum;
  floorsArray: number[] = []; //= Array.from(Array(this.inputList[1]).keys());
  elevatorsNumber: number = 1;

  selectedFloors: number[] = [0, 0];
 

  constructor(private http: HttpClient, private route: ActivatedRoute) {
    this.route.params.subscribe(params =>{
      if(params['inputsList']){
        this.elevatorsNumber = params['inputsList'][0];
        this.floorsArray = Array.from(Array(params['inputsList'][1]).keys());
        console.log(this.floorsArray);
      }else if(window.history.state.inputsList){
        this.elevatorsNumber = window.history.state.inputsList[0];
        this.floorsArray = Array.from(Array(window.history.state.inputsList[1]).keys());
      }
    });
  }

  ngOnInit(): void {
    this.http.post(`${BASE_URL}?elevatorsNumber=${this.elevatorsNumber}`, null).subscribe();

    this.refreshData$.next();

    setInterval(() => {
      this.getElevatorsStatuses();
    }, 800);
  }

  callElevator(floor: number): void {
    this.http
      .post(`${BASE_URL}/callElevator?floor=${floor}`, null)
      .subscribe(() => this.refreshData$.next());
    //this.updateElevatorState(elevatorId, floor);
    //console.log(this.getElevatorStatus());
  }

  submitChosenFloors(): void {
    console.log("cos");
  }


/*
  callElevator(elevatorId: number, floor: number): void {
    this.http
      .post(`${BASE_URL}/${elevatorId}/callElevator?floor=${floor}`, null)
      .subscribe(() => this.refreshData$.next());
    //this.updateElevatorState(elevatorId, floor);
    //console.log(this.getElevatorStatus());
  }*/

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
    });
  }

  options = [
    {label: '1', value: 1}
  ]


}
