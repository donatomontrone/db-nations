package org.java.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/nations";
		String user = "root";
		String password = "code";

		try (Scanner in = new Scanner(System.in); 
				Connection con = DriverManager.getConnection(url, user, password)){
		    
			String sql =" SELECT c.country_id, c.name, r.name, c2.name \n" +
					" FROM countries c \n" +
					" JOIN regions r \n" +
						" ON c.region_id = r.region_id \n" +
					" JOIN continents c2 \n" +
						" ON c2.continent_id = r.continent_id \n" +
					"WHERE c.name LIKE ? " + 
					" ORDER BY c.name;"; 
			
			System.out.print("Search: ");
			String search = in.nextLine();
			try (PreparedStatement ps = con.prepareStatement(sql)) {				
					
				ps.setString(1, "%" + search + "%");
				

				try (ResultSet rs = ps.executeQuery()){
				System.out.println("ID \tCOUNTRY \t\t\t\tREGION \t\t\tCONTINENT");
					while(rs.next()) {
						
						final int countryId = rs.getInt(1);
						final String countryName = rs.getString(2);
						final String regionName = rs.getString(3);
						final String continentName = rs.getString(4);
						System.out.println(countryId + " \t" + countryName + " \t\t\t\t" 
								+ regionName + "  \t\t" + continentName);
					}
				}
				
			} catch (SQLException e) {
				System.err.println("Errore nella composizione della query " + e.getMessage());
			}
			
			String sql2 = "SELECT c.name, l.`language` \n"
					+ "FROM languages l \n"
					+ "	JOIN country_languages cl \n"
					+ "		ON l.language_id = cl.language_id \n"
					+ "	JOIN countries c\n"
					+ "		ON c.country_id = cl.country_id \n"
					+ "WHERE c.country_id = ? ";
			
			String sql3 = "SELECT cs.`year`, cs.population, cs.gdp \n"
					+ "FROM country_stats cs\n"
					+ "WHERE cs.country_id = ? \n"
					+ "ORDER BY cs.`year` DESC \n"
					+ "LIMIT 1; ";
			
			System.out.print("\nChoose a country ID: ");
			int id = in.nextInt();
			try (PreparedStatement ps1 = con.prepareStatement(sql2)) {				
				
				ps1.setInt(1, id);
				

				try (ResultSet rs2 = ps1.executeQuery()){
					if(rs2.next()) {
						
						final String name = rs2.getString(1);
						System.out.println("Details for country: " + name);
					}
					while(rs2.next()) {
						
						final String languages = rs2.getString(2);
						System.out.print(languages + ", ");
					}
				}
				
			} catch (SQLException e) {
				System.err.println("Errore nella composizione della query " + e.getMessage());
			}
			try (PreparedStatement ps2 = con.prepareStatement(sql3)) {				
				
				ps2.setInt(1, id);
				
				try (ResultSet rs3 = ps2.executeQuery()){
					System.out.println("\nMost recent stats");
				while(rs3.next()) {
					
					final int year = rs3.getInt(1);
					final int population = rs3.getInt(2);
					final long gdp = rs3.getLong(3);
					System.out.println("Year: " + year);
					System.out.println("Population: " + population);
					System.out.println("GDP: " + gdp);
				}
			}
				
			} catch (SQLException e) {
				System.err.println("Errore nella composizione della query " + e.getMessage());
			}

		} catch (SQLException e) {
			System.err.println("Errore di connessione " + e.getMessage());
		}
	}
	
}
