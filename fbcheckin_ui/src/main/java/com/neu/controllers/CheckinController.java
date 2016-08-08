package com.neu.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.neu.config.MyDBConstants;
import com.neu.dao.DAO;

/**
 * Servlet implementation class CheckinController which fetches data from MongoDb
 */
public class CheckinController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckinController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONArray coordinatesJsonList = new JSONArray();
		int x_coord = Integer.parseInt(request.getParameter("xcoord"));
		int y_coord = Integer.parseInt(request.getParameter("ycoord"));
		
		RequestDispatcher rd = request.getRequestDispatcher("/checkin.jsp?x="+x_coord+"&y="+y_coord);
		rd.forward(request, response);
	}

	public static int findClosest(int targetVal, int[] set) {
		int dif = 100, cand = 0;
		for (int x : set)
			if (Math.abs(x - targetVal) < dif) {
				dif = Math.abs(x - targetVal);
				cand = x;
			}
		return cand;
	}

	class MyComparator implements Comparator {
		Map map;

		public MyComparator(Map map) {
			this.map = map;
		}

		public int compare(Object o1, Object o2) {
			return ((Integer) map.get(o2)).compareTo((Integer) map.get(o1));
		}
	}

	public int[] getArray(Set<String> set, int index) {
		int[] timeslots = new int[set.size()];
		for (String s : set) {
			timeslots[index] = Integer.parseInt(s);
			index++;
		}
		return timeslots;
	}

	public int getHour() {
		Date date = new Date(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = 0;
		if (date != null) {
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			System.out.println(hour);
		}
		return hour;
	}

	public Map<String, Integer> performKNN(int x_coord, int y_coord) {
		DAO dao = new DAO();
		int index = 0;
		// 1.Unique Time Slots
		Set<String> set = dao.getUniqueTimeSlots();
		int[] timeslots = getArray(set, index);

		// 2.Get Nearest TimeSlot
		int hour = getHour();
		ArrayList<String> timeslotslist = dao
				.getTimeSlots(findClosest(hour, timeslots) + "_" + x_coord + "_" + y_coord);

		// 3.Sort in Descending order
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();

		for (String temp : timeslotslist) {
			Integer count = map.get(temp);
			map.put(temp, (count == null) ? 1 : count + 1);
		}
		MyComparator comp = new MyComparator(map);
		Map<String, Integer> newMap = new TreeMap(comp);
		newMap.putAll(map);
		System.out.println("Ranked List :" + newMap);
		return newMap;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		/// doGet(request, response);
		/////////////////
		System.out.println("Call from AJAX");
		// getUnique TimeStamp from DB
		int x_coord = Integer.parseInt(request.getParameter("xcoord"));
		int y_coord = Integer.parseInt(request.getParameter("ycoord"));
		Map ordermap = new LinkedHashMap();
		String[] comments = new String[10];
		comments[0] = "Right near the XYZ area and there are plenty of investors to come in which makes for a fantastic view";
		comments[1] = "Its generally very busy on the weekends so wait on the sidewalk and relive last nights debauchery or call ahead and look like youre not one paycheck away from losing your Jetta as you stroll out with your order. ";
		comments[2] = "It's small, but, being small, trust me, small can sometimes bark the loudest, and business of breakfast sandwiches, Mike & Patty's is waking up the neighborhood.";
		comments[3] = "This is probably the BEST place to setup sandwich bar.";
		comments[4] = "The space seems much older with fame stretching far and wide, pulling up to 12 Church Street may at first seem completely underwhelming";
		comments[5] = "You will to realise easily that your holiday is too short to enjoy enough this marvelleous city from this apartment !!!";
		comments[6] = "We stayed at this wonderful apartment for 9 days and felt very much at home. We were 2 families with teenagers and loved every minute of our trip. ";
		comments[7] = "Good location ,lots of restaurant nearby and ideal for shopping ,near the motorway too much terafic and difficult to commute at certain time of the day .";
		comments[8] = "In the heart of the city and closed to everywhere,Bakirkoy seaside district,( European side)";
		comments[9] = "Awesome";
		
		int n = 5;
		JSONArray coordinatesJsonList = new JSONArray();
		Random rand = new Random();
		Map <String,Integer> map = performKNN(x_coord, y_coord);
		if (map != null ) {
			Iterator entries = map.entrySet().iterator();
			if (entries.hasNext()) {
			  Entry thisEntry = (Entry) entries.next();
			  String key =(String) thisEntry.getKey();
			  Integer count =(Integer) thisEntry.getValue();
			  JSONObject coordinatesJsonObject = new JSONObject(ordermap);
				try {
					coordinatesJsonObject.put(MyDBConstants.FIELD_LOC_ID, key);
					coordinatesJsonObject.put(MyDBConstants.FIELD_X_COORD, x_coord);//Doubt
					coordinatesJsonObject.put(MyDBConstants.FIELD_Y_COORD, y_coord);
					coordinatesJsonObject.put("review", comments[(rand.nextInt((10 - 0) + 1) + 0)]);
					coordinatesJsonList.put(coordinatesJsonObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			response.getWriter().println(coordinatesJsonList);
			
		}
	}

}
