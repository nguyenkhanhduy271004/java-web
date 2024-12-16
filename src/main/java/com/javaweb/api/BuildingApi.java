package com.javaweb.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.customexception.FieldRequiredException;
import com.javaweb.model.BuildingDTO;
import com.javaweb.service.BuildingService;
import com.javaweb.service.impl.BuildingServiceImpl;



@RestController
public class BuildingApi {
	
	@Autowired
	private BuildingService buildingService;
	
	@GetMapping(value = "/api/building/")
	public List<BuildingDTO> getBuilding(@RequestParam(value = "name", required = false) String name, 
			@RequestParam(value = "floorArea", required = false) Long floorArea,
			@RequestParam(value = "districtId", required = false) Long districtId,
			@RequestParam(value = "ward", required = false) String ward,
			@RequestParam(value = "street", required = false) String street,
			@RequestParam(value = "numberOfBasement", required = false) Long numberOfBasement,
			@RequestParam(value = "direction", required = false) String direction,
			@RequestParam(value = "level", required = false) String level,
			@RequestParam(value = "areaFrom", required = false) Long areaFrom,
			@RequestParam(value = "areaTo", required = false) Long areaTo,
			@RequestParam(value = "rentPriceFrom", required = false) Long rentPriceFrom,
			@RequestParam(value = "rentPriceTo", required = false) Long rentPriceTo,
			@RequestParam(value = "managerName", required = false) String managerName,
			@RequestParam(value = "managerPhone", required = false) String managerPhone,
			@RequestParam(value = "staffId", required = false) Long staffId,
			@RequestParam(value = "typeCode", required = false) List<String> typeCode
			) {
		List<BuildingDTO> result = buildingService.findAll(name, floorArea, districtId, ward, street, numberOfBasement, direction, level, areaFrom, areaTo, rentPriceFrom, rentPriceTo, managerName, managerPhone, staffId, typeCode);
		return result;	
	}
	
	public void validate(@RequestBody BuildingDTO buildingDto)  {
		if (buildingDto.getNameBuilding() == null || buildingDto.getNameBuilding() == "" || 
				buildingDto.getNameBuilding() == null) {
			throw new FieldRequiredException("Name or numberbasement is null");
		}
	}
}
