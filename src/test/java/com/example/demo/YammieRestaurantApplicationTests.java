package com.example.demo;

import com.example.demo.order.Order;
import com.example.demo.order.OrderController;
import com.example.demo.order.OrderRepository;
import com.example.demo.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@ActiveProfiles("test")
class OrderControllerTests{
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	private ZonedDateTime timeStamp;

	private ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		this.timeStamp = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Test
	void shouldReturn200AndGetOneUserById() throws Exception{
		Long orderId = 1L;
		Order order = new Order(orderId, "Coffee", this.timeStamp);
		given(orderService.getOrder(orderId)).willReturn(order);
		this.mockMvc.perform(get("http://localhost:8080/api/v1/order/{id}", orderId)).andExpect(status().isOk()).
				andExpect(jsonPath("$.name").value(order.getName()));
	}
	@Test
	void shouldReturn404WhenNoOrderById() throws Exception{
		Long orderId = 1L;
		IllegalStateException ex = new IllegalStateException();
		given(orderService.getOrder(orderId)).willThrow(ex);
		this.mockMvc.perform(get("http://localhost:8080/api/v1/order/{id}", orderId)).andExpect(status().isNotFound());
	}

	@Test
	void shouldReturn201AndGetSameOrderWhenAddNewOrder() throws Exception{
		Order order = new Order(1L, "eggs", this.timeStamp);
		given(orderService.addNewOrder(any(Order.class))).willReturn(order);

		this.mockMvc.perform(post("http://localhost:8080/api/v1/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(order)))
				.andExpect(status().isCreated()).
				andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("eggs"));

	}
	@Test
	void shouldReturn400WhenTryingToAddInvalidOrder() throws Exception{
		Order order = new Order("      ");
		IllegalArgumentException ex = new IllegalArgumentException();
		given(orderService.addNewOrder(order)).willThrow(ex);
		this.mockMvc.perform(post("http://localhost:8080/api/v1/order")).andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturn204AndReturnNothingWhenTryingToChangeValidOrder() throws Exception{
		doNothing().when(orderService).changeOrder(1L, "cheese");
		this.mockMvc.perform(put("http://localhost:8080/api/v1/order/1?name=goat")).andExpect(status().isNoContent());
	}
	@Test
	void shouldReturn400WhenTryingToChangeInvalidOrder() throws Exception{
		IllegalStateException ex = new IllegalStateException();
		doThrow(ex).when(orderService).changeOrder(1L, "cheese");
		this.mockMvc.perform(put("http://localhost:8080/api/v1/order/1?name=goat")).andExpect(status().isNoContent());
	}

	@Test
	void shouldReturn200AndGetLastMonthOrders() throws Exception{
		long orderId = 1L;
		List<Order> orderList = Arrays.asList(new Order(orderId, "Coffee", this.timeStamp));
		given(orderService.getLastMonthOrders()).willReturn(orderList);
		this.mockMvc.perform(get("http://localhost:8080/api/v1/order/lastMonth")).andExpect(status().isOk()).
				andExpect(jsonPath("$.size()").value(1));
	}

	@Test
	void shouldReturn200AndGetLastWeekOrders() throws Exception{
		long orderId = 1L;
		List<Order> orderList = Arrays.asList(new Order(orderId, "Coffee", this.timeStamp));
		given(orderService.getLastWeekOrders()).willReturn(orderList);
		this.mockMvc.perform(get("http://localhost:8080/api/v1/order/lastWeek")).andExpect(status().isOk()).
				andExpect(jsonPath("$.size()").value(1));
	}

	@Test
	void shouldReturn200AndGetLastDayOrders() throws Exception{
		long orderId = 1L;
		List<Order> orderList = Arrays.asList(new Order(orderId, "Coffee", this.timeStamp));
		given(orderService.getLastDayOrders()).willReturn(orderList);
		this.mockMvc.perform(get("http://localhost:8080/api/v1/order/lastDay")).andExpect(status().isOk()).
				andExpect(jsonPath("$.size()").value(1));
	}

}

@ExtendWith(MockitoExtension.class)
class OrderServiceTests {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;



	@Test
	void shouldReturnExistedOrder(){
		ZonedDateTime timeStamp = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
		Order order = new Order(1L, "cheese", timeStamp);
		given(orderRepository.findOrderById(1L)).willReturn(Optional.of(order));
		Order givenOrder = orderService.getOrder(1L);
		Assertions.assertEquals(givenOrder, order);
	}
	@Test()
	void shouldNotReturnNotExistedOrder() {
		given(orderRepository.findOrderById(1L)).willReturn(Optional.empty());
		try {
			Order givenOrder = orderService.getOrder(1L);
			fail("Exception wasn't thrown when order wasn't found");
		} catch(IllegalStateException ex){
			assertEquals("order with id: 1 doesn't exist", ex.getMessage());
		}
	}

	@Test
	void shouldAddNewValidOrder() {
		ZonedDateTime timeStamp = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
		Order order = new Order("cheese");
		order.setTimeStamp(timeStamp);
		Order orderWithId = new Order(1L, "cheese", timeStamp);
		given(orderRepository.save(order)).willReturn(orderWithId);
		Order givenOrder = orderService.addNewOrder(order);
		Assertions.assertEquals(givenOrder, orderWithId);
	}
	@Test
	void shouldNotAddNewInvalidOrder() {
		Order order = new Order("    ");
		try{
			orderService.addNewOrder(order);
			fail("Exception wasn't thrown when the name of the order wasn't valid");
		}catch (IllegalArgumentException ex){
			assertEquals("The order name: " + order.getName() + " is invalid", ex.getMessage());
		}
	}

	@Test
	void shouldChangeValidOrder() {
		ZonedDateTime timeStamp = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
		Order order = new Order(1L, "cheese", timeStamp);
		given(orderRepository.findOrderById(1L)).willReturn(Optional.of(order));
		orderService.changeOrder(1L, "goat");
		Assertions.assertEquals(order.getName(), "goat");
	}
	@Test
	void shouldNotChangeInvalidOrder() {
		given(orderRepository.findOrderById(1L)).willReturn(Optional.empty());
		try {
			orderService.changeOrder(1L, "goat");
			fail("Exception wasn't thrown when order wasn't found");
		} catch(IllegalStateException ex){
			assertEquals("order with id " + 1L + " doesn't exist", ex.getMessage());
		}
	}

}
