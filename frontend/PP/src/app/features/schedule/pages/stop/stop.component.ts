import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import * as moment from 'moment';
import { Observable, Subject, takeUntil, tap } from 'rxjs';
import { LineDto } from '../../models/lineDto';
import { StopDepartureDto } from '../../models/stopDepartureDto';
import { StopDetailsDto } from '../../models/stopDetailsDto';
import { ScheduleService } from '../../services/schedule.service';

@Component({
  selector: 'app-stop',
  templateUrl: './stop.component.html',
  styleUrls: ['./stop.component.scss']
})
export class StopComponent implements OnInit, OnDestroy {

  stop: StopDetailsDto | null;
  destroy$ = new Subject();
  includeDeviation: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private scheduleService: ScheduleService,
    private router: Router
  ) { }

  
  ngOnInit(): void {
    const stopId = this.route.snapshot.paramMap.get('stopId')!;
    this.scheduleService.getStopDetails(stopId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(stop => {
        this.stop = stop;
        this.stop.departures.forEach(stop => {
          stop.deviation = this.calculateDeviation(stop.deviation);
        })
      });
  }
  
  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }

  calculateTimeLeft(stopDeparture: StopDepartureDto, includeDeviation: boolean): string {
    const currentTime = moment().utc();
    let departureTime = moment(stopDeparture.departure + 'Z');
    if (includeDeviation) {
      departureTime = departureTime.add(moment.duration(stopDeparture.deviation));
    }
    const difference = departureTime.diff(currentTime, 'minutes');
    if (difference >= 60) {
      const hours = Math.floor(difference / 60);
      const reminder = difference % 60;
      const minutes = reminder < 10 ? `0${reminder % 60}` : reminder;
      return minutes ? `${hours}:${minutes} h` : `${hours} h`;
    }
    return `${difference} min`
  }

  calculateDeviation(deviation: string | null): string | null {
    const minutes = moment.duration(deviation).asMinutes();
    return minutes ? minutes.toString() : null;
  }

  calculateDepartureTime(stopDeparture: StopDepartureDto, includeDeviation: boolean): string {
    const departureTime = moment(stopDeparture.departure + 'Z');
    if (!includeDeviation) return departureTime.format();
    else return departureTime.add(moment.duration(stopDeparture.deviation)).format();
  }
  
  onToggleChange(value: string) {
    if (value === 'yes') {
      this.includeDeviation = true;
    } else {
      this.includeDeviation = false;
    }
  }

  selectLine(line: LineDto) {
    this.router.navigate(['lines', line.id]);
  }
}
