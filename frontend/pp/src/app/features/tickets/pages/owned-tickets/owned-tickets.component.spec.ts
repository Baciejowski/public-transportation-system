import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnedTicketsComponent } from './owned-tickets.component';

describe('OwnedTicketsComponent', () => {
  let component: OwnedTicketsComponent;
  let fixture: ComponentFixture<OwnedTicketsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OwnedTicketsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OwnedTicketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
