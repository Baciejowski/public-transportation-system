import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveTicketComponent } from './active-ticket.component';

describe('ActiveTicketComponent', () => {
  let component: ActiveTicketComponent;
  let fixture: ComponentFixture<ActiveTicketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActiveTicketComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActiveTicketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
