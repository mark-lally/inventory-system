package com.MarkLally.InventorySystem.utility;

import java.util.LinkedHashMap;


public class UtilityMapping<K, V extends Integer> extends LinkedHashMap<K, V> {
	
	// Set required constraints on Inventory size and Item quantity.
	private static int MAX_INVENTORY_SIZE;
	private static int MAX_ITEM_COUNT;
	
	public UtilityMapping(int maxInvSize, int maxItemCount) {
		super();
		this.MAX_INVENTORY_SIZE = maxInvSize;
		this.MAX_ITEM_COUNT = maxItemCount;
	}

	@Override
	public V put(K key, V value) {
		
		if(!containsKey(key) && size() >= MAX_INVENTORY_SIZE) {
			System.out.println("Inventory is full");
			return null;
		}
		else if(containsKey(key) && get(key)>= MAX_ITEM_COUNT) {
			System.out.println("Too many items");
			return null;
		}
		else
			return super.put(key, value);
	}
	
	

	
}
