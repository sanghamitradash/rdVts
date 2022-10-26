package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDeviceInfo {
    private Integer id;
    private Integer vehicleId;
    private Integer deviceId;
    private Date installationDate;
    private Long imeiNo1;
    private String simIccId1;
    private Long mobileNumber1;
    private Long imeiNo2;
    private String simIccId2;
    private Long mobileNumber2;
    private String modelName;
    private Integer deviceNo;
    private String installedBy;
    private String deactivationDate;
}
