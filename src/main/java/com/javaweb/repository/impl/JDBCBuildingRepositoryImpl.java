package com.javaweb.repository.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;

@Repository
@PropertySource("classpath:application.properties")
public class JDBCBuildingRepositoryImpl implements BuildingRepository {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Value("${spring.datasource.url}")
	private static String DB_URL;
	@Value("${spring.datasource.username}")
	private static String USER;
	@Value("${spring.datasource.username}")
	private static String PASS;

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

	public static void queryNormal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
		try {
			Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
			for (Field item : fields) {
				item.setAccessible(true);
				String fieldName = item.getName();
				if (!fieldName.equals("staffId") && !fieldName.equals("typeCode") && !fieldName.startsWith("area")
						&& !fieldName.startsWith("rentPrice")) {
					Object value = item.get(buildingSearchBuilder);
					if (value != null) {
						if ((item.getType().getName().equals("java.lang.Long")
								|| item.getType().getName().equals("java.lang.Integer"))
								&& !fieldName.equals("managerPhone")) {
							where.append(" AND b." + fieldName.toLowerCase() + " = " + value);
						} else {
							where.append(" AND b." + fieldName.toLowerCase() + " LIKE '%" + value + "%' ");
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
		if (staffId != null) {
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
			String sql = typeCode.stream().map(it -> "renttype.code LIKE" + "'%" + it + "%' ")
					.collect(Collectors.joining(" OR "));
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
		Query query =  entityManager.createQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public void create(BuildingRequestDTO buildingRequestDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(BuildingRequestDTO buildingRequestDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
