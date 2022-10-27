package gov.orsac.RDVTS.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfo {

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

    private Integer vehicleId;

    private String vehicleNo;

    private Boolean isActive;

    private String vtuVendorName;
    private String vendorAddress;
    private Long vendorPhone;
    private Long customerCareNumber;

    private Integer distId;
    private Integer blockId;
    private Integer divisionId;
    private Integer gDistId;
    private Integer gBlockId;

    private String distName;
    private String blockName;
    private String gdistName;
    private String gblockName;

    private Integer slNo;

    private boolean deviceAssigned;
    private boolean vehicleAssigned;
}
