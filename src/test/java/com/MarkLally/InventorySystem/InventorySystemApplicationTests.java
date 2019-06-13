package com.MarkLally.InventorySystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
public class InventorySystemApplicationTests {
	
	Item exampleItem1, exampleItem2;
	List<Item> itemList;
	
	@Autowired
	ItemRepository repo;
	
	@Before
	public void setup() {
		exampleItem1 = new Item("Pen", 500, "School Appliances");
		exampleItem2 = new Item();
		itemList = new ArrayList<Item>();
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
		
		repo.save(exampleItem1);
		assertNotEquals(Optional.empty(), repo.findById(1));
		assertTrue(repo.findById(1).get().getName().equals("Pen"));
		
		assertEquals(Optional.empty(), repo.findById(2));
		
		itemList = repo.findAll();
		assertTrue(itemList.size() == 1);
		
		Item temp = repo.findById(1).get();
		temp.setName("Biro");
		repo.save(temp);
		assertTrue(repo.findById(1).get().getName().equals("Biro"));
		
		repo.deleteById(1);
		assertEquals(Optional.empty(), repo.findById(1));
		itemList = repo.findAll();
		assertTrue(itemList.size() == 0);
	}

}
