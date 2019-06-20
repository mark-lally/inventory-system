package com.MarkLally.InventorySystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.MarkLally.InventorySystem.entity.Item;
import com.MarkLally.InventorySystem.repository.ItemRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemOperationTests {
	
	Item exampleItem1, exampleItem2;
	List<Item> itemList;
	
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
	}
	

	@Test
	public void testGetters() {
		assertEquals("Pen", exampleItem1.getName());
		assertEquals(500, exampleItem1.getPrice());
		assertEquals("School Appliances", exampleItem1.getCategory());
		assertEquals(1, exampleItem1.getId());
		
		long millis = System.currentTimeMillis();
		Date testDate = new Date(millis);
		exampleItem1.setDate(testDate);
		assertEquals(testDate, exampleItem1.getDate());
	}
	
	@Test
	public void testSettersAndToString() {
		exampleItem2.setName("Microscope");
		assertEquals("Microscope", exampleItem2.getName());
		exampleItem2.setPrice(3000);
		assertEquals(3000, exampleItem2.getPrice());
		exampleItem2.setCategory("Lab Equipment");
		assertEquals("Lab Equipment", exampleItem2.getCategory());
		exampleItem2.setId(4);
		assertEquals(4, exampleItem2.getId());
		
		assertEquals("Item [id=" + exampleItem2.getId() + ", "
						+ "name=" + exampleItem2.getName() + ", "
						+ "price=" + exampleItem2.getPrice() + ", "
						+ "category=" + exampleItem2.getCategory() + ", "
						+ "date=" + exampleItem2.getDate()
						+ "]", exampleItem2.toString());
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
		// SQL 'BETWEEN' clause includes the min and max bounds in its results
		searchResults = repo.findByPriceBetween(200, 300);
		assertTrue(searchResults.size() == 2);
		for(Item item: searchResults)
			assertTrue(item.getPrice() >= 200 && item.getPrice() <= 300);
		
		
		
	}

	
}
