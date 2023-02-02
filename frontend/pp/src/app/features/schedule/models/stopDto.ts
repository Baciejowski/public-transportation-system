import { Coordinates } from "./coordinates";

export interface StopDto {
    id: number;
    name: string;
    coordinates: Coordinates;
    onRequest: boolean;
}