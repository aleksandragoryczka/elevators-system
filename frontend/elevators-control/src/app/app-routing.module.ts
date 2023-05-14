import { NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ElevatorsComponent } from './elevators/elevators.component';
import { StartInputsComponent } from './start-inputs/start-inputs.component';


const routes: Routes = [
  {path: '', component: StartInputsComponent},
  {path: 'elevators', component: ElevatorsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
