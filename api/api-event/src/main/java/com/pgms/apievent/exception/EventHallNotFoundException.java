package com.pgms.apievent.exception;

import static com.pgms.apievent.exception.EventErrorCode.EVENT_HALL_NOT_FOUND;

public class EventHallNotFoundException extends CustomException{
    public EventHallNotFoundException() {
        super(EVENT_HALL_NOT_FOUND);
    }
}
