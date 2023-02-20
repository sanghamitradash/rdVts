package gov.orsac.RDVTS.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDeviceMappingDto {
    private Integer id;

    private Integer vehicleId;

    private Integer deviceId;

    private String installationDate;

    private String installedBy;

    private boolean active;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    private String  vehicleNo;

    private Date deactivationDate;

    private String instalDate;

    private Long imeiNo1;

    private Long imeiNo2;

    private Long imei;


}
