import { StopDto } from "./stopDto";

export interface RouteDetailDto {
    id: number;
    name: string;
    stops: StopDto[];
}