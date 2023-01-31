import { OfferedTicket } from "./offeredTicketDto";

export interface TicketOffer {
    offerStart: string;
    offerEnd: string;
    id: number;
    tickets: OfferedTicket[];
    currentlyValid: boolean;
}