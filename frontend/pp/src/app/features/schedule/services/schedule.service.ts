import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LineDto } from '../models/lineDto';
import { RouteDetailDto } from '../models/routeDetailDto';
import { RouteManifestDto } from '../models/routeManifestDto';
import { StopDepartureDto } from '../models/stopDepartureDto';
import { StopDto } from '../models/stopDto';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {

  constructor(private http: HttpClient) { }

  getStops(): Observable<StopDto[]> {
    return this.http.get<StopDto[]>("/api/schedule/schedule/stops");
  }

  getLines(): Observable<LineDto[]> {
    return this.http.get<LineDto[]>("/api/schedule/schedule/lines");
  }

  getLineRoutes(lineId: string): Observable<RouteManifestDto[]> {
    return this.http.get<RouteManifestDto[]>(`/api/schedule/schedule/lines/${lineId}/routes`);
  }

  getRouteDetails(routeId: string): Observable<RouteDetailDto> {
    return this.http.get<RouteDetailDto>(`/api/schedule/schedule/routes/${routeId}`);
  }

  getStopDepartures(stopId: string): Observable<StopDepartureDto[]> {
    return this.http.get<StopDepartureDto[]>(`/api/schedule/schedule/stops/${stopId}/departures`);
  }
}
