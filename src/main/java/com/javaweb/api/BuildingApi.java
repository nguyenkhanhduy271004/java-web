package com.javaweb.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public List<BuildingDTO> getBuilding(@RequestParam Map<String, Object> params,@RequestParam(value = "typeCode", required = false) List<String> typeCode) {
		List<BuildingDTO> result = buildingService.findAll(params, typeCode);
		return result;	
	}
	
	public void validate(@RequestBody BuildingDTO buildingDto)  {
		if (buildingDto.getNameBuilding() == null || buildingDto.getNameBuilding() == "" || 
				buildingDto.getNameBuilding() == null) {
			throw new FieldRequiredException("Name or numberbasement is null");
		}
	}
}
