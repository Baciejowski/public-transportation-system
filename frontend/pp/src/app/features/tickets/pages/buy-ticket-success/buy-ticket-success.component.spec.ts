import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyTicketSuccessComponent } from './buy-ticket-success.component';

describe('BuyTicketSuccessComponent', () => {
  let component: BuyTicketSuccessComponent;
  let fixture: ComponentFixture<BuyTicketSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BuyTicketSuccessComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BuyTicketSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
