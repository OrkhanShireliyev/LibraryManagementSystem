package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.OrderDTO;
import com.company.librarymanagementsystem.mapper.OrderMapper;
import com.company.librarymanagementsystem.model.Order;
import com.company.librarymanagementsystem.request.OrderRequest;
import com.company.librarymanagementsystem.service.inter.OrderServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceInter orderServiceInter;
    private final OrderMapper orderMapper;

    @GetMapping("/")
    String viewOrderPage(){
        return "order";
    }

    @PostMapping("/save/{bookIds}/{studentId}")
    ResponseEntity<OrderRequest> save(@RequestBody OrderRequest orderRequest,
                                      @PathVariable String bookIds,
                                      @PathVariable Long studentId,
                                      Model model){
        System.out.println(orderRequest);
        System.out.println(bookIds);
        System.out.println(studentId);

        List<Long> bookListId= Arrays.stream(bookIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        OrderRequest order=orderServiceInter.save(orderRequest,bookListId,studentId).getBody();
        model.addAttribute("order",order);
        return ResponseEntity.ok(orderRequest);
    }

    @PostMapping("/update/{orderId}")
    ResponseEntity<OrderDTO> update(
                  @PathVariable("orderId") Long id,
                  @RequestParam("orderNumber") Long orderNumber,
                  @RequestParam("localDate") LocalDate localDate,
                  @RequestParam("deliveryDate") LocalDate deliveryDate,
                  @RequestParam("bookIds") List<Long> bookIds,
                  @RequestParam("studentId") Long studentId,
                  Model model){
        System.out.println("id: "+id);
        Order orderUpdate=orderServiceInter.update(id, orderNumber, localDate,deliveryDate, bookIds, studentId).getBody();
        model.addAttribute("orderUpdate",orderUpdate);
        OrderDTO orderDTO=orderMapper.orderToOrderDTO(orderUpdate);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/orders")
    ResponseEntity<List<OrderDTO>> getAllOrders(){
        List<OrderDTO> orderDTOS=orderServiceInter.getAllOrders().getBody();
        if (orderDTOS == null || orderDTOS.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
        return ResponseEntity.ok(orderDTOS);
    }

    @GetMapping("/getById/{id}")
    ResponseEntity<OrderDTO> getById(@PathVariable Long id,Model model){
        OrderDTO orderDTO=orderServiceInter.getById(id).getBody();
        model.addAttribute("orderDTO",orderDTO);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/return/{id}/{deliveryTime}")
    ResponseEntity<OrderDTO> returnOrder(@PathVariable Long id,@PathVariable LocalDate deliveryTime,Model model){
        OrderDTO orderDTO=orderServiceInter.returnOrder(id,deliveryTime).getBody();
        model.addAttribute("orderDTO",orderDTO);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/getByOrderNumber/{orderNumber}")
    ResponseEntity<OrderDTO> getByOrderNumber(@PathVariable Long orderNumber,Model model){
        OrderDTO orderDTO=orderServiceInter.getById(orderNumber).getBody();
        model.addAttribute("orderDTO",orderDTO);
        return ResponseEntity.ok(orderDTO);
    }

    @DeleteMapping("/delete/{id}")
    String delete(@PathVariable Long id,Model model){
        orderServiceInter.delete(id);
        model.addAttribute("successMessage", "Author successfully deleted!");
        return "order";
    }
}
