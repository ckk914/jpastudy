package com.spring.jpastudy.event.repository;

import com.spring.jpastudy.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

//EventRepositoryCustom 여기에 상속
public interface EventRepository  extends JpaRepository<Event,Long>,EventRepositoryCustom {
}
