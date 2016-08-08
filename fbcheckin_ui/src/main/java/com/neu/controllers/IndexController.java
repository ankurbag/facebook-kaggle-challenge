package com.neu.controllers;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class IndexController
 */
public class IndexController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IndexController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("/home.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		/////////////////
		System.out.println("Call from AJAX");
		int n = 5;
		Random rand = new Random();
		JSONArray coordinatesJsonList = new JSONArray();

		try {
			for (int i = 0; i < n; i++) {
				Map ordermap = new LinkedHashMap();
				JSONObject coordinatesJsonObject = new JSONObject(ordermap);
				coordinatesJsonObject.put("loc_key", "0_0");
				coordinatesJsonObject.put("loc_id", "8328914443" + (rand.nextInt((10 - 0) + 1) + 0));
				coordinatesJsonObject.put("x_coord", (int) ((Math.random() * 90) + 10) / 10.0);
				coordinatesJsonObject.put("y_coord", (int) ((Math.random() * 90) + 10) / 10.0);
				coordinatesJsonList.put(coordinatesJsonObject);
			}
			
			response.getWriter().println(coordinatesJsonList);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
