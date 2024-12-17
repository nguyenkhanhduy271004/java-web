package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;

@Component
public class BuildingDTOConverter {
	
	@Autowired
	private DistrictRepository districtRepository;
	@Autowired
	private RentAreaRepository rentAreaRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public BuildingDTO toBuildingDTO(BuildingEntity item) {
		BuildingDTO buildingDTO = modelMapper.map(item, BuildingDTO.class);
		DistrictEntity districtEntity = districtRepository.findById(item.getDistrictId());
		buildingDTO.setAddress(item.getStreet() + ", " + item.getWard() + ", " + districtEntity.getName());
		
		List<RentAreaEntity> areaEntities = rentAreaRepository.findAllRentAreaValueByBuildingId(item.getId());
		String rentAreaValue = areaEntities.stream()
				.map(it -> it.getValue().toString())
				.collect(Collectors.joining(","));	
			
		buildingDTO.setRentArea(rentAreaValue);
		
		
		
		return buildingDTO;
	}
}
