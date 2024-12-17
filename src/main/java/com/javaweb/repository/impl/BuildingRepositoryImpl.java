package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.ConnectionJDBCUtil;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

	
	public static void joinTable(Map<String, Object> params, List<String> typeCode, StringBuilder join) {
	    String staffId = (String)params.get("staffId");
	    if (StringUtil.checkString(staffId) == true) {
	    	join.append(" INNER JOIN assignmentbuilding ON b.id = assignmentbuilding.buildingid ");
	    }
	    
	    if (typeCode != null && !typeCode.isEmpty()) {
	    	join.append(" INNER JOIN buildingrenttype ON b.id = buildingrenttype.buildingid ");
	    	join.append(" INNER JOIN renttype ON b.id = buildingrenttype.renttypeid ");
	    }

	}

	
	public static void queryNormal(Map<String, Object> params, List<String> typeCode, StringBuilder where) {
		for(Map.Entry<String, Object> it : params.entrySet()) {
			if(!it.getKey().equals("staffId") && !it.getKey().equals("typeCode") 
					&& !it.getKey().startsWith("area") && !it.getKey().startsWith("rentPrice")) {
				String value = it.getValue().toString();
				if(StringUtil.checkString(value) && !it.getKey().equals("rentAreaFrom") && !it.getKey().equals("rentAreaTo")) {
					if(NumberUtil.isNumber(value) && !it.getKey().equals("managerPhone")) {
						where.append(" AND b."+ it.getKey().toLowerCase() + " = "+ value); 
					} else {
						where.append(" AND b."+ it.getKey().toLowerCase() +" LIKE '%" + value + "%' ");
					}
				}
			}
		}
	}
	
	public static void querySpecial(Map<String, Object> params, List<String> typeCode, StringBuilder where) {
		String staffId = (String)params.get("staffId");
		if(StringUtil.checkString(staffId)) {
			where.append(" AND assignmentbuilding.staffid = " + staffId);
		}
		
		String rentAreaFrom = (String)params.get("rentAreaFrom");
		String rentAreaTo = (String)params.get("rentAreaTo");
		    
		if (StringUtil.checkString(rentAreaFrom) == true || StringUtil.checkString(rentAreaTo) == true) {
			where.append(" AND EXISTS (SELECT * FROM rentarea r WHERE b.id = r.buildingid");
			if (StringUtil.checkString(rentAreaFrom)) {
				where.append(" AND r.value >= " + rentAreaFrom);
			}
			if (StringUtil.checkString(rentAreaTo)) {
				where.append(" AND r.value <= " + rentAreaFrom);
			}
			where.append(") ");
		}
		
		String rentPriceFrom = (String)params.get("rentPriceFrom");
		String rentPriceTo = (String)params.get("rentPriceTo");
		    
		if (StringUtil.checkString(rentPriceFrom) == true || StringUtil.checkString(rentPriceTo) == true) {
			if (StringUtil.checkString(rentPriceFrom)) {
				where.append(" AND b.rentprice >= " + rentPriceFrom);
			}
			if (StringUtil.checkString(rentPriceTo)) {
				where.append(" AND b.rentprice <= " + rentPriceTo);
			}
		}
		
//		if (typeCode != null && typeCode.size() != 0) {
//			List<String> code = new ArrayList<>();
//			for (String item : typeCode) {
//				code.add("'" + item + "'");
//			}
//			where.append(" AND renttype.code IN(" + String.join(",", code) + ")");
//		}
		
		if (typeCode != null && typeCode.size() != 0) {
			where.append(" AND(");
			String sql = typeCode.stream().map(it -> "renttype.code LIKE" + "'%" + it + "%' ").collect(Collectors.joining(" OR "));
			where.append(sql + ") ");
		}
	}
	@Override
	public List<BuildingEntity> findAll(Map<String, Object> params, List<String> typeCode) {
		StringBuilder sql = new StringBuilder("SELECT b.id, b.name, b.street, b.ward, b.districtid, "
				+ "b.numberofbasement, b.managername, b.managerphone, b.floorarea, b.rentprice, b.servicefee, b.brokeragefee FROM building b ");
		joinTable(params, typeCode, sql);
		StringBuilder where = new StringBuilder(" WHERE 1=1");
		queryNormal(params, typeCode, where);
		querySpecial(params, typeCode, where);
		where.append(" GROUP BY b.id");
		sql.append(where);
		System.out.println(sql);
		List<BuildingEntity> result = new ArrayList<>();
		try(Connection conn = ConnectionJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString());
			) {
			
			while(rs.next()) {
				BuildingEntity buildingEntity = new BuildingEntity();
				buildingEntity.setId(rs.getLong("id"));
				buildingEntity.setNameBuilding(rs.getString("name"));
				buildingEntity.setStreet(rs.getString("street"));
				buildingEntity.setWard(rs.getString("ward"));
				buildingEntity.setDistrictId(rs.getLong("districtid"));
				buildingEntity.setNumberOfBasement(rs.getLong("numberofbasement"));
				buildingEntity.setManagerName(rs.getString("managername"));
				buildingEntity.setManagerPhone(rs.getString("managerphone"));
				buildingEntity.setFloorArea(rs.getLong("floorarea"));
				buildingEntity.setEmptySpace((long) 0);
				buildingEntity.setRentPrice(rs.getLong("rentprice"));
				buildingEntity.setServiceFee(rs.getLong("servicefee"));
				buildingEntity.setBrokerageFee(rs.getLong("brokeragefee"));					        	 
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
