import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-start-inputs',
  templateUrl: './start-inputs.component.html',
  styleUrls: ['./start-inputs.component.scss']
})
export class StartInputsComponent {
  inputsList: number[] = [1, 3];

  constructor(private router: Router){}

  submitInput() :void  {
    console.log(this.inputsList);
    this.router.navigate(['/elevators'], {state: {inputsList: this.inputsList}});
  }

}
