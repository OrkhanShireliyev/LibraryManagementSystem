package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.OrderDTO;
import com.company.librarymanagementsystem.model.Order;
import com.company.librarymanagementsystem.request.OrderRequest;
import com.company.librarymanagementsystem.service.inter.OrderServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceInter orderServiceInter;

    @GetMapping("/")
    String viewOrderPage(){
        return "order";
    }

    @PostMapping("/create/{bookId}/{studentId}")
    String save(@ModelAttribute OrderRequest orderRequest, @PathVariable List<Long> bookIds, @PathVariable Long studentId, Model model){
        OrderRequest order=orderServiceInter.save(orderRequest,bookIds,studentId).getBody();
        model.addAttribute("order",order);
        return "order";
    }

    @PostMapping("/edit/{id}/{orderNumber}/{localDate}/{bookIds}/{studentId}")
    String update(@PathVariable Long id,
                  @PathVariable Long orderNumber,
                  @PathVariable LocalDate localDate,
                  @PathVariable List<Long> bookIds,
                  @PathVariable Long studentId,
                  Model model){
        Order orderUpdate=orderServiceInter.update(id, orderNumber, localDate, bookIds, studentId).getBody();
        model.addAttribute("orderUpdate",orderUpdate);
        return "order";
    }

    @GetMapping("/orders")
    String getAllOrders(Model model){
        List<OrderDTO> orders=orderServiceInter.getAllOrders().getBody();
        model.addAttribute("orders",orders);
        return "order";
    }

    @GetMapping("/getByOrderNumber/{orderNumber}")
    String getOrderByOrderNumber(@PathVariable Long orderNumber,Model model){
        OrderDTO orderDTO=orderServiceInter.getOrderByOrderNumber(orderNumber).getBody();
        model.addAttribute("orderDTO",orderDTO);
        return "order";
    }

    @PostMapping("/delete/{orderNumber}")
    String delete(@PathVariable Long orderNumber,Model model){
        orderServiceInter.delete(orderNumber);
        model.addAttribute("successMessage", "Author successfully deleted!");
        return "order";
    }
}
