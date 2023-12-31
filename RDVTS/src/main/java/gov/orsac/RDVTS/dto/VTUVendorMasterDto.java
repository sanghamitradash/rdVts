package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VTUVendorMasterDto {

    private Integer id;

    private Integer vendorId;

    private Integer userId;

    private Integer deviceId;

    private String deviceName;

    private String vtuVendorName;

    private String vtuVendorAddress;

    private Long vtuVendorPhone;

    private Long customerCareNumber;

    private Boolean isActive;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;
    private String subs;
    private Integer slNo;

}
