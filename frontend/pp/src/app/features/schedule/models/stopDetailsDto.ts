import { LineDto } from "./lineDto";
import { StopDepartureDto } from "./stopDepartureDto";
import { StopDto } from "./stopDto";

export interface StopDetailsDto extends StopDto {
    lines: LineDto[];
    departures: StopDepartureDto[];
}