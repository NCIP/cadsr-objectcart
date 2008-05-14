package gov.nih.nci.objectCart.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper singleton class which will be initialized with a map of ObjectCartClients.
 * The keys in the map correspond to the classification schemes associated with the 
 * ObjectCartClient mapped to that key. 
 * 
 * @author Denis Avdic
 */
public class ClientManager {

	private Map<String,ObjectCartClient> clients;
	
	// Private constructor suppresses generation of a (public) default constructor
	private ClientManager() {}
	
	/**
	 * SingletonHolder is loaded on the first execution of CartManager.getInstance() 
	 * or the first access to SingletonHolder.instance , not before.
	 */
	private static class SingletonHolder { 
		private final static ClientManager instance = new ClientManager();
	}

	public static ClientManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Takes an array of classificationScheme names and creates a map of 
	 * ObjectCartClients corresponding to those classificationScheme names. 
	 * It then stores the map in the private member fixing it within the singleton.
	 * 
	 * @param classificationSchemes
	 * @throws ObjectCartException
	 */
	public void initClients(String[] classificationSchemes) throws ObjectCartException {
		Map<String, ObjectCartClient> temp = new HashMap<String, ObjectCartClient>();
		if (clients == null) {
			for (String cScheme: classificationSchemes){
				temp.put(cScheme, new ObjectCartClient(cScheme));
			}
			clients = temp;
		} else throw new ObjectCartException("Initializing manager more than once");
	}

	/**
	 * Takes a classificationScheme name and uses the String to access the 
	 * underlying HashMap containing ObjectCartClients.  It returns the ObjectCartClient
	 * 
	 * @param classificationSchemes
	 * @throws ObjectCartException
	 */
	public ObjectCartClient getClient(String classificationScheme) {
		return clients.get(classificationScheme);
	}
}