import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subject, takeUntil, tap } from 'rxjs';
import { LineDetailsDto } from '../../models/lineDetailsDto';
import { RouteDetailDto } from '../../models/routeDetailDto';
import { RouteManifestDto } from '../../models/routeManifestDto';
import { StopDto } from '../../models/stopDto';
import { ScheduleService } from '../../services/schedule.service';

@Component({
  selector: 'app-line',
  templateUrl: './line.component.html',
  styleUrls: ['./line.component.scss']
})
export class LineComponent implements OnInit, OnDestroy {

  line: LineDetailsDto | null;
  routeDetails: RouteDetailDto;
  destroy$ = new Subject();

  constructor(
    private route: ActivatedRoute,
    private scheduleService: ScheduleService,
    private router: Router
  ) { }
  
  ngOnInit(): void {
    const lineId = this.route.snapshot.paramMap.get('lineId')!;
    this.scheduleService.getLineDetails(lineId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(lineDetails => this.line = lineDetails);
  }
  
  switchRoute(routeId: string) {
    this.scheduleService.getRouteDetails(routeId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(routeDetails => this.routeDetails = routeDetails);
  }

  selectStop(stop: StopDto) {
    this.router.navigate(['stops', stop.id]);
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }
}
