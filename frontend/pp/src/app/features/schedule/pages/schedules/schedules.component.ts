import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { LineDto } from '../../models/lineDto';
import { StopDto } from '../../models/stopDto';
import { ScheduleService } from '../../services/schedule.service';

@Component({
  selector: 'app-schedules',
  templateUrl: './schedules.component.html',
  styleUrls: ['./schedules.component.scss']
})
export class SchedulesComponent implements OnInit {

  lines$: Observable<LineDto[]>;
  stops$: Observable<StopDto[]>;

  constructor(private scheduleService: ScheduleService, private router: Router) { }

  ngOnInit(): void {
    this.lines$ = this.scheduleService.getLines();
    this.stops$ = this.scheduleService.getStops();
  }

  selectLine(line: LineDto) {
    this.router.navigate(['lines', line.id]);
  }
}
