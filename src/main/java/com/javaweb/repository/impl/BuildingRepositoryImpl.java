package com.javaweb.repository.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.ConnectionJDBCUtil;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

	
	public static void joinTable(BuildingSearchBuilder buildingSearchBuilder, StringBuilder join) {
	    Long staffId = buildingSearchBuilder.getStaffId();
	    if (staffId != null) {
	    	join.append(" INNER JOIN assignmentbuilding ON b.id = assignmentbuilding.buildingid ");
	    }
	    List<String> typeCode = buildingSearchBuilder.getTypeCode();
	    if (typeCode != null && !typeCode.isEmpty()) {
	    	join.append(" INNER JOIN buildingrenttype ON b.id = buildingrenttype.buildingid ");
	    	join.append(" INNER JOIN renttype ON b.id = buildingrenttype.renttypeid ");
	    }

	}

	
	public static void queryNormal (BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
		try {
			Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
			for (Field item : fields) {
				item.setAccessible(true);
				String fieldName = item.getName();
				if(!fieldName.equals("staffId") && !fieldName.equals("typeCode") 
						&& !fieldName.startsWith("area") && !fieldName.startsWith("rentPrice"))  {
					Object value = item.get(buildingSearchBuilder);
					if(value != null) {
						if((item.getType().getName().equals("java.lang.Long") || item.getType().getName().equals("java.lang.Integer")) && !fieldName.equals("managerPhone")) {
							where.append(" AND b."+ fieldName.toLowerCase() + " = "+ value); 
						} else {
							where.append(" AND b."+ fieldName.toLowerCase() +" LIKE '%" + value + "%' ");
						}
					}
				}
			}
 		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void querySpecial(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
	    Long staffId = buildingSearchBuilder.getStaffId();
		if(staffId != null) {
			where.append(" AND assignmentbuilding.staffid = " + staffId);
		}
		
		Object rentAreaFrom = buildingSearchBuilder.getAreaFrom();
		Object rentAreaTo = buildingSearchBuilder.getAreaTo();
		    
		if (rentAreaFrom != null || rentAreaTo != null) {
			where.append(" AND EXISTS (SELECT * FROM rentarea r WHERE b.id = r.buildingid");
			if (rentAreaFrom != null) {
				where.append(" AND r.value >= " + rentAreaFrom);
			}
			if (rentAreaTo != null) {
				where.append(" AND r.value <= " + rentAreaFrom);
			}
			where.append(") ");
		}
		
		Object rentPriceFrom = buildingSearchBuilder.getRentPriceFrom();
		Object rentPriceTo = buildingSearchBuilder.getRentPriceTo();
		    
		if (rentAreaFrom != null || rentAreaTo != null) {
			if (rentAreaFrom != null) {
				where.append(" AND b.rentprice >= " + rentPriceFrom);
			}
			if (rentAreaTo != null) {
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
		
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
		
		if (typeCode != null && typeCode.size() != 0) {
			where.append(" AND(");
			String sql = typeCode.stream().map(it -> "renttype.code LIKE" + "'%" + it + "%' ").collect(Collectors.joining(" OR "));
			where.append(sql + ") ");
		}
	}
	@Override
	public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder) {
		StringBuilder sql = new StringBuilder("SELECT b.id, b.name, b.street, b.ward, b.districtid, "
				+ "b.numberofbasement, b.managername, b.managerphone, b.floorarea, b.rentprice, b.servicefee, b.brokeragefee FROM building b ");
		joinTable(buildingSearchBuilder, sql);
		StringBuilder where = new StringBuilder(" WHERE 1=1");
		queryNormal(buildingSearchBuilder, where);
		querySpecial(buildingSearchBuilder, where);
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
