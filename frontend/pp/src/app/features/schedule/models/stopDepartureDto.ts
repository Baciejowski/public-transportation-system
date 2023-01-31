import { LineDto } from "./lineDto";
import { RouteManifestDto } from "./routeManifestDto";

export interface StopDepartureDto {
    line: LineDto;
    route: RouteManifestDto;
    departure: string;
    deviation: string;
}