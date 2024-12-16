package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.repository.impl.BuildingRepositoryImpl;
import com.javaweb.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService{
	
	@Autowired
	private BuildingRepository buildingRepository;
	@Autowired
	private DistrictRepository districtRepository;
	@Autowired
	private RentAreaRepository rentAreaRepository;

	@Override
	public List<BuildingDTO> findAll(Map<String, Object> params, List<String> typeCode) {
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(params, typeCode);
		List<BuildingDTO> result = new ArrayList<BuildingDTO>();
		for (BuildingEntity item:buildingEntities) {
			BuildingDTO buildingDTO = new BuildingDTO();
			buildingDTO.setNameBuilding(item.getNameBuilding());
			
			DistrictEntity districtEntity = districtRepository.findById(item.getDistrictId());
			buildingDTO.setAddress(item.getStreet() + ", " + item.getWard() + ", " + districtEntity.getName());
			
			buildingDTO.setNumberOfBasement(item.getNumberOfBasement());
			buildingDTO.setManagerName(item.getManagerName());
			buildingDTO.setManagerPhone(item.getManagerPhone());
			buildingDTO.setFloorArea(item.getFloorArea());
			buildingDTO.setEmptySpace(item.getEmptySpace());
			buildingDTO.setRentPrice(item.getRentPrice());
			buildingDTO.setServiceFee(item.getServiceFee());
			buildingDTO.setBrokerageFee(item.getBrokerageFee());
			
			List<RentAreaEntity> areaEntities = rentAreaRepository.findAllRentAreaValueByBuildingId(item.getId());
			StringBuilder rentAreaValue = new StringBuilder();

			for (int i = 0; i < areaEntities.size(); i++) {
			    rentAreaValue.append(areaEntities.get(i).getValue());
			    if (i < areaEntities.size() - 1) {
			        rentAreaValue.append(",");
			    }
			}
			buildingDTO.setRentalArea(rentAreaValue.toString());
			result.add(buildingDTO);
		}
		return result;
	}


	
}
