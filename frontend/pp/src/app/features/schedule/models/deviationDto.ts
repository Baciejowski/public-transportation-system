import { LineDto } from "./lineDto";
import { StopDto } from "./stopDto";

export interface DeviationDto {
    deviation: string;
    line: LineDto;
    lastStop: StopDto;
    departed: string;
    nextStop: StopDto;
}