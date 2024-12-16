package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

	static final String DB_URL = "jdbc:mysql://localhost:3306/estateadvance";
	static final String USER = "root";
	static final String PASS = "123456";
	
	@Override
	public List<BuildingEntity> findAll(String name,Long floorArea, 
			Long districtId, String ward, 
			String street, Long numberOfBasement, String direction, 
			String level, Long areaFrom, Long areaTo, Long rentPriceFrom, 
			Long rentPriceTo, String managerName, String managerPhone, 
			Long staffId, List<String> typeCode) {
		StringBuilder sql = new StringBuilder("SELECT * "
				+ "FROM building b "
				+ "JOIN assignmentbuilding asb ON asb.buildingid = b.id "
				+ "JOIN rentarea ra ON ra.buildingid = b.id "
				+ "WHERE 1 = 1 ");
		
		List<BuildingEntity> result = new ArrayList<>();
		if (name != null && !name.equals("")) {
			sql.append("AND b.name like '%" + name + "%' ");
		}
		if (floorArea != null) {
			sql.append("AND b.floorArea = " + floorArea + " ");
		}
		if (districtId != null) {
			sql.append("AND b.district = " + districtId + " ");
		}
		if(ward != null && !ward.equals("")) {
			sql.append("AND b.ward like '%" + ward + "%' ");
		}
		if(street != null && !street.equals("")) {
			sql.append("AND b.street like '%" + street + "%' ");
		}
		if (numberOfBasement != null) {
			sql.append("AND b.numberOfBasement = " + numberOfBasement + " ");
		}
		if(direction != null && !direction.equals("")) {
			sql.append("AND b.direction like '%" + direction + "%' ");
		}
		if(level != null && !level.equals("")) {
			sql.append("AND b.level like '%" + level + "%' ");
		}
		if(areaFrom != null) {
			sql.append("AND b.areaFrom >= " + areaFrom + " ");
		}
		if(areaTo != null) {
			sql.append("AND b.areaTo <= " + areaTo + " ");
		}
		if(rentPriceFrom != null) {
			sql.append("AND b.rentprice >= " + rentPriceFrom + " ");
		}
		if(rentPriceTo != null) {
			sql.append("AND b.rentprice <= " + rentPriceTo + " ");
		}
		if(managerName != null && !managerName.equals("")) {
			sql.append("AND b.managername like '%" + managerName + "%' ");
		}
		if(managerPhone != null && !managerPhone.equals("")) {
			sql.append("AND b.managerphone like '%" + managerPhone + "%' ");
		}
		if (staffId != null) {
			sql.append("AND asb.staffid = " + staffId + " ");
		}
		if (typeCode != null && !typeCode.isEmpty()) {
			for (String type : typeCode) {
				sql.append("OR b.type like '%" + type + "%' ");
			}
		}
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString());
			) {
			
			while(rs.next()) {
				BuildingEntity buildingEntity = new BuildingEntity();
				buildingEntity.setNameBuilding(rs.getString("name"));
				buildingEntity.setStreet(rs.getString("street"));
				buildingEntity.setWard(rs.getString("ward"));
				buildingEntity.setDistrictId(rs.getLong("district"));
				buildingEntity.setNumberOfBasement(rs.getLong("numberofbasement"));
				buildingEntity.setManagerName(rs.getString("managername"));
				buildingEntity.setManagerPhone(rs.getString("managerphone"));
				buildingEntity.setFloorArea(rs.getLong("floorarea"));
				buildingEntity.setEmptySpace((long) 0);
				buildingEntity.setRentPrice(rs.getLong("rentprice"));
				buildingEntity.setServiceFee(rs.getLong("servicefee"));
				buildingEntity.setBrokerageFee(rs.getLong("brokeragefee"));
				
	            List<String> rentAreaValues = new ArrayList<>();
	            
	            while (rs.next() && rs.getLong("b.id") == rs.getLong("ra.buildingid")) {
	                String value = rs.getString("ra.value");
	                if (value != null) {
	                    rentAreaValues.add(value);
	                }
	            }

	            StringBuilder rentAreaValueBuilder = new StringBuilder();
	            for (String value : rentAreaValues) {
	                rentAreaValueBuilder.append(value).append(",");
	            }

	            if (rentAreaValueBuilder.length() > 0) {
	                rentAreaValueBuilder.setLength(rentAreaValueBuilder.length() - 1);
	            }
	            
	            buildingEntity.setRentalArea(rentAreaValueBuilder.toString());
				result.add(buildingEntity);	
			}

			System.out.println("Connected database successfully...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connected database failed...");
		}
		return result;
	}
	
}
