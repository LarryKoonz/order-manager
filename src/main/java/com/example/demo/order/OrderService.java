package com.example.demo.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public Order getOrder(Long id){
        Optional<Order> dbOrder = orderRepository.findOrderById(id);
        if (dbOrder.isEmpty()){
            throw new IllegalStateException("order with id: " + id + " doesn't exist");
        }
        return dbOrder.get();
    }

    private boolean isValidOrderName(String name){
        if (name == null){
            return false;
        }
        return !(name.trim().isEmpty());
    }

    public void addNewOrder(Order order) {
        if (isValidOrderName(order.getName())){
            ZonedDateTime timeStamp = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
            order.setTimeStamp(timeStamp);
            orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("The order name: " + order.getName() + " is invalid");
        }
    }

    @Transactional
    public void changeOrder(Long orderId, String name){
        Order dbOrder = orderRepository.findOrderById(orderId).orElseThrow(()
                -> new IllegalStateException("order with id " + orderId + " doesn't exist"));
        if (dbOrder.wasOrderedBeforeSpecifiedPeriod(0, 0, 0, 15 * 60)){
            dbOrder.setName(name);
        }else{
            throw new IllegalStateException("The given order can't be changed, because it has exceeded the time that we can change it");
        }
    }

    public List<Order> getLastMonthOrders(){
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        ZonedDateTime currentTimeMinusOneMonth = currentTime.minusMonths(1);
        Optional<List<Order>> dbOrders = orderRepository.findOrdersByTimeStampBetween(currentTimeMinusOneMonth, currentTime);
        if (dbOrders.isEmpty()){
            throw new IllegalStateException("There are no orders in the last month");
        }
        return dbOrders.get();
    }

    public List<Order> getLastWeekOrders() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        ZonedDateTime currentTimeMinusOneWeek = currentTime.minusWeeks(1);
        Optional<List<Order>> dbOrders = orderRepository.findOrdersByTimeStampBetween(currentTimeMinusOneWeek, currentTime);
        if (dbOrders.isEmpty()){
            throw new IllegalStateException("There are no orders in the last week");
        }
        return dbOrders.get();
    }

    public List<Order> getLastDayOrders() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        ZonedDateTime currentTimeMinusOneDay = currentTime.minusDays(1);
        Optional<List<Order>> dbOrders = orderRepository.findOrdersByTimeStampBetween(currentTimeMinusOneDay, currentTime);
        if (dbOrders.isEmpty()){
            throw new IllegalStateException("There are no orders in the last day");
        }
        return dbOrders.get();
    }
}
