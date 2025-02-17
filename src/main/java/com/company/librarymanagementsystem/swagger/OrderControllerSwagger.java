package com.company.librarymanagementsystem.swagger;

import com.company.librarymanagementsystem.dto.OrderDTO;
import com.company.librarymanagementsystem.model.Order;
import com.company.librarymanagementsystem.request.OrderRequest;
import com.company.librarymanagementsystem.service.inter.OrderServiceInter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Operations related to order management")
public class OrderControllerSwagger {

    private final OrderServiceInter orderServiceInter;

    @Operation(summary = "Save order", description = "Fill order information and save it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved!"),
            @ApiResponse(responseCode = "500", description = "Can't save order!")
    })
    @PostMapping("/create/{bookId}/{studentId}")
    ResponseEntity<OrderRequest> save(@RequestBody OrderRequest orderRequest, @PathVariable List<Long> bookIds, @PathVariable Long studentId) {
        try {
            OrderRequest order = orderServiceInter.save(orderRequest, bookIds, studentId).getBody();
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update order", description = "Fill order for change order's info and update it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated!"),
            @ApiResponse(responseCode = "500", description = "Can't update order!")
    })
    @PutMapping("/edit/{id}/{orderNumber}/{localDate}/{deliveryDate}/{bookIds}/{studentId}")
    ResponseEntity<Order> update(@PathVariable Long id,
                                 @PathVariable Long orderNumber,
                                 @PathVariable LocalDate localDate,
                                 @PathVariable LocalDate deliveryDate,
                                 @PathVariable List<Long> bookIds,
                                 @PathVariable Long studentId
    ) {
        try {
            Order orderUpdate = orderServiceInter.update(id, orderNumber, localDate,deliveryDate, bookIds, studentId).getBody();
            return new ResponseEntity<>(orderUpdate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all orders", description = "Get all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The order was not found")
    })
    @GetMapping("/orders")
    ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            List<OrderDTO> orderDTOS = orderServiceInter.getAllOrders().getBody();
            if (orderDTOS == null || orderDTOS.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get a order by orderNumber", description = "Returns a order as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The order was not found")
    })
    @GetMapping("/getByOrderNumber/{id}")
    ResponseEntity<OrderDTO> getById(@PathVariable Long id) {
        try {
            OrderDTO orderDTO = orderServiceInter.getById(id).getBody();
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a order by orderNumber", description = "Delete a order as per the orderNumber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Can't delete order!")
    })
    @DeleteMapping("/delete/{orderNumber}")
    ResponseEntity<String> delete(@PathVariable Long orderNumber) {
        try {
        orderServiceInter.delete(orderNumber);
        return new ResponseEntity<>("Order successfully deleted!", HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>("Error occurred deleting order!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
}
