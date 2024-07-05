package com.spring.jpastudy.event.repository;

import com.spring.jpastudy.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EventRepositoryCustom {

    Page<Event> findEvents(Pageable pageable, String sort);

    // ...

    // ...

    // ...
}