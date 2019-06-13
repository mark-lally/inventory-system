package com.MarkLally.InventorySystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MarkLally.InventorySystem.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	
}
