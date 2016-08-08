/**
 * 
 */
package com.neu.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.neu.config.MyDBConstants;

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
	 */
	static DB db = null;

	public static DB initConnection() {

		MongoClient mongoClient = null;
		if (db == null) {
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

		String msg = MyDBConstants.FAILURE;
		DB db = initConnection();
		DBCollection collection = db.getCollection(MyDBConstants.COLLECTION_NAME);

		BasicDBObject doc = new BasicDBObject(MyDBConstants.FIELD_LOC_KEY, documentMap.get(MyDBConstants.FIELD_LOC_KEY))
				.append(MyDBConstants.FIELD_LOC_ID, documentMap.get(MyDBConstants.FIELD_LOC_ID))
				.append(MyDBConstants.FIELD_X_COORD, documentMap.get(MyDBConstants.FIELD_X_COORD))
				.append(MyDBConstants.FIELD_Y_COORD, documentMap.get(MyDBConstants.FIELD_Y_COORD))
				.append(MyDBConstants.FIELD_ROW_ID, documentMap.get(MyDBConstants.FIELD_ROW_ID))
				.append(MyDBConstants.FIELD_TIME_STAMP, documentMap.get(MyDBConstants.FIELD_TIME_STAMP))
				.append(MyDBConstants.FIELD_TIME_SLOT, documentMap.get(MyDBConstants.FIELD_TIME_SLOT));
		WriteResult wr = collection.insert(doc);
		System.out.println("wr :" + wr.getN());
		if (wr.getN() > 0)
			msg = MyDBConstants.SUCCESS;
		return msg;
	}

	/**
	 * Fetches unique timeslots in the database.
	 */
	public static Set<String> getUniqueTimeSlots() {
		Set timeslots = new HashSet<String>();
		String msg = "";
		DB db = initConnection();
		DBCollection collection = db.getCollection(MyDBConstants.COLLECTION_NAME);
		BasicDBObject allQuery = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject();
		fields.put(MyDBConstants.FIELD_TIME_SLOT, 1);
		fields.put("_id", 0);
		DBCursor cursorDocMap = collection.find(allQuery, fields);

		while (cursorDocMap.hasNext()) {
			// System.out.println(cursorDocMap.next());
			Map<String, Object> rs = cursorDocMap.next().toMap();
			timeslots.add(rs.get(MyDBConstants.FIELD_TIME_SLOT));
		}
		return timeslots;
	}

	/**
	 * Fetches timeslots in the database.
	 */
	public static Map<String, Object> getData(String locKey) {
		DB db = initConnection();
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		DBCollection collection = db.getCollection(MyDBConstants.COLLECTION_NAME);
		BasicDBObject allQuery = new BasicDBObject();
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put(MyDBConstants.FIELD_LOC_KEY, locKey);
		DBCursor cursorDocMap = collection.find(whereQuery);

		while (cursorDocMap.hasNext()) {
			// System.out.println(cursorDocMap.next());
			Map<String, Object> rs = cursorDocMap.next().toMap();
			resultMap.put(MyDBConstants.FIELD_LOC_KEY, rs.get(MyDBConstants.FIELD_LOC_KEY));
			resultMap.put(MyDBConstants.FIELD_LOC_ID, rs.get(MyDBConstants.FIELD_LOC_ID));
			resultMap.put(MyDBConstants.FIELD_X_COORD, rs.get(MyDBConstants.FIELD_X_COORD));
			resultMap.put(MyDBConstants.FIELD_Y_COORD, rs.get(MyDBConstants.FIELD_Y_COORD));
			resultMap.put(MyDBConstants.FIELD_ROW_ID, rs.get(MyDBConstants.FIELD_ROW_ID));
			resultMap.put(MyDBConstants.FIELD_TIME_STAMP, rs.get(MyDBConstants.FIELD_TIME_STAMP));
			resultMap.put(MyDBConstants.FIELD_TIME_SLOT, rs.get(MyDBConstants.FIELD_TIME_SLOT));
		}
		return resultMap;
	}
	/**
	 * Fetches timeslots in the database.
	 */
	public static ArrayList<String> getTimeSlots(String locKey) {
		ArrayList timeSlots = new ArrayList<String>();
		String msg = "";
		// int cnt
		DB db = initConnection();
		DBCollection collection = db.getCollection(MyDBConstants.COLLECTION_NAME);
		BasicDBObject allQuery = new BasicDBObject();
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put(MyDBConstants.FIELD_LOC_KEY, locKey);
		DBCursor cursorDocMap = collection.find(whereQuery);

		while (cursorDocMap.hasNext()) {
			// System.out.println(cursorDocMap.next());
			Map<String, Object> rs = cursorDocMap.next().toMap();
			timeSlots.add(rs.get(MyDBConstants.FIELD_LOC_ID));
		}
		return timeSlots;
	}
	// Takes a double and returns its squared value.
	private double squaredDistance(double n1) {
		return Math.pow(n1, 2);
	}

	private double totalSquaredDistance(double R1, double R2, double S1, double S2) {
		double xCoordDifference = S1 - R1;
		double yCoordDifference = S2 - R2;

		// The sum of squared distances is used rather than the euclidean
		// distance
		// because taking the square root would not change the order.
		// Status and gender are not squared because they are always 0 or 1.
		return squaredDistance(xCoordDifference) + squaredDistance(yCoordDifference);
	}

	private double normalisedDouble(String n1, double minValue, double maxValue) {
		return (Double.parseDouble(n1) - minValue) / (maxValue - minValue);
	}

}
