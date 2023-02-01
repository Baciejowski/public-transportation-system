import { OfferedTicketDto } from "./offeredTicketDto";

export interface TicketOfferDto {
    offerStart: string;
    offerEnd: string;
    id: number;
    tickets: OfferedTicketDto[];
    currentlyValid: boolean;
}