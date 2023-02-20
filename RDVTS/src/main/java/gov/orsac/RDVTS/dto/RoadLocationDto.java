package gov.orsac.RDVTS.dto;

import gov.orsac.RDVTS.entities.RoadEntity;
import gov.orsac.RDVTS.entities.RoadLocationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadLocationDto {
    private Integer roadId;
    private Integer userId;
    private List<Double> longitude;
    private List<Double> latitude;
//    private Double altitude;
//    private Double accuracy;
    private Boolean isActive;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private List<RoadLocationEntity> roadLocation;
    private List<RoadEntity> roadMaster;
    private String geom;
    private Double gisLength;
}
