import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FollowMapComponent } from './follow-map.component';

describe('FollowMapComponent', () => {
  let component: FollowMapComponent;
  let fixture: ComponentFixture<FollowMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FollowMapComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FollowMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
