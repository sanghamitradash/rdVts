package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadMasterDto {

    private Integer id;

    private Integer userId;

    private Integer packageId;

    private String packageName;

    private String roadName;

    private Double roadLength;

    private Double roadLocation;

    private String roadAllignment;

    private Geometry geom;

    private Double roadWidth;

    private Integer groadId;

    private Integer geoMasterId;

    private Boolean isActive;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;
}
