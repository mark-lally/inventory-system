package com.MarkLally.InventorySystem.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	
	// A list to hold retrieval results from db
	List<Item> itemList;
	// Used in /save to hold the results of a query for a given name
	List<Item> listByName;
	
	// Database size and item quantity constraints/business rules.
	// Held in application.properties for simple configuration and testing.
	@Value("${inventory.maxsize}")
	int MAX_INVENTORY_SIZE;	
	@Value("${inventory.item.maxcount}")
	int MAX_ITEM_COUNT;
	
	@GetMapping("/")
	public String getInventory(Model theModel) {
		
		// Use JPARepository's built in findAll method to return all items in database
		itemList = itemRepo.findAll();
		// Add findAll results and inventory constraints to the Model for use in View
		theModel.addAttribute("items", itemList);
		theModel.addAttribute("MAX_INV", MAX_INVENTORY_SIZE);
		
		return "index";
	}
	
	@GetMapping("/addItemForm")
	public String addItem(Model theModel) {
		
		// Create a blank Item object to bind form data to
		Item blankItem = new Item();
		theModel.addAttribute("item", blankItem);
		
		return "addItemForm";
	}
	
	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("item") Item itemToSave,
						BindingResult theBindingResult) {
		
		// Validate results held in theBindingResult
		if(theBindingResult.hasErrors())
			return "addItemForm";
		
		// 
		
		// Call custom query of the name submitted in form
		// Returns a list of all items by that name
		listByName= itemRepo.findByName(itemToSave.getName());
		
		/* Before saving item, check whether -
		 *   - Max entries for that item already exist or
		 *   - the Inventory has reached max capacity
		 */
		if(listByName.size() >= MAX_ITEM_COUNT || itemRepo.findAll().size() >= MAX_INVENTORY_SIZE) {
			return "addItemForm";
		}
		itemRepo.save(itemToSave);
		// Redirect to prevent duplicate submissions
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
		
		// Pre-populate the form with the item to edit's details
		theModel.addAttribute("item", itemToEdit);
		
		return "addItemForm";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("itemId") int idOfItemToDelete) {
		
		// JpaRepository built-in delete method
		itemRepo.deleteById(idOfItemToDelete);
		
		// Refresh index to load list based on updated model
		return "redirect:";
	}
	
	@GetMapping("/filterCategory")
	public String filterCategory(@RequestParam(value="searchTerm", required=false) String searchTerm,Model theModel) {
		
		// Call custom query on search term and apply result to model
		itemList = itemRepo.findByCategoryIgnoreCase(searchTerm);		
		theModel.addAttribute("items", itemList);
		
		return "index";
	}
	
	@GetMapping("/filterPrice")
	public String filterPrice(@RequestParam(value="min", required=false) int minPrice,
							  @RequestParam(value="max", required=false) int maxPrice,
							  Model theModel) {
		
		// Call custom price search query and update model
		itemList = itemRepo.findByPriceBetween(minPrice, maxPrice);
		theModel.addAttribute("items", itemList);
		
		return "index";		
	}
	
	@GetMapping("/getMostRecent")
	public String getMostRecent(Model theModel) {
		
		// Custom query returns last 5 inserted based on automatically generated timestamps
		itemList = itemRepo.findTop5ByOrderByDateDesc();
		theModel.addAttribute("items", itemList);
		
		return "index";
	}
	
	@GetMapping("/filterForm")
	public String filterForm() {
		
		return "filter";
	}
	
}
