package com.otsi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONValue;

/**
 * Servlet implementation class DashBoardGeneration
 */
public class DashBoardGeneration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DashBoardGeneration() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw=response.getWriter();
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3308/chandu", "root", "root");

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("select dept,sum(sal) as value from emp group by dept");

			String s = getJSONFromResultSet(rs);
			pw.println(s);
			System.out.println(s);
			request.setAttribute("name", s);
			request.getRequestDispatcher("page.jsp").forward(request, response);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	@SuppressWarnings("unchecked")
	public static String getJSONFromResultSet(ResultSet rs) {
		// Map json = new HashMap();
		@SuppressWarnings("rawtypes")
		List list = new ArrayList();
		if (rs != null) {
			try {
				ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					Map<String, Object> columnMap = new HashMap<String, Object>();
					for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
						if (rs.getString(metaData.getColumnName(columnIndex)) != null)
							columnMap.put(metaData.getColumnLabel(columnIndex),
									rs.getString(metaData.getColumnName(columnIndex)));
						else
							columnMap.put(metaData.getColumnLabel(columnIndex), "");
					}
					list.add(columnMap);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// json.put(keyName, list);
		}
		return JSONValue.toJSONString(list);
	}
}
