package com.javaweb.repository;

import java.util.ArrayList;
import java.util.List;

import com.javaweb.repository.entity.BuildingEntity;

public interface BuildingRepository {
	List<BuildingEntity> findAll(String name,
			Long floorArea,  Long districtId, String ward, 
			String street, Long numberOfBasement, String direction, 
			String level, Long areaFrom, Long areaTo, Long rentPriceFrom, 
			Long rentPriceTo, String managerName, String managerPhone, 
			Long staffId, List<String> typeCode);
}
