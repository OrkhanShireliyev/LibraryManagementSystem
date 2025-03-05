package com.company.librarymanagementsystem.service.inter;

import com.company.librarymanagementsystem.dto.OrderDTO;
import com.company.librarymanagementsystem.model.Order;
import com.company.librarymanagementsystem.request.OrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface OrderServiceInter {
    ResponseEntity<OrderRequest> save(OrderRequest orderRequest, List<Long> bookIds, Long studentId);

    ResponseEntity<OrderDTO> returnOrder(Long orderNumber,LocalDate deliveryDate);

    ResponseEntity<Order> update(Long id,
                                 Long orderNumber,
                                 LocalDate localDate,
                                 LocalDate deliveryDate,
                                 List<Long> bookIds,
                                 Long studentId);

    ResponseEntity<List<OrderDTO>> getAllOrders();

    ResponseEntity<OrderDTO> getById(Long id);

    ResponseEntity<OrderDTO> getByOrderName(Long id);

    ResponseEntity<String> delete(Long id);
}
