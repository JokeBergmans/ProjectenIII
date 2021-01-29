import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { InputDialogComponent } from '../input-dialog/input-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  constructor(private dialog:  MatDialog) { }

  openInputDialog() {
    return this.dialog.open(InputDialogComponent, {
      width: '550px',
      panelClass: 'confirm-dialog',
      autoFocus: false,
      disableClose: true,
      data : {
        routeName : name
      }
    })
  }
}


