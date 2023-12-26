package com.pgms.apievent.exception;

public class EventSeatAreaNotFoundException extends CustomException{
    public EventSeatAreaNotFoundException() {
        super(EventErrorCode.EVENT_SEAT_AREA_NOT_FOUND);
    }
}
