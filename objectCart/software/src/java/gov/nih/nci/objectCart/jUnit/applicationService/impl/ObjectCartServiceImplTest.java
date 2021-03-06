/*L
 * Copyright Ekagra Software Technologies Ltd, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-objectcart/LICENSE.txt for details.
 */

package gov.nih.nci.objectCart.jUnit.applicationService.impl;


import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;
import gov.nih.nci.objectCart.applicationService.ObjectCartService;
import gov.nih.nci.objectCart.domain.Cart;
import gov.nih.nci.objectCart.domain.CartObject;
import gov.nih.nci.objectCart.util.ValidatorException;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ObjectCartServiceImplTest extends TestCase{

	public List<Cart> cartList = null;
    public ObjectCartService ocs = null;
    public ApplicationService as = null;
    public String setupTime = null;
 	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		as = ApplicationServiceProvider.getApplicationService("objectCartServiceInfo");
		ocs = (ObjectCartService) as;
		setupTime = (new Date(System.currentTimeMillis())).toString();
	
	}

	@After
	public void tearDown() throws Exception {	
	}

	@Test
	public void testCreateAndExpireCartUC(){
		
		String userId = (new Date(System.currentTimeMillis())).toString();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
	        
		//Check attribute values were correctly saved.
		assertEquals(name, first.getName());
		assertEquals(userId, first.getUserId());
		assertNotNull(first.getId());
		//assertNotNull(first.getExpirationDate());
		assertNotNull(first.getCreationDate());
		
		expireCart(first.getId());
		
		//Trying to create cart with same name and userId.  If it exists(not expired) it should return 
		//cart with same id as before.  Otherwise create new cart.
		Cart second = createCart(userId, name);
		
		assertEquals(second.getName(), first.getName());
		assertEquals(second.getUserId(), first.getUserId());
		assertTrue(second.getId().intValue() != first.getId().intValue());
		
		
		//Cleanup
		
		expireCart(second.getId());
	}
	
	public void testCreateAndDoNotExpireCartUC(){
		
		String userId = (new Date(System.currentTimeMillis())).toString();
	    String name = "First Cart name: " + userId;
	    Cart first = createCart(userId, name);
	        
		//Check attribute values were correctly saved.
		assertEquals(name, first.getName());
		assertEquals(userId, first.getUserId());
		assertNotNull(first.getId());
		//assertNotNull(first.getExpirationDate());
		assertNotNull(first.getCreationDate());
		
		//Trying to create cart with same name and userId.  If it exists(not expired) it should return 
		//cart with same id as before.  Otherwise create new cart.
		Cart second = createCart(userId, name);

		assertEquals(second.getName(), first.getName());
		assertEquals(second.getUserId(), first.getUserId());
		assertEquals(second.getId(), first.getId());
		//assertNotNull(first.getExpirationDate());
		assertNotNull(first.getCreationDate());
		
		//Cleanup
		expireCart(first.getId());

	}
	
	@Test
	public void testCreateTwoCarts() {
		
		String userId = (new Date(System.currentTimeMillis())).toString();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
		
	    name = "Second Cart name: " + (new Date(System.currentTimeMillis())).toString();
		Cart second = createCart(first.getUserId(), name);
	
		//Check that we didn't get back first
		assertTrue(!second.equals(first));
		
		//Check second cart is what it's supposed to be.		
		//Check attribute values were correctly saved.
		assertEquals(name, second.getName());
		assertEquals(userId, second.getUserId());
		assertNotNull(second.getId());
		//assertNotNull(second.getExpirationDate());
		assertNotNull(second.getCreationDate());
		
		
		//Cleanup
		expireCart(first.getId());
		expireCart(second.getId());
	}
	
	@Test
	public void testGetCartsforUser() {
			
		String userId = (new Date(System.currentTimeMillis())).toString();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
		
	    name = "Second Cart name: " + (new Date(System.currentTimeMillis())).toString();
		Cart second = createCart(first.getUserId(), name);
		
		//We should have two carts for the user
		retrieveCartsForUser(userId);
		
		//Check Cart List exists
		assertNotNull(cartList);
		assertTrue(cartList.size() == 2);
		assertTrue(cartList.get(0) instanceof Cart);
		assertTrue(cartList.get(1) instanceof Cart);
		
		//Make sure we don't get same carts in both places
		assertTrue(!((Cart)cartList.get(0)).equals((Cart)cartList.get(1)));
		
		//Cleanup
		expireCart(first.getId());
		expireCart(second.getId());
	
	}
	
	public void testAddObject() {
		
		String userId = "Test Add Object " +System.currentTimeMillis();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
		String testData = "This is teh sserialized data of the object";
		String testType = ":Test:CDE Cart Test";
	    CartObject testObject = new CartObject();
	    testObject.setType(testType);
	    testObject.setData(testData);
	    
		assertNull(first.getCartObjectCollection());
		
		addObject(first.getId(), testObject);
		
		Cart second = getCart(first.getId());
		
		assertNotNull(second);
		assertNotNull(second.getCartObjectCollection());
		assertTrue(!second.getCartObjectCollection().isEmpty());
		assertTrue(second.getCartObjectCollection().size() == 1);
		
		Collection<CartObject> col = new HashSet<CartObject>();
		col =  second.getCartObjectCollection();
		Object[] co =  col.toArray();
		CartObject retrievedObject = (CartObject) co[0]; 
			
		assertEquals(retrievedObject.getData(),testData);
		assertEquals(retrievedObject.getType(), testType);
		
		//Cleanup
		expireCart(first.getId());
		
	}

	public void testRemoveObject() {
		
		String userId = "Test Remove Object" +System.currentTimeMillis();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
	    String testData = "This is teh sserialized data of the object";
		String testType = ":Test:CDE Cart Test";
	    CartObject testObject = new CartObject();
	    testObject.setType(testType);
	    testObject.setData(testData);
	    
	    CartObject testObject2 = new CartObject();
	    testObject2.setType(testType);
	    testObject2.setData(testData+testType);
	   
	    assertNull(first.getCartObjectCollection());
	    
		Collection<CartObject> testCol = new HashSet<CartObject>();
		
		testCol.add(testObject);
		testCol.add(testObject2);
		
		addObjects(first.getId(), testCol);
	
		Cart second = getCart(first.getId());
		
		assertNotNull(second);
		assertNotNull(second.getCartObjectCollection());
		assertTrue(!second.getCartObjectCollection().isEmpty());
		assertTrue(second.getCartObjectCollection().size() == 2);
	
		Integer[] arr = new Integer[second.getCartObjectCollection().size()];

		int cntr = 0;
		for (CartObject c: second.getCartObjectCollection()){
			arr[cntr++] = c.getId();
		}
		Integer removedId = arr[0];
		Integer remainingId = arr[arr.length-1];
		
		System.out.println("Removing:"+removedId);
		System.out.println(remainingId);
		
		removeObject(second.getId(), arr[0]);
	
		Cart third = getCart(second.getId());
		
		assertNotNull(third);
		assertNotNull(third.getCartObjectCollection());
		assertTrue(!third.getCartObjectCollection().isEmpty());
		assertTrue(third.getCartObjectCollection().size() == 1);

		CartObject retrievedObject = third.getCartObjectCollection().iterator().next(); 
		
		assertEquals(remainingId, retrievedObject.getId());
		assertTrue(retrievedObject.getId().intValue() != removedId.intValue());
		
		//Cleanup
		expireCart(first.getId());
		
	}
	
	public void testRemoveObjects() {
		
		String userId = "Test Remove Objects" +System.currentTimeMillis();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
	    String testData = "This is teh sserialized data of the object";
		String testType = ":Test:CDE Cart Test";
	    CartObject testObject = new CartObject();
	    testObject.setType(testType);
	    testObject.setData(testData);
	    
	    CartObject testObject2 = new CartObject();
	    testObject2.setType(testType);
	    testObject2.setData(testData+testType);
	   
	    CartObject testObject3 = new CartObject();
	    testObject3.setType(testType);
	    testObject3.setData(testData+testType);
	    
	    assertNull(first.getCartObjectCollection());
	    
		Collection<CartObject> testCol = new HashSet<CartObject>();
		
		testCol.add(testObject);
		testCol.add(testObject2);
		testCol.add(testObject3);
		
		addObjects(first.getId(), testCol);
	
		Cart second = getCart(first.getId());
		
		assertNotNull(second);
		assertNotNull(second.getCartObjectCollection());
		assertTrue(!second.getCartObjectCollection().isEmpty());
		assertTrue(second.getCartObjectCollection().size() == 3);
	
		Integer[] arr = new Integer[second.getCartObjectCollection().size()];

		int cntr = 0;
		for (CartObject c: second.getCartObjectCollection()){
			arr[cntr++] = c.getId();
			System.out.println(arr[cntr-1]);
		}
		
		removeObjects(second.getId(), arr);
		
		Cart third = getCart(second.getId());	
		
		assertNotNull(third);
		assertNotNull(third.getCartObjectCollection());
		assertTrue(third.getCartObjectCollection().isEmpty());		
		
		//Cleanup
		expireCart(first.getId());
		
	
	}
	
	public void testGetObjects() {

		String userId = "Test Remove Objects" +System.currentTimeMillis();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
	    String testData = "This is teh sserialized data of the object";
		String testType = ":Test:CDE Cart Test";
	    CartObject testObject = new CartObject();
	    testObject.setType(testType);
	    testObject.setData(testData);
		   
	    CartObject testObject2 = new CartObject();
	    testObject2.setType(testType);
	    testObject2.setData(testData+testType);
	   
	    assertNull(first.getCartObjectCollection());
	    
		Collection<CartObject> testCol = new HashSet<CartObject>();
		
		testCol.add(testObject);
		testCol.add(testObject2);
		
		addObjects(first.getId(), testCol);
		
		Collection<CartObject> col = getObjects(first.getId());
		
		for(CartObject c: col){
			System.out.println(c.getId());
		}
		
		//Cleanup
		expireCart(first.getId());
	}
	
	/*public void testClassification(){
		
		String userId = (new Date(System.currentTimeMillis())).toString();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
	        
		//Check attribute values were correctly saved.
		assertEquals(name, first.getName());
		assertEquals(userId, first.getUserId());
		assertNotNull(first.getId());
		//assertNotNull(first.getExpirationDate());
		assertNotNull(first.getCreationDate());
		
		
		//Trying to create cart with same name and userId but different classification.  It should return 
		//cart with different id than before.
		Cart second = createAnotherClassificationCart(userId, name);
		
		assertEquals(second.getName(), first.getName());
		assertEquals(second.getUserId(), first.getUserId());
		assertTrue(second.getId().intValue() != first.getId().intValue());
		ObjectCartClientTest.printCart(first);
		ObjectCartClientTest.printCart(second);
		
		List<Cart> firstList = getCartsByClassification(first.getType());
		List<Cart> secondList = getCartsByClassification(second.getType());
				
		assertTrue(cartContains(firstList, first));
		assertTrue(cartContains(secondList, second));
	}*/
	
	public void testAssociateCart() {

		String userId = "Test Associate Cart" +System.currentTimeMillis();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
	    
	    String userId2 = "Test Associate Cart TWO" +System.currentTimeMillis();
	    String name2 = "Second Cart name: " +userId;
	    Cart second = createCart(userId2, name2);
	    
	    String testData = "This is teh sserialized data of the object";
		String testType = ":Test:CDE Cart Test";
	    CartObject testObject = new CartObject();
	    testObject.setType(testType);
	    testObject.setData(testData);
		   
	    CartObject testObject2 = new CartObject();
	    testObject2.setType(testType);
	    testObject2.setData(testData+testType);
	   
	    
	    CartObject testObject3 = new CartObject();
	    testObject3.setType(testType);
	    testObject3.setData(testData+testType+testType);
	   
	    assertNull(first.getCartObjectCollection());
	    
		Collection<CartObject> testCol = new HashSet<CartObject>();
		
		testCol.add(testObject);
		testCol.add(testObject2);
		
		addObjects(first.getId(), testCol);
		addObject(second.getId(), testObject3);
		
		associateCart(second.getUserId(), first.getUserId(), first.getName());
		
		second = getCart(second.getId());
		first = getCart(first.getId());
		
		assertEquals(first.getUserId(), userId2);
		assertEquals(first.getName(), name);
		
		assertTrue(first.getCartObjectCollection().contains(testObject));
		
		//Cleanup
		expireCart(first.getId());
		expireCart(second.getId());
		
	}
	
	public void testAssociateCartSameName() {

		String userId = "Test Associate Cart" +System.currentTimeMillis();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
	    
	    String userId2 = "Test Associate Cart TWO" +System.currentTimeMillis();
	    
	    Cart second = createCart(userId2, name);
	    
	    String testData = "This is teh sserialized data of the object";
		String testType = ":Test:CDE Cart Test";
	    CartObject testObject = new CartObject();
	    testObject.setType(testType);
	    testObject.setData(testData);
		   
	    CartObject testObject2 = new CartObject();
	    testObject2.setType(testType);
	    testObject2.setData(testData+testType);
	   
	    
	    CartObject testObject3 = new CartObject();
	    testObject3.setType(testType);
	    testObject3.setData(testData+testType+testType);
	   
	    assertNull(first.getCartObjectCollection());
	    
		Collection<CartObject> testCol = new HashSet<CartObject>();
		
		testCol.add(testObject);
		testCol.add(testObject2);
		
		addObjects(first.getId(), testCol);
		addObject(second.getId(), testObject3);
		
		associateCart(second.getUserId(), first.getUserId(), first.getName());
				
		second = getCart(second.getId());
		
		try {
			//This should throw exception
			first = ocs.getCart(first.getId());
		}catch (Exception e) {
			e.printStackTrace();
			assertTrue(true);
		}
		
		assertEquals(second.getUserId(), userId2);
		assertEquals(second.getName(), name);
		
		assertTrue(second.getCartObjectCollection().contains(testObject));
		
		//Cleanup
		
		expireCart(second.getId());
		
	}
	
	public void testGetObjectsByType() {

		String userId = "Test Remove Objects" +System.currentTimeMillis();
	    String name = "First Cart name: " +userId;
	    Cart first = createCart(userId, name);
	    String testData = "This is teh sserialized data of the object";
		String testType = ":Test:CDE Cart Test";
	    CartObject testObject = new CartObject();
	    testObject.setType(testType);
	    testObject.setData(testData);
		   
	    CartObject testObject2 = new CartObject();
	    testObject2.setType(testType+"z");
	    testObject2.setData(testData+testType);
	   
	    assertNull(first.getCartObjectCollection());
	    
		Collection<CartObject> testCol = new HashSet<CartObject>();
		
		testCol.add(testObject);
		testCol.add(testObject2);
		
		addObjects(first.getId(), testCol);
		
		Collection<CartObject> col = getObjects(first.getId(), testType);
		
		for(CartObject c: col){
			assertEquals(c.getType(), testType);
			assertEquals(c.getData(), testData);
		}
		
		//Cleanup
		expireCart(first.getId());
	}
	
	private boolean cartContains(List<Cart> cList, Cart cart) {
		
		for (Cart c: cList) {
			if (c.getId().equals(cart.getId())) 
				return true;
		}
		return false;	
	}
	
	
	private Cart createCart(String userId, String cname) {
		
		try {
			return ocs.getNewCart(userId, cname);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Create Cart" +ae.getMessage());
			return null;
		} 
	}
	
	private Cart createAnotherClassificationCart(String userId, String cname) {
		
		try {
			return ocs.getNewCart(userId, cname);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Create Cart" +ae.getMessage());
			return null;
		} 
	}
	
	/*private List<Cart> getCartsByClassification(String classificationType) {
		
		try {
			return ocs.getClassificationSchemeCarts(classificationType);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("get Carts by classification type" +ae.getMessage());
			return null;
		} 
	}*/
	
	private void expireCart(Integer cartId) {
		
		try {
			 ocs.expireCart(cartId);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Expire User Cart" +ae.getMessage());
		} 
	}
	
	private Cart getCart(Integer cartId) {
		
		try {
			 return ocs.getCart(cartId);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Get Existing Cart" +ae.getMessage());
		} 
		return null;
	}
	
	private void addObject(Integer cartId, CartObject cartObject) {
		
		try {
			 ocs.addObject(cartId, cartObject);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Add Object" +ae.getMessage());
		}  catch (ValidatorException ae) {
			ae.printStackTrace();
			fail("Error During Validation" +ae.getMessage());
		} 
	
	}
	 
	private void associateCart(String newUserId, String oldUserId, String cartName) {
		
		try {
			 ocs.associateCart(newUserId, oldUserId, cartName);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Associate Cart" +ae.getMessage());
		}
	}
	
	private void removeObject(Integer cartId, Integer objectId) {
		
		try {
			 ocs.removeObject(cartId, objectId);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Expire User Cart" +ae.getMessage());
		} 
	
	}
	
	private void removeObjects(Integer cartId, Integer[] objectIds) {
		
		try {
			 ocs.removeObjects(cartId, objectIds);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Expire User Cart" +ae.getMessage());
		} 
	
	}
	
	private void addObjects(Integer cartId, Collection<CartObject> cartObjects) {
		
		try {
			 ocs.addObjects(cartId, cartObjects);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Expire User Cart" +ae.getMessage());
		}  catch (ValidatorException ae) {
			ae.printStackTrace();
			fail("Error During Validation" +ae.getMessage());
		} 
	
	}
	
	private void retrieveCartsForUser(String userId) {
		
		try {
			 cartList = ocs.getUserCarts(userId);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Get User Carts" +ae.getMessage());
		} 
	}
	
	private Collection<CartObject> getObjects(Integer cartId) {
		
		try {
			 return ocs.retrieveObjects(cartId);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Get Objects:" +ae.getMessage());
		} 
		return null;
	}
	
	private Collection<CartObject> getObjects(Integer cartId, String type) {
		
		try {
			 return ocs.retrieveObjectsByType(cartId, type);
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			fail("Get Objects By Type:" +ae.getMessage());
		} 
		return null;
	}
}
