package com.company.librarymanagementsystem.mapper;

import com.company.librarymanagementsystem.dto.OrderDTO;
import com.company.librarymanagementsystem.model.Order;
import com.company.librarymanagementsystem.request.OrderRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order orderRequestToOrder(OrderRequest orderRequest);

    OrderDTO orderToOrderDTO(Order order);

}
