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
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public ResponseEntity<OrderRequest> save(OrderRequest orderRequest, List<Long> bookIds, Long studentId) {
        List<Book> books = bookRepository.findAllById(bookIds);
        if (books.isEmpty()) {
            throw new NoSuchElementException("Not found book!");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Not found student by id=" + studentId));

        for (Book book : books) {
            if (book.getStockCount()<=0){
                throw new ArithmeticException(StringUtils.capitalize(book.getName())+" book run out!");
            }
            book.setStockCount(book.getStockCount() - 1);
        }

        try {
            Order order = orderMapper.orderRequestToOrder(orderRequest);

            order.setBooks(books);

            order.setStudent(student);
            System.out.println(order);
            orderRepository.save(order);
            bookRepository.saveAll(books);
            log.info("Successfully created {}", order);
            return new ResponseEntity<>(orderRequest, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when creating order!",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<OrderDTO> returnOrder(Long orderNumber,LocalDate deliveryDate) {
        Order order = orderRepository.findOrderByOrderNumber(orderNumber);
                if(order==null){
                    new NoSuchElementException("Not found order with orderNumber=" + orderNumber);
                };

        List<Book> books=new ArrayList<>();

        for (Book book: order.getBooks()){
            Long id=book.getId();
            book=bookRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Not found book with id=" + id));
            book.setStockCount(book.getStockCount()+1);
            books.add(book);
        }

        try {
            order.setDeliveryTime(deliveryDate);
            System.out.println(order);
            orderRepository.save(order);
            bookRepository.saveAll(books);
            OrderDTO orderDTO=orderMapper.orderToOrderDTO(order);
            log.info("The book was successfully delivered! {}", order);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when delivering order!",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Order> update(Long id, Long orderNumber, LocalDate localDate, LocalDate deliveryDate, List<Long> bookIds, Long studentId) {
        System.out.println("order update metoduna daxil oldu");
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
            System.out.println("try daxil oldu");
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Not found order by id=" + id));
            System.out.println("Order id: "+id);
            order.setOrderNumber(orderNumber);
            order.setLocalDate(localDate);
            order.setDeliveryTime(deliveryDate);
            order.setBooks(books);
            order.setStudent(student);
            orderRepository.save(order);
            log.info("Successfully created {}", order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when updating order!",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders == null || orders.isEmpty()) {
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
            log.error("Error occurred when retrieving orders!",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<OrderDTO> getById(Long id) {
        Order order = orderRepository.findById(id).get();
        if (order == null) {
            throw new NoSuchElementException("Not found order id=" + id);
        }
        try {
            OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
            log.info("Successfully retrieved {}", orderDTO);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when retrieving order by id!",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<OrderDTO> getByOrderName(Long orderNumber) {
        Order order = orderRepository.findOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new NoSuchElementException("Not found order orderNumber=" + orderNumber);
        }
        try {
            OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
            log.info("Successfully retrieved {}", orderDTO);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when retrieving order by orderNumber!",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        Order order = orderRepository.findById(id).get();
        if (order == null) {
            throw new NoSuchElementException("Not found order id=" + id);
        }
        try {
            orderRepository.delete(order);
            log.info("Successfully deleted {}", order);
            return new ResponseEntity<>("Successfully deleted!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when deleting order!",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
