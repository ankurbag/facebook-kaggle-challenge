/**
 * 
 */
package com.neu.fbchallenge.config;

/**
 * This Interface contains all the constants used throughout the application
 * @author Ankur
 *
 */
public interface MyDBConstants {
	/**
	 * Constants declaration
	 */
	//DB RELATED CONSTANTS
	String HOST_DB_URL = "localhost";
	int HOST_DB_PORT = 27017;
	String DB_NAME = "big_data";//"my_app_test_db";
	String COLLECTION_NAME = "location_coordinates";
	//APP CONSTANTS
	String SUCCESS = "success";
	String FAILURE = "failure";
	//FIELDS
	String FIELD_LOC_KEY = "loc_key";
	String FIELD_LOC_ID = "loc_id";
	String FIELD_X_COORD = "x_coord";
	String FIELD_Y_COORD = "y_coord";
	String FIELD_ROW_ID ="row_id";
	String FIELD_TIME_STAMP ="time_stamp";
	String FIELD_TIME_SLOT="time_slot";
	
	
}
