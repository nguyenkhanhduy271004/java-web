package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository{
	
	static final String DB_URL = "jdbc:mysql://localhost:3306/estateadvance";
	static final String USER = "root";
	static final String PASS = "123456";

	@Override
	public List<RentAreaEntity> findAllRentAreaValueByBuildingId(Long id) {
		StringBuilder sql = new StringBuilder("SELECT * FROM rentarea r WHERE r.buildingid = " + id);
		System.out.println(sql);
		List<RentAreaEntity> result = new ArrayList<>();
		
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(sql.toString())) {
			
			while (rs.next()) {
				RentAreaEntity areaEntity = new RentAreaEntity();
				areaEntity.setBuildingId(rs.getLong("buildingid"));
				areaEntity.setValue(rs.getLong("value"));
				result.add(areaEntity);
			}
			System.out.println("Connected to the database successfully...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Database connection failed...");
		}
		
		return result;
	}

}
