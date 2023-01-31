import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subject, takeUntil, tap } from 'rxjs';
import { RouteDetailDto } from '../../models/routeDetailDto';
import { RouteManifestDto } from '../../models/routeManifestDto';
import { ScheduleService } from '../../services/schedule.service';

@Component({
  selector: 'app-line',
  templateUrl: './line.component.html',
  styleUrls: ['./line.component.scss']
})
export class LineComponent implements OnInit, OnDestroy {

  lineRoutes$: Observable<RouteManifestDto[]>;
  routeDetails: RouteDetailDto;
  destroy$ = new Subject();

  constructor(private route: ActivatedRoute, private scheduleService: ScheduleService) { }
  
  ngOnInit(): void {
    const lineId = this.route.snapshot.paramMap.get('lineId')!;
    this.lineRoutes$ = this.scheduleService.getLineRoutes(lineId);
  }
  
  switchRoute(routeId: string) {
    console.log(routeId)
    this.scheduleService.getRouteDetails(routeId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(routeDetails => this.routeDetails = routeDetails);
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }
}
