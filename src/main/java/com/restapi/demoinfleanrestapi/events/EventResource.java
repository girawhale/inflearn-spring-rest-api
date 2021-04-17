package com.restapi.demoinfleanrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource
//        extends RepresentationModel {
        extends EntityModel<Event> { // 이렇게하면 자동으로 unwrap되어 들어가기 때문에 코드가 간결해짐
//    // BeanSerializer를 사용하기 때문에 field이름을 사용하기 때문에 event에 하위 value들로 들어감....
//    @JsonUnwrapped
//    private Event event;
//
//    public EventResource(Event event) {
//        this.event = event;
//    }
//
//    public Event getEvent() {
//        return event;
//    }

    public EventResource(Event event, Link... links) {
        super(event, links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }

}
