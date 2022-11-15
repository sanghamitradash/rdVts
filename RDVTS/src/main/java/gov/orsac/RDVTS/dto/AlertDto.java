package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertDto {
   private Integer id;
    private Integer vmmId ;
    private Long imei;
    private Integer alertTypeId ;
    private String alertTypeName;
    private Double latitude ;
    private Double longitude ;
    private Double altitude ;
    private Double accuracy ;
    private Double speed ;
    private Date gpsDtm;
    private boolean active ;
    private Integer createdBy;
    private Date createdOn ;
    private Integer updatedBy ;
    private Date  updatedOn ;
    private boolean resolve ;
    private Integer resolvedBy ;
    private String resolvedByUser;
    private double speedLimit;
    private Integer vehicleId;
    private Integer deviceId;

}
