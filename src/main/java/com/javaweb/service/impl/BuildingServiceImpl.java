package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.impl.BuildingRepositoryImpl;
import com.javaweb.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService{
	
	@Autowired
	private BuildingRepository buildingRepository;

	@Override
	public List<BuildingDTO> findAll(String name, Long floorArea, Long districtId, String ward, String street, Long numberOfBasement, String direction, String level, Long areaFrom, Long areaTo, Long rentPriceFrom, Long rentPriceTo, String managerName, String managerPhone, Long staffId, List<String> typeCode) {
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(name, floorArea, districtId, ward, street, numberOfBasement, direction, level, areaFrom, areaTo, rentPriceFrom, rentPriceTo, managerName, managerPhone, staffId, typeCode);
		List<BuildingDTO> result = new ArrayList<BuildingDTO>();
		for (BuildingEntity item:buildingEntities) {
			BuildingDTO buildingDTO = new BuildingDTO();
			buildingDTO.setNameBuilding(item.getNameBuilding());
			buildingDTO.setAddress(item.getStreet() + "," + item.getWard() + ", Quận " + item.getDistrictId()); 
			buildingDTO.setNumberOfBasement(item.getNumberOfBasement());
			buildingDTO.setManagerName(item.getManagerName());
			buildingDTO.setManagerPhone(item.getManagerPhone());
			buildingDTO.setFloorArea(item.getFloorArea());
			buildingDTO.setEmptySpace(item.getEmptySpace());
			buildingDTO.setRentPrice(item.getRentPrice());
			buildingDTO.setServiceFee(item.getServiceFee());
			buildingDTO.setBrokerageFee(item.getBrokerageFee());
			buildingDTO.setRentalArea(item.getRentalArea());
			result.add(buildingDTO);
		}
		return result;
	}
	
}
