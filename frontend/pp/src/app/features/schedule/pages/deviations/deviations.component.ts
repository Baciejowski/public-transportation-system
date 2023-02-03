import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DeviationDto } from '../../models/deviationDto';
import { LineDto } from '../../models/lineDto';
import { ScheduleService } from '../../services/schedule.service';
import { Subject, takeUntil } from 'rxjs';
import * as moment from 'moment';

@Component({
  selector: 'app-deviations',
  templateUrl: './deviations.component.html',
  styleUrls: ['./deviations.component.scss']
})
export class DeviationsComponent implements OnInit {

  deviations: DeviationDto[];
  destroy$ = new Subject();
  includeDeviation: boolean = false;

  constructor(
    private scheduleService: ScheduleService,
    private router: Router
  ) { }

  
  ngOnInit(): void {
    this.scheduleService.getDeviations()
      .pipe(takeUntil(this.destroy$))
      .subscribe(deviations => {
        this.deviations = deviations;
      });
  }
  
  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }

  calculateDeviation(deviation: string): string {
    const minutes = moment.duration(deviation).asMinutes();
    return minutes.toString();
  }

  selectLine(line: LineDto) {
    this.router.navigate(['lines', line.id]);
  }

}
