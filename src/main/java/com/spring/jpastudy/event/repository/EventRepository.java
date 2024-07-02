package com.spring.jpastudy.event.repository;

import com.spring.jpastudy.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository  extends JpaRepository<Event,Long>,EventRepositoryCustom {
}
