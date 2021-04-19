package com.restapi.demoinfleanrestapi.events;

import com.restapi.demoinfleanrestapi.accounts.Account;
import com.restapi.demoinfleanrestapi.accounts.CurrentUser;
import com.restapi.demoinfleanrestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    // @Autowired
    // 생성자가 하나만 있고, 파라미터가 이미 빈으로 등록되어 있다면 생략 가능 since 4.3
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors
            , @CurrentUser Account currentUser) {
        if (errors.hasErrors())
            return badRequest(errors);

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors())
            return badRequest(errors);

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        event.setManager(currentUser);
        Event newEvent = this.eventRepository.save(event);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();

        EventResource eventResource = new EventResource(newEvent);
//        eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler,
//                                      @AuthenticationPrincipal AccountAdapter currentUser) { // 아래와 동일
                                      @CurrentUser Account account) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User principal = (User) authentication.getPrincipal(); // accountService에서 리턴한 객체가 나옴
        Page<Event> page = this.eventRepository.findAll(pageable);
        var pagedResources = assembler.toModel(page, EventResource::new);
        pagedResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));

        if (account != null)
            pagedResources.add(linkTo(EventController.class).withRel("create-event"));

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id,
                                   @CurrentUser Account currentUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty())
            return ResponseEntity.notFound().build();

        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));
        if (event.getManager().equals(currentUser))
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));

        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id,
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors,
                                      @CurrentUser Account currentUser) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty())
            return ResponseEntity.notFound().build();

        if (errors.hasErrors())
            return badRequest(errors);

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors())
            return badRequest(errors);

        Event existingEvent = optionalEvent.get();
        if (!existingEvent.getManager().equals(currentUser))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        this.modelMapper.map(eventDto, existingEvent); // 1에서 2로 덮어씀
        Event savedEvent = this.eventRepository.save(existingEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResource);
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

}
