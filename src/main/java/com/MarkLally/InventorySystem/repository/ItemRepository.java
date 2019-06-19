package com.MarkLally.InventorySystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.MarkLally.InventorySystem.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	
	// Custom query to filter search results by category
	List<Item> findByCategoryIgnoreCase(String searchTerm);
	
	// Custom query to filter results by a given price range
	List<Item> findByPriceBetween(int minPrice, int maxPrice);
	
}
