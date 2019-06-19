package com.MarkLally.InventorySystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.MarkLally.InventorySystem.entity.Item;
import com.MarkLally.InventorySystem.repository.ItemRepository;
import com.MarkLally.InventorySystem.utility.UtilityMapping;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemOperationTests {
	
	Item exampleItem1, exampleItem2;
	List<Item> itemList;
	UtilityMapping<String, Integer> countsMap;
	
	int maxInvSize;
	int maxItemCount;
	
	@Autowired
	ItemRepository repo;
	
	@Before
	public void setup() {
		maxInvSize = 5; maxItemCount = 2;
		exampleItem1 = new Item(1, "Pen", 500, "School Appliances");
		exampleItem2 = new Item();
		itemList = new ArrayList<Item>();
		// Test size constraints with a max Inventory size of 5 and max item count of 2
		countsMap = new UtilityMapping<String, Integer>(maxInvSize, maxItemCount);
	}
	

	@Test
	public void testGetters() {
		assertEquals("Pen", exampleItem1.getName());
		assertEquals(500, exampleItem1.getPrice());
		assertEquals("School Appliances", exampleItem1.getCategory());
	}
	
	@Test
	public void testSetters() {
		exampleItem2.setName("Microscope");
		assertEquals("Microscope", exampleItem2.getName());
		exampleItem2.setPrice(3000);
		assertEquals(3000, exampleItem2.getPrice());
		exampleItem2.setCategory("Lab Equipment");
		assertEquals("Lab Equipment", exampleItem2.getCategory());
	}
	
	@Test
	public void testCRUDOperations() {
		// Saving
		repo.save(exampleItem1);
		assertNotEquals(Optional.empty(), repo.findById(1));
		assertTrue(repo.findById(1).get().getName().equals("Pen"));
		
		assertEquals(Optional.empty(), repo.findById(2));
		
		// Reading
		itemList = repo.findAll();
		assertTrue(itemList.size() == 1);
		
		// Updating	
		Item temp = repo.findById(1).get();
		temp.setName("Biro");
		repo.save(temp);
		assertTrue(repo.findById(1).get().getName().equals("Biro"));
		
		// Deleting
		repo.deleteById(1);
		assertEquals(Optional.empty(), repo.findById(1));
		itemList = repo.findAll();
		assertTrue(itemList.size() == 0);
	}
	
	@Test
	public void testQuantityConstraints() {
		
		String key = exampleItem1.getName();
		int value = 1;
		
		countsMap.put(key, value);
		assertTrue(countsMap.size() == 1);
		assertTrue(countsMap.get(key) == 1);
		
		exampleItem2 = new Item(2, "Book", 500, "School Appliances");
		Item exampleItem3 = new Item(3, "Projector", 500, "School Appliances");
		Item exampleItem4 = new Item(4, "Chalk", 500, "School Appliances");
		Item exampleItem5 = new Item(5, "Chair", 500, "School Appliances");
		Item exampleItem6 = new Item(6, "Ink", 500, "School Appliances");
		
		countsMap.put(exampleItem2.getName(), 1);
		countsMap.put(exampleItem3.getName(), 1);
		countsMap.put(exampleItem4.getName(), 1);
		countsMap.put(exampleItem5.getName(), 1);
		countsMap.put(exampleItem6.getName(), 1);
		
		// Check max size not exceeded
		assertTrue("Inventory max size exceeded, size is: " +countsMap.size(), countsMap.size() <= maxInvSize);
		Set<String> keys = countsMap.keySet();
		// Last item "Ink" should not be added - inventory is full
		assertEquals("[Pen, Book, Projector, Chalk, Chair]", keys.toString());
		
		// Increase amount of Pens past max
		countsMap.put(key, ++value); // Count now 2, Max is also 2;
		countsMap.put(key, ++value); // Count now 3, Max is 2;
		
		assertTrue("Max itemCount exceeded: "+countsMap.get(key),countsMap.get(key) <= 2);
	}
	
	@Test
	public void testCustomJPADataQueries() {
		
		// Create 4 dummy items 
		Item exampleItem3 = new Item(3, "marker", 100, "black");
		Item exampleItem4 = new Item(4, "crayon", 200, "black");
		Item exampleItem5 = new Item(5, "tipp-ex", 300, "white");
		Item exampleItem6 = new Item(6, "chalkdust", 400, "white");
		repo.save(exampleItem3);
		repo.save(exampleItem4);
		repo.save(exampleItem5);
		repo.save(exampleItem6);
		
		// Call custom search by category method
		// Equivalent to SQL -> SELECT * FROM item WHERE category = 'black'
		List<Item> searchResults = repo.findByCategoryIgnoreCase("blAcK");
		
		for(Item item:searchResults) {
			assertEquals("black", item.getCategory());
		}
		searchResults = repo.findByCategoryIgnoreCase("WHIte");
		
		for(Item item:searchResults) {
			assertEquals("white", item.getCategory());
		}
		
		// Call custom price range search
		// SQL BETWEEN clause includes the min and max bounds in its results
		searchResults = repo.findByPriceBetween(200, 300);
		assertTrue(searchResults.size() == 2);
		for(Item item: searchResults)
			assertTrue(item.getPrice() >= 200 && item.getPrice() <= 300);
		
		//TODO Add default max and min prices, query requires 2 min and max!
		
	}

	
}
