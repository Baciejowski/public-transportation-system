import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LineDetailsDto } from '../models/LineDetailsDto';
import { LineDto } from '../models/lineDto';
import { RouteDetailDto } from '../models/routeDetailDto';
import { RouteManifestDto } from '../models/routeManifestDto';
import { StopDepartureDto } from '../models/stopDepartureDto';
import { StopDetailsDto } from '../models/stopDetailsDto';
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

  getLineDetails(lineId: string): Observable<LineDetailsDto> {
    return this.http.get<LineDetailsDto>(`/api/schedule/schedule/lines/${lineId}`);
  }

  getRouteDetails(routeId: string): Observable<RouteDetailDto> {
    return this.http.get<RouteDetailDto>(`/api/schedule/schedule/routes/${routeId}`);
  }

  getStopDetails(stopId: string): Observable<StopDetailsDto> {
    return this.http.get<StopDetailsDto>(`/api/schedule/schedule/stops/${stopId}`);
  }
}
