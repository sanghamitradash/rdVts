package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import springfox.documentation.spring.web.json.Json;


import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadMasterDto {

    private Integer id;

    private Integer userId;

//    private List<Integer> workIds;

    private Integer workIds;

    private Integer distIds;

    private Integer blockIds;

    private Integer contractIds;

    private Integer packageId;

    private String packageName;

    private String roadName;

    private Double roadLength;

    private Double roadLocation;

    private String roadAllignment;

    private String geom;

    private String geoJSON;

    private Double roadWidth;

    private Integer groadId;

//    private Integer geoMasterId;

    private Boolean isActive;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    private Integer slNo;

    private Double completedRoadLength;
    private Date sanctionDate;
    private String roadCode;
    private Integer roadStatus;
    private String subs;
    private Integer approvalStatus;
    private Integer approvedBy;

    private Integer activityId;
    private Integer vehicleId;
    private String piuName;
    private Boolean geomPresent;
    private Integer roadId;
    private Double sanctionLength;
    private Date awardDate;
    private Date pmisFinalizeDate;
    private Date completionDate;
    private String workStatusName;

    private String vehicleName;
    private List<ActivityDto> activityList;

    private String sanctionDateStr;

}
