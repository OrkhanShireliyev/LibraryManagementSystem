package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.dto.OrderDTO;
import com.company.librarymanagementsystem.exception.NotFoundException;
import com.company.librarymanagementsystem.mapper.OrderMapper;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.model.Order;
import com.company.librarymanagementsystem.model.Student;
import com.company.librarymanagementsystem.repository.BookRepository;
import com.company.librarymanagementsystem.repository.OrderRepository;
import com.company.librarymanagementsystem.repository.StudentRepository;
import com.company.librarymanagementsystem.request.OrderRequest;
import com.company.librarymanagementsystem.service.inter.OrderServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderServiceInter {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final BookRepository bookRepository;

    private final StudentRepository studentRepository;

    @Override
    public ResponseEntity<OrderRequest> save(OrderRequest orderRequest, List<Long> bookIds, Long studentId) {
        List<Book> books = new ArrayList<>();
        Book book;

        for (Long getBookId : bookIds) {
            book = bookRepository.findById(getBookId)
                    .orElseThrow(() -> new NoSuchElementException("Not found book by id=" + getBookId));
            books.add(book);
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Not found student by id=" + studentId));


        try {
            Order order = orderMapper.orderRequestToOrder(orderRequest);
            order.setBooks(books);
            order.setStudent(student);
            orderRepository.save(order);
            log.info("Successfully created {}", order);
            return new ResponseEntity<>(orderRequest, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when creating order!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Order> update(Long id, Long orderNumber, LocalDate localDate, List<Long> bookIds, Long studentId) {
        List<Book> books = new ArrayList<>();
        Book book;

        for (Long getBookId : bookIds) {
            book = bookRepository.findById(getBookId)
                    .orElseThrow(() -> new NoSuchElementException("Not found book by id=" + getBookId));
            books.add(book);
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Not found student by id=" + studentId));

        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Not found order by id=" + id));
            order.setOrderNumber(orderNumber);
            order.setLocalDate(localDate);
            order.setBooks(books);
            order.setStudent(student);
            orderRepository.save(order);
            log.info("Successfully created {}", order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when updating order!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new NotFoundException("Not found orders!");
        }
        List<OrderDTO> orderDTOS = new ArrayList<>();

        try {
            for (Order order : orders) {
                OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
                orderDTOS.add(orderDTO);
            }
            log.info("Successfully retrieved {}", orderDTOS);
            return new ResponseEntity<>(orderDTOS, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when retrieving orders!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<OrderDTO> getOrderByOrderNumber(Long orderNumber) {
        Order order = orderRepository.findOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new NoSuchElementException("Not found order orderNumber=" + orderNumber);
        }
        try {
            OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
            log.info("Successfully retrieved {}", orderDTO);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when retrieving order!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> delete(Long orderNumber) {
        Order order = orderRepository.findOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new NoSuchElementException("Not found order orderNumber=" + orderNumber);
        }
        try {
            orderRepository.delete(order);
            log.info("Successfully deleted {}", order);
            return new ResponseEntity<>("Successfully deleted!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when deleting order!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
