package com.example.demo.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findOrderById(Long id);

    Optional<List<Order>> findOrdersByTimeStampBetween(ZonedDateTime firstLocalDateTime, ZonedDateTime secondLocalDateTIme);
}
