package com.javaweb.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.entity.BuildingEntity;

@Component
public class BuildingDTOConverter {

	@Autowired
	private ModelMapper modelMapper;

	public BuildingDTO toBuildingDTO(BuildingEntity item) {
		BuildingDTO buildingDTO = modelMapper.map(item, BuildingDTO.class);
//		DistrictEntity districtEntity = districtRepository.findById(item.getDistrictId());
//		if(districtEntity != null) {
//			buildingDTO.setAddress(item.getStreet() + ", " + item.getWard() + ", " + districtEntity.getName());
//		}

		buildingDTO.setAddress(item.getStreet() + ", " + item.getWard() + ", " + item.getDistrict().getName());
		String rentAreaValue = item.getAreas().stream().map(it -> it.getValue().toString())
				.collect(Collectors.joining(","));

		buildingDTO.setRentArea(rentAreaValue);

		return buildingDTO;
	}
}
