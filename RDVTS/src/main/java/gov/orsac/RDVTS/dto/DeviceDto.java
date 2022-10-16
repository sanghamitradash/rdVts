package gov.orsac.RDVTS.dto;

import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    private Integer id;
    private Long imeiNo1;
    private String simIccId1;
    private Long mobileNumber1;
    private Long imeiNo2;
    private String simIccId2;
    private Long mobileNumber2;
    private String modelName;
    private Integer vtuVendorId;
    private Integer deviceNo;

    private Boolean isActive = true;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;

    private String vtuVendorName;
    private String vendorAddress;
    private Long vendorPhone;
    private Long customerCareNumber;

    private List<DeviceMappingEntity> deviceMapping;
}

