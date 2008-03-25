package gov.nih.nci.objectCart.jUnit.applicationService.impl;

import java.util.Collection;
import java.util.HashSet;

import gov.nih.nci.objectCart.client.ObjectCartClient;
import gov.nih.nci.objectCart.domain.Cart;
import gov.nih.nci.objectCart.domain.CartObject;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ObjectCartClientTest extends TestCase{

	public ObjectCartClient cartManager = null;
	@Before
	public void setUp() throws Exception {
		super.setUp();

		try {
			cartManager = new ObjectCartClient("unitTest");
		} catch (Exception e) {
			fail("Exception creating cart manager");
		}		
	}

	@After
	public void tearDown() throws Exception {	
	}

	@Test
	public void testCreateCart(){
		
		String name = "testCreateCart Cart Name";
		String userId = "testCreateCart Cart User";
		
		Cart cart = null;
		try {	
			 cart = cartManager.createCart(userId,name);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}
		assertNotNull(cart);	
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationTime());
		assertNotNull(cart.getLastActive());
		assertNotNull(cart.getExpirationDate());
		
		printCart(cart);
		
		try {	
			cartManager.deleteCart(cart);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}
	}	
	
	
	@Test
	public void testRetrieveCart() {
		
		String name = "testRetrieveCart Cart Name";
		String userId = "testRetrieveCart Cart User";
		
		Cart cart = null;
		try {	
			 cart = cartManager.createCart(userId,name);
		} catch (Exception e) {
			fail("Exception creating cart");
		}
		assertNotNull(cart);	
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationTime());
		assertNotNull(cart.getLastActive());
		assertNotNull(cart.getExpirationDate());
		
		printCart(cart);
		Cart secondCart = null;
		
		try {	
			cartManager.deleteCart(cart);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}
		
		try {
			secondCart = cartManager.retrieveCart(cart.getUserId(), cart.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception retrieving cart");
		}
		
		assertNull(secondCart);	
			
	}
	
	@Test
	public void testDeleteCart() {
		
		String name = "testDeleteCart Cart Name";
		String userId = "testDeleteCart Cart User";
		
		Cart cart = null;
		try {	
			 cart = cartManager.createCart(userId,name);
		} catch (Exception e) {
			fail("Exception creating cart");
		}
		assertNotNull(cart);	
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationTime());
		assertNotNull(cart.getLastActive());
		assertNotNull(cart.getExpirationDate());
		
		printCart(cart);
		Cart secondCart = null;
		
		try {
			secondCart = cartManager.retrieveCart(cart.getUserId(), cart.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception retrieving cart");
		}
		
		assertNotNull(secondCart);	
		assertEquals(name, secondCart.getName());
		assertEquals(userId, secondCart.getUserId());
		assertNotNull(secondCart.getId());
		assertNotNull(secondCart.getCreationTime());
		assertNotNull(secondCart.getLastActive());
		assertNotNull(secondCart.getExpirationDate());
		
		assertEquals(secondCart.getId(), cart.getId());
		
		printCart(secondCart);
			
	}
	
	@Test
	public void testAddandRetrieveObjects(){
		
		String name = "testCreateCart Cart Name";
		String userId = "testCreateCart Cart User";
		
		String type = "CDE Cart type";
		String dispName = "This is my Name";
		String data = "Some data here";
		
		Cart cart = null;
		try {	
			 cart = cartManager.createCart(userId,name);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}
		assertNotNull(cart);	
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationTime());
		assertNotNull(cart.getLastActive());
		assertNotNull(cart.getExpirationDate());
		
		printCart(cart);
		
		CartObject co = new CartObject();
		co.setData(data);
		co.setDisplayName(dispName);
		co.setType(type);
		co.setNativeId(Long.toString(System.currentTimeMillis()));
		
		try {	
			 cart = cartManager.storeObject(cart, co);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}
		
		Cart secondCart = null;
		
		
		try {
			secondCart = cartManager.retrieveCart(cart.getUserId(), cart.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception retrieving cart");
		}
		
		assertNotNull(secondCart);	
		assertEquals(name, secondCart.getName());
		assertEquals(userId, secondCart.getUserId());
		assertNotNull(secondCart.getId());
		assertNotNull(secondCart.getCreationTime());
		assertNotNull(secondCart.getLastActive());
		assertNotNull(secondCart.getExpirationDate());
		
		assertEquals(secondCart.getId(), cart.getId());
		
		printCart(secondCart);
	}	
	
	public void testAddCollection(){
		
		String name = "testAddCollection Cart Name";
		String userId = "testAddCollection Cart User";
		
		String type = "CDE Cart type";
		String dispName = "This is my Name";
		String data = "Some data here";
		
		Cart cart = null;
		try {	
			 cart = cartManager.createCart(userId,name);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}
		assertNotNull(cart);	
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationTime());
		assertNotNull(cart.getLastActive());
		assertNotNull(cart.getExpirationDate());
		
		printCart(cart);
		
		CartObject co = new CartObject();
		co.setData(data);
		co.setDisplayName(dispName);
		co.setType(type);
		co.setNativeId(Long.toString(System.currentTimeMillis()));
		CartObject co2 = new CartObject();
		co2.setData(data+"2");
		co2.setDisplayName(dispName+"2");
		co2.setType(type);
		co.setNativeId(Long.toString(System.currentTimeMillis()));
		
		Collection<CartObject> col = new HashSet<CartObject>();
		col.add(co);
		col.add(co2);
		
		try {	
			 cart = cartManager.storeObjectCollection(cart, col);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}
		
		Cart secondCart = null;
		
		
		try {
			secondCart = cartManager.retrieveCart(cart.getUserId(), cart.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception retrieving cart");
		}
		
		assertNotNull(secondCart);	
		assertEquals(name, secondCart.getName());
		assertEquals(userId, secondCart.getUserId());
		assertNotNull(secondCart.getId());
		assertNotNull(secondCart.getCreationTime());
		assertNotNull(secondCart.getLastActive());
		assertNotNull(secondCart.getExpirationDate());
		
		assertEquals(secondCart.getId(), cart.getId());
		
		printCart(secondCart);
	}	
	
	private void printCart(Cart cart) {
		System.out.println("------------------------");
		System.out.print("Id:         ");
		System.out.println(cart.getId());
		System.out.print("User Id:    ");
		System.out.println(cart.getUserId());
		System.out.print("Name:       ");
		System.out.println(cart.getName());
		System.out.print("Created On: ");
		System.out.println(cart.getCreationTime());
		System.out.print("Active On:  ");
		System.out.println(cart.getLastActive());
		System.out.print("Expires:    ");
		System.out.println(cart.getExpirationDate());
		System.out.print("Contents:   ");
		printContents(cart.getCartObjectCollection());
		System.out.println("______________________");
	}
	
	
	
	private void printContents(Collection<CartObject> objects) {
		
		if (objects != null) {
			System.out.println("Collection Size:"+objects.size());
			for (CartObject c: objects) {
				System.out.println(c.getId());
				System.out.println(c.getType());
				System.out.println(c.getDisplayName());
				System.out.println(c.getNativeId());
				System.out.println(c.getData());
				System.out.println("*********************************");
				
			}
		}
	}
}