import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { NgbTime } from '@ng-bootstrap/ng-bootstrap/timepicker/ngb-time';

export interface DialogRouteData {
  routeName: String;
  start: NgbDate;
  startTime: NgbTime;
  info: string;
  distance: number;
}

@Component({
  selector: 'app-input-dialog',
  templateUrl: './input-dialog.component.html',
  styleUrls: ['./input-dialog.component.css'],
})

export class InputDialogComponent implements OnInit {
  public routeName: String;
  public start: NgbDate;
  public startTime: NgbTime;
  public info: string;

  public today : NgbDate;
  public minuteStep = 15;

  constructor(
    public dialogRef: MatDialogRef<InputDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogRouteData
  ) {
    console.log(data);
  }

  ngOnInit() {
    let t = new Date(); 
    this.today = new NgbDate(t.getFullYear(), t.getMonth() + 1, t.getDate())
  }

  onNoClick(): void { 
    this.dialogRef.close();
  }

  closeDialog() {
    this.dialogRef.close(false);
  }
}
