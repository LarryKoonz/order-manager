package com.example.demo.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path="api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path="{orderId}")
    public Order getOrder(@PathVariable("orderId") Long id){
        return this.orderService.getOrder(id);
    }

    @PostMapping
    public void addNewOrder(@RequestBody Order order){
        orderService.addNewOrder(order);
    }

    @PutMapping(path = "{orderId}")
    public void changeOrder(@PathVariable("orderId") Long orderId, @RequestParam String name){ // TODO localhost:8080/api/v1/order/1?name=hamburger
        orderService.changeOrder(orderId, name);
    }

    @GetMapping(path="lastMonth")
    public List<Order> getLastMonthOrders(){
        return orderService.getLastMonthOrders();
    }

    @GetMapping(path="lastWeek")
    public List<Order> getLastWeekOrders(){
        return orderService.getLastWeekOrders();
    }

    @GetMapping(path="lastDay")
    public List<Order> getLastDayOrders(){
        return orderService.getLastDayOrders();
    }

}
