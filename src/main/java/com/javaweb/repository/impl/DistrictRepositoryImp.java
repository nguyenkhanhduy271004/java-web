package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.DistrictEntity;

@Repository
public class DistrictRepositoryImp implements DistrictRepository {
	
	static final String DB_URL = "jdbc:mysql://localhost:3306/estateadvance";
	static final String USER = "root";
	static final String PASS = "123456";
	
	@Override
	public DistrictEntity findById(Long id) {
		StringBuilder sql = new StringBuilder("SELECT * FROM district d WHERE d.id = " + id);
		DistrictEntity result = null;
		
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(sql.toString())) {
			
			if (rs.next()) {
				result = new DistrictEntity();
				result.setCode(rs.getString("code"));
				result.setName(rs.getString("name"));
			} else {
				System.out.println("No district found with the given id: " + id);
			}
			
			System.out.println("Connected to the database successfully...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Database connection failed...");
		}
		
		return result;
	}
}
