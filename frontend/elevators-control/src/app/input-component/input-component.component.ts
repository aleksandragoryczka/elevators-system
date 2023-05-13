import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-input-component',
  templateUrl: './input-component.component.html',
  styleUrls: ['./input-component.component.scss']
})
export class InputComponentComponent {
  elevatorsNumber = 1;
  floorsNumber = 3;
  @Output() valuesEmitter= new EventEmitter<number>();

  submitInput(){
    this.valuesEmitter.emit(this.elevatorsNumber);
    console.log(this.elevatorsNumber);
  }

}
