package io.mosip.kernel.masterdata.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data


public class DeviceSpecificationDto {
	private String id;
	private String name;
	private String brand;
	private String model;
	private String deviceTypeCode;
	private String minDriverversion;
	private String description;
	private String langCode;
	private Boolean isActive;
}
