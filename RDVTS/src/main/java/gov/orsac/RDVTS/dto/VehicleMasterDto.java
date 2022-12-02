package gov.orsac.RDVTS.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleMasterDto {


    private Integer id;
    private Integer deviceId;

    private String vehicleNo;

    private Integer vehicleTypeId;

    private String vehicleTypeName;

    private Long imeiNo1;

    private String model;

    private Double speedLimit;

    private String chassisNo;

    private String engineNo;

    private boolean active;

    private Integer createdBy;

    private Date createdOn ;

    private Integer updatedBy;

    private Date updatedOn;
    private Integer slNo;

    private Integer start;
    private String ownerName;
    private boolean deviceAssigned;
    private boolean workAssigned;
    private boolean activityAssigned;
    private boolean trackingStatus;
    private String contractorName;
    private Integer userId;
    private Integer contractorId;
    private Integer activityId;
    private Integer distId;
    private Integer divisionId;
    private Integer activityWorkMapId;

}
