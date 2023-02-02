import { LineDto } from "./lineDto";
import { RouteManifestDto } from "./routeManifestDto";

export interface LineDetailsDto extends LineDto {
    routes: RouteManifestDto[];
}