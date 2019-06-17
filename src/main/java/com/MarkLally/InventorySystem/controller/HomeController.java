package com.MarkLally.InventorySystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.MarkLally.InventorySystem.entity.Item;
import com.MarkLally.InventorySystem.repository.ItemRepository;

@Controller
public class HomeController {
	
	@Autowired
	ItemRepository itemRepo;
	
	List<Item> itemList;
	
	@GetMapping("/")
	public String getInventory(Model theModel) {
		
		// Use JPARepository's built in findAll method to return all items in database
		itemList = itemRepo.findAll();
		theModel.addAttribute("items", itemList);
		
		return "index";
	}
	
	@GetMapping("/addItemForm")
	public String addItem(Model theModel) {
		
		// Create a blank Item to bind form data to
		Item blankItem = new Item();
		theModel.addAttribute("item", blankItem);
		
		return "addItemForm";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute("item") Item itemToSave) {
		
		itemRepo.save(itemToSave);
		// Return to index (default mapping for index.html is "/" which is equivalent to "")
		return "redirect:";
	}
	
	@GetMapping("/update")
	public String update(@RequestParam("itemId") int idOfItemToEdit, Model theModel) {
		
		// Object reference to hold Item to update
		Item itemToEdit;
		
		// Optionals contain either an object or null
		Optional<Item> tempItem = itemRepo.findById(idOfItemToEdit);
		
		// Check if null
		if(tempItem.isPresent())
			itemToEdit = tempItem.get();
		else // if null
			throw new RuntimeException("Error finding item. No item with the id: " + idOfItemToEdit);
		
		// Prepopulate the form with the item to edit's details
		theModel.addAttribute("item", itemToEdit);
		
		return "addItemForm";
	}
}
