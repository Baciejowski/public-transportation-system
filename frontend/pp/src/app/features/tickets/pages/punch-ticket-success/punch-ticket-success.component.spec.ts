import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PunchTicketSuccessComponent } from './punch-ticket-success.component';

describe('PunchTicketSuccessComponent', () => {
  let component: PunchTicketSuccessComponent;
  let fixture: ComponentFixture<PunchTicketSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PunchTicketSuccessComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PunchTicketSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
