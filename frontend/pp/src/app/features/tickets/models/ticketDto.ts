export interface TicketDto {
    passengerId: number;
    pricePaid: number;
    isReduced: boolean;
    ticketNo: number;
    punchTime: string;
    punched: boolean;
    duration?: string;
    rideId?: number;
}