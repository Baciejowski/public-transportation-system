import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { RouteDetailDto } from '../../models/routeDetailDto';
import { RouteManifestDto } from '../../models/routeManifestDto';
import { ScheduleService } from '../../services/schedule.service';

@Component({
  selector: 'app-line',
  templateUrl: './line.component.html',
  styleUrls: ['./line.component.scss']
})
export class LineComponent implements OnInit {

  lineRoutes$: Observable<RouteManifestDto[]>;
  route$: Observable<RouteDetailDto>;

  constructor(private route: ActivatedRoute, private scheduleService: ScheduleService) { }

  ngOnInit(): void {
    const lineId = this.route.snapshot.paramMap.get('lineId')!;
    this.lineRoutes$ = this.scheduleService.getLineRoutes(lineId);
  }

  switchRoute(routeId: string) {
    console.log(routeId)
    this.route$ = this.scheduleService.getRouteDetails(routeId);
  }

}
