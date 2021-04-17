package com.restapi.demoinfleanrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
//            errors.rejectValue("basePrice", "wrongValue", "Base price is wrong.");
//            errors.rejectValue("maxPrice", "wrongValue", "Max price is wrong.");
            errors.reject("wrongPrices", "Prices is wrong");

        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();

        if (endEventDateTime.isBefore(beginEventDateTime)
                || endEventDateTime.isBefore(closeEnrollmentDateTime)
                || endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "End Event DateTime is Wrong");
        }

//        if (beginEventDateTime.isAfter(endEventDateTime)
//                || beginEventDateTime.isAfter(closeEnrollmentDateTime)) {
//            errors.rejectValue("BeginEventDateTime", "wrongValue", "Begin Event DateTime is Wrong");
//        }

    }
}
