/**
 * 
 */
package com.neu.fbchallenge.classification;

import java.util.Map;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.neu.fbchallenge.config.MyDBConstants;

/**
 * It is DAO class which gives CRUD functionalities with MongoDB
 * 
 * @author Ankur
 * @version 0.1
 */
public class DAO {

	/**
	 * This method initializes the connection to MongoDB
	 * 
	 * @return DB
	 * static 
	 */
	static DB db = null;
	public static DB initConnection() {

		MongoClient mongoClient = null;
		
	if(db == null){
		try {

			// To connect to mongodb server
			
			mongoClient = new MongoClient(MyDBConstants.HOST_DB_URL, MyDBConstants.HOST_DB_PORT);

			// Now connect to your databases
			db = mongoClient.getDB(MyDBConstants.DB_NAME);
			System.out.println("Connect to database successfully");
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
		return db;
	}

	/**
	 * Creates location_coordinate to the DB.
	 * 
	 * @return
	 */
	public static String insertLocationCoordinates(Map<String, Object> documentMap) {

		String msg = MyDBConstants.SUCCESS;
		DB db = initConnection();
		DBCollection collection = db.getCollection(MyDBConstants.COLLECTION_NAME);
		
		BasicDBObject doc = new BasicDBObject(MyDBConstants.FIELD_LOC_KEY, documentMap.get(MyDBConstants.FIELD_LOC_KEY)).
		            append(MyDBConstants.FIELD_LOC_ID, documentMap.get(MyDBConstants.FIELD_LOC_ID)).
		            append(MyDBConstants.FIELD_X_COORD, documentMap.get(MyDBConstants.FIELD_X_COORD)).
		            append(MyDBConstants.FIELD_Y_COORD,documentMap.get(MyDBConstants.FIELD_Y_COORD)).
		            append(MyDBConstants.FIELD_ROW_ID, documentMap.get(MyDBConstants.FIELD_ROW_ID)).
		            append(MyDBConstants.FIELD_TIME_STAMP,  documentMap.get(MyDBConstants.FIELD_TIME_STAMP)).
		            append(MyDBConstants.FIELD_TIME_SLOT,  documentMap.get(MyDBConstants.FIELD_TIME_SLOT));
		WriteResult wr = collection.insert(doc);
		System.out.println("wr :"+wr.getN());
		if(wr.getN()>0) msg = MyDBConstants.FAILURE;
		return msg;
	}

}
