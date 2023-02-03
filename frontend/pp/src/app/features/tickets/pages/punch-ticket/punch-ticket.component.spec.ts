import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PunchTicketComponent } from './punch-ticket.component';

describe('PunchTicketComponent', () => {
  let component: PunchTicketComponent;
  let fixture: ComponentFixture<PunchTicketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PunchTicketComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PunchTicketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
