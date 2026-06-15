package com.employee.management.repository;

import com.employee.management.model.CartItem;
import com.employee.management.model.Employee;
import com.employee.management.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
//public interface CartItemRepository extends JpaRepository<CartItem, Long> {
//    CartItem findByStudentAndProduct(Employee emp, Product product);
//}

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

//    CartItem findByEmployeeAndProduct(Employee emp, Product product);

    CartItem findByEmployeeAndProduct(Employee emp, Product product);

    void deleteByEmployeeAndProduct(Employee employee, Product product);

    List<CartItem> findByEmployee(Employee employee);
}
