import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { NgbTime } from '@ng-bootstrap/ng-bootstrap/timepicker/ngb-time';
import { DialogRouteData } from 'src/app/input-dialog/input-dialog.component';
import { DialogService } from 'src/app/services/dialog.service';
import { environment } from 'src/environments/environment';
import { Route } from '../route.model';
import { KmlFileService } from './kml-file.service';

export interface RouteData {
  id : number,
  url: string,
  name: string
}

@Component({
  selector: 'app-add-file',
  templateUrl: './add-file.component.html',
  styleUrls: ['./add-file.component.css']
})
export class AddFileComponent implements OnInit {

  @Output() public routeData = new EventEmitter<RouteData>(); 
  
  private _fileToUpload: File = null;
  public buttonDisabled = true;

  constructor(
    private _fileService : KmlFileService, 
    private _dialogService: DialogService
  ){}

  ngOnInit(): void {
    document
      .querySelector('.custom-file-input')
      .addEventListener('change', function (e) {
        var file = (<HTMLInputElement>document.getElementById('myInput')).files[0];
        if (typeof file !== 'undefined') {
          document.getElementById('fileLabel').innerText = file.name;
        }
      });
  }

  handleFileInput(files: FileList) {
    this._fileToUpload = files.item(0);
    this.buttonDisabled = false;
  }

  uploadFile(data: DialogRouteData) {   
    
    //prevents cancelled uploads 
    if(data) {

      //google does not allow spaces in their kml requests
      data.routeName = data.routeName.replace(/ /g, "_"); 
      let startDatum = this.createDate(data.start, data.startTime);
      
      const reader = new FileReader();
      reader.readAsBinaryString(this._fileToUpload);
      let url = `${environment.staticFilesUrl}/${data.routeName}.kml`
      reader.onload = () => {
          this._fileService.addKMLfile(data.routeName, data.distance, startDatum, btoa(reader.result as string), url, data.info).subscribe(res => {
           if(res) {
             this.routeData.emit({ id: res['id'], url: `${environment.staticFilesUrl}/${data.routeName}.kml`, name: data.routeName as string})
           } else {
             console.error("Something went wrong when uploading the file.")
           }
         })
      }; 
      this.buttonDisabled = true;
    }
  }

  createDate(date : NgbDate, time: NgbTime) : Date {
      return new Date(date.year, date.month - 1, date.day, time.hour, time.minute, time.second)
  }

  //dialog returns name to upload 
  openDialog(): void {
    this._dialogService.openInputDialog()
    .afterClosed()
    .subscribe(name => {
        this.uploadFile(name)
    });
  }
}
