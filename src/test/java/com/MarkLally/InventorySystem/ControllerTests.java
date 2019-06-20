package com.MarkLally.InventorySystem;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void contextLoads() throws Exception{
		this.mockMvc.perform(get("/helloTest")).andDo(print()).andExpect(status().isOk())
			.andExpect(content().string(containsString("Hello World")));
	}
	
	@Test
	public void testGetInventory() throws Exception{
		
		// Assert endpoints return HTTP status codes for success (200)
		this.mockMvc.perform(get("/")).andExpect(status().isOk());
		this.mockMvc.perform(get("/addItemForm")).andExpect(status().isOk());
		this.mockMvc.perform(get("/filterCategory")).andExpect(status().isOk());
		this.mockMvc.perform(get("/filterForm")).andExpect(status().isOk());
		
		// Assert codes in in the 4xx range
		this.mockMvc.perform(get("/save")).andExpect(status().isMethodNotAllowed());
		this.mockMvc.perform(get("/update")).andExpect(status().is4xxClientError());
		this.mockMvc.perform(get("/delete")).andExpect(status().is4xxClientError());
		
		
		
	}

}
