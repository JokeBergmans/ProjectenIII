import { TestBed } from '@angular/core/testing';

import { KmlFileService } from './kml-file.service';

describe('KmlFileService', () => {
  let service: KmlFileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KmlFileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
