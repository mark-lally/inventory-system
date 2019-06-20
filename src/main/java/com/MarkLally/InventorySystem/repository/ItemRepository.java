package com.MarkLally.InventorySystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.MarkLally.InventorySystem.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	
	// Custom query to filter search results by category
	List<Item> findByCategoryIgnoreCase(String searchTerm);
	
	// Custom query to filter results by a given price range
	List<Item> findByPriceBetween(int minPrice, int maxPrice);
	
	// Get last 5 items added to inventory based on SQL timestamps
	List<Item> findTop5ByOrderByDateDesc();
	
	// Query to help enforce max item count of 5
	List<Item> findByName(String itemName);
	
}
