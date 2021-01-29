import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EMPTY, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ParticipantDataService } from '../participant-data.service';
import { Participant } from '../participant.model';

@Component({
  selector: 'app-participant-list',
  templateUrl: './participant-list.component.html',
})
export class ParticipantListComponent implements OnInit {

  showSucces : boolean = false;
  showError : boolean = false; 
  participants$ : Observable<Participant[]>;
  errorMessage : string = '';
  succesMessage :string;

  constructor(private _participantService : ParticipantDataService,
    private _router: Router) { }

  ngOnInit(): void {
    this.getParticipants()
  }

  getParticipants() {
    this.participants$ = this._participantService.getAllParticipants().pipe(
      catchError((err) => {
        this.errorMessage = err;
        return EMPTY;
      })     
    )
    this.participants$.subscribe();
  }

}
