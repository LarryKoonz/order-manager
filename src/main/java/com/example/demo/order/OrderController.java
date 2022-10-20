package com.example.demo.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping(path="api/v1/order")

public class OrderController {

    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ResponseStatus(code=HttpStatus.OK)
    @GetMapping(path="{id}")
    public Order getOrder(@PathVariable("id") Long id){
        logger.info("Getting order id: " + id);
        try{
            return this.orderService.getOrder(id);
        }catch(IllegalStateException ex){
            logger.error("Didn't find an order with the id: " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @ResponseStatus(code=HttpStatus.CREATED)
    @PostMapping
    public Order addNewOrder(@RequestBody Order order){
        logger.info("Adding new order");
        try{
            order = orderService.addNewOrder(order); // TODO remove after testing
            return orderService.addNewOrder(order);
        }catch(IllegalArgumentException ex){
            logger.error("The order name: " + order.getName() + " is invalid");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    @PutMapping(path = "{id}")
    public void changeOrder(@PathVariable("id") Long id, @RequestParam String name){
        logger.info("Changing order " + id);
        try{
            orderService.changeOrder(id, name);
        }catch(Exception ex){
            logger.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    @ResponseStatus(code=HttpStatus.OK)
    @GetMapping(path="lastMonth")
    public List<Order> getLastMonthOrders(){
        logger.info("Getting last month orders");
        List<Order> lastMonthOrders = orderService.getLastMonthOrders();
        if (lastMonthOrders.isEmpty()){
            logger.warn("There are no orders in the last month");
        }
        return lastMonthOrders;

    }

    @ResponseStatus(code=HttpStatus.OK)
    @GetMapping(path="lastWeek")
    public List<Order> getLastWeekOrders(){
        logger.info("Getting last week orders");
        List<Order> lastWeekOrders = orderService.getLastWeekOrders();
        if (lastWeekOrders.isEmpty()){
            logger.warn("There are no orders in the last week");
        }
        return lastWeekOrders;

    }

    @ResponseStatus(code=HttpStatus.OK)
    @GetMapping(path="lastDay")
    public List<Order> getLastDayOrders(){
        logger.info("Getting last day orders");
        List<Order> lastDayOrders = orderService.getLastDayOrders();
        if (lastDayOrders.isEmpty()){
            logger.warn("There are no orders in the last day");
        }
        return lastDayOrders;
    }

}
