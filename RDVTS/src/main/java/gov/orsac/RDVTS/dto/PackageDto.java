package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageDto
{
    private Integer packageId;
    private String packageNo;
    private Integer roadId;
    private Boolean isActive;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private String piuName;
    private Date awardDate;
    private Date pmisFinalizeDate;
    private Date completionDate;
    private String workStatusName;
    //List<VehicleMasterDto> vehicle;
    List<RoadMasterDto> road;
}
