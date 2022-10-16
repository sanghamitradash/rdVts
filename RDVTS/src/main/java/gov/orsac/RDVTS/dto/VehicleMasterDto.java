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

    private String vehicleNo;

    private Integer vehicleTypeId;

    private String vehicleTypeName;


    private String model;

    private Double speedLimit;

    private String chassisNo;

    private String engineNo;

    private boolean active;

    private Integer createdBy;

    private Date createdOn ;

    private Integer updatedBy;

    private Date updatedOn;

    private Integer start;
    private String ownerName;
    private String firstName;
    private String lastName;
    private String middleName;
    private boolean deviceAssigned;
    private boolean workAssigned;
    private boolean trackingStatus;

}
