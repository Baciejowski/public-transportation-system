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
  deviation: string;
  line: string;
  filteredDeviations: DeviationDto[]

  constructor(
    private scheduleService: ScheduleService,
    private router: Router
  ) { }

  
  ngOnInit(): void {
    this.scheduleService.getDeviations()
      .pipe(takeUntil(this.destroy$))
      .subscribe(deviations => {
        this.deviations = deviations;
        this.filteredDeviations = deviations;
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

  filter() {
    this.filteredDeviations = this.deviations
      .filter(x => {
        if (this.line) {
          return x.line.name.toLowerCase().includes(this.line.toLowerCase())
        }
        return x;
      })
      .filter(x => {
        if (this.deviation && parseInt(this.deviation)) {
          return moment.duration(x.deviation).asMinutes() >= parseInt(this.deviation) 
        }
        return x;
      })
  }

}
