package com.ecommerce.micrommerce.web.dao;

import com.ecommerce.micrommerce.web.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
	
	@Query("SELECT p FROM Product p ORDER BY p.nom ASC")
	List<Product> findNomOrderByAlphabetical();
	
    Product findById(int id);
    
    List<Product> findByPrixGreaterThan(int prixLimit);

}
