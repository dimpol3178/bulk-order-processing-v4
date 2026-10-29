package com.bdo.exam.repository;

import com.bdo.exam.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    
    Page<Order> findByCustomerId(String customerId, Pageable pageable);

    List<Order> findByCustomerId(String customerId);

    @Query("SELECT o FROM Order o WHERE o.status = 'PROCESSED'")
    List<Order> findAllProcessedOrders();

    @Query("SELECT o.customerId, SUM(o.totalPrice) FROM Order o WHERE o.status = 'PROCESSED' GROUP BY o.customerId ORDER BY SUM(o.totalPrice) DESC")
    List<Object[]> findTopCustomers();

    @Query("SELECT o.productCode, SUM(o.quantity) FROM Order o WHERE o.status = 'PROCESSED' GROUP BY o.productCode ORDER BY SUM(o.quantity) DESC")
    List<Object[]> findTopProducts();
}
