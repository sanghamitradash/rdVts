package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadWorkMappingDto {
    private Integer roadId;
    private Integer userId;
    private Integer packageId;
    private String packageName;
    private String roadName;
    private Double roadLength;
    private Double roadLocation;
    private String roadAllignment;
    private Double roadWidth;
    private Integer gRoadId;
    private Integer geoMasterId;
    private Integer workId;
    private Integer gWorkId;
    private String gWorkName;
    private Boolean isActive;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private String piuName;
}
