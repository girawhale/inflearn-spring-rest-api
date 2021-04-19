package com.restapi.demoinfleanrestapi.events;


import junitparams.Parameters;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

    @Test
//    @Parameters({
//            "0, 0, true",
//            "100, 0, false",
//            "0, 100, false"
//    })
//    @Parameters(method = "parametersForTestFree")
    @Parameters // 이름 컨벤션을 자동으로 찾아줌
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
//        // Given
//        Event event = Event.builder()
//                .basePrice(0)
//                .maxPrice(0)
//                .build();
//
//        // When
//        event.update();
//
//        // Then
//        assertThat(event.isFree()).isTrue();
//
//        // Given
//        event = Event.builder()
//                .basePrice(100)
//                .maxPrice(0)
//                .build();
//
//        // When
//        event.update();
//
//        // Then
//        assertThat(event.isFree()).isFalse();
//
//        // Given
//        event = Event.builder()
//                .basePrice(0)
//                .maxPrice(100)
//                .build();
//
//        // When
//        event.update();
//
//        // Then
//        assertThat(event.isFree()).isFalse();

        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private Object[] parametersForTestFree() {
        return new Object[]{
                new Object[]{0, 0, true},
                new Object[]{100, 0, false},
                new Object[]{0, 100, false},
                new Object[]{100, 200, false}

        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) {
//        // Given
//        Event event = Event.builder()
//                .location("강남역")
//                .build();
//
//        // When
//        event.update();
//
//        // Then
//        assertThat(event.isOffline()).isTrue();
//
//        // Given
//        event = Event.builder()
//                .location(null)
//                .build();
//
//        // When
//        event.update();
//
//        // Then
//        assertThat(event.isOffline()).isFalse();

        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private Object[] parametersForTestOffline() {
        return new Object[]{
                new Object[]{"강남", true},
                new Object[]{null, false},
                new Object[]{"            ", false}
        };
    }

}