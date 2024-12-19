package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.utils.ConnectionJDBCUtil;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository {

	@Override
	public List<RentAreaEntity> findAllRentAreaValueByBuildingId(Long id) {
		StringBuilder sql = new StringBuilder("SELECT * FROM rentarea r WHERE r.buildingid = " + id);
		List<RentAreaEntity> result = new ArrayList<>();

		try (Connection conn = ConnectionJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString())) {

			while (rs.next()) {
				RentAreaEntity areaEntity = new RentAreaEntity();
//				areaEntity.setBuildingId(rs.getLong("buildingid"));
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
