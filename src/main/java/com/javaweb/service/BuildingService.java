package com.javaweb.service;

import java.util.List;

import com.javaweb.model.BuildingDTO;

public interface BuildingService {
	List<BuildingDTO> findAll(String name, Long floorArea, Long districtId, String ward, String street, Long numberOfBasement, String direction, String level, Long areaFrom, Long areaTo, Long rentPriceFrom, Long rentPriceTo, String managerName, String managerPhone, Long staffId, List<String> typeCode);
}
