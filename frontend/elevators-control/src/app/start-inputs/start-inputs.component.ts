import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-start-inputs',
  templateUrl: './start-inputs.component.html',
  styleUrls: ['./start-inputs.component.scss']
})
export class StartInputsComponent {
  inputsList: number[] = [1, 3];

  
  //@Output() valuesEmitter = new EventEmitter<number[]>();

  constructor(private router: Router){}

  submitInput() :void  {
    console.log(this.inputsList);
    this.router.navigate(['/elevators'], {state: {inputsList: this.inputsList}});


    //this.valuesEmitter.emit([this.elevatorsNumber, this.floorsNumber]);
    //console.log(this.elevatorsNumber);
  }

}
