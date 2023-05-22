package org.java.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/nations";
		String user = "root";
		String password = "code";
		
		try (Connection con = DriverManager.getConnection(url, user, password)){
		    
			String sql =" SELECT c.country_id, c.name, r.name, c2.name \n" +
					" FROM countries c \n" +
					" JOIN regions r \n" +
						" ON c.region_id = r.region_id \n" +
					" JOIN continents c2 \n" +
						" ON c2.continent_id = r.continent_id \n" +
					" ORDER BY c.name;";
			
			try (PreparedStatement ps = con.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {				
					
				System.out.println("ID\t| Country Name | Region Name | Continent Name");
					while(rs.next()) {
						
						final int countryId = rs.getInt(1);
						final String countryName = rs.getString(2);
						final String regionName = rs.getString(3);
						final String continentName = rs.getString(4);
						
						System.out.println(countryId + "\t| " + countryName + " | " 
								+ regionName + " | " + continentName);
				}				
			} catch (SQLException e) {
				System.err.println("Errore nella composizione della query " + e.getMessage());
			}
		} catch (SQLException e) {
			System.err.println("Errore di connessione " + e.getMessage());
		}
	}
	
}
