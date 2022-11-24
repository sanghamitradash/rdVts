package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertCountDto {

    private Integer alertId;
    private Integer workId;
    private Long imei;
    private Integer alertTypeId;
    private String alertType;
    private Integer count;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double accuracy;
    private Double speed;
    private Integer roadId;
    private String roadName;
    private Integer packageId;
    private String packageName;
    private Date gpsDtm;
//    private Date alertTime;
//    private Boolean isActive;
//    private Integer createdBy;
//    private Date createdOn;
//    private Integer updatedBy;
//    private Date updatedOn;
    private Boolean isResolve;
    private Integer resolvedBy;
//    private Integer userId;
//    private String alertName;
    private Integer slNo;
}
