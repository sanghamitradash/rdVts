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
public class VehicleOwnerMappingDto {
    private Integer id;
    private Integer vehicleId;
    private Integer contractorId;
    private Integer userId;
    private boolean Contractor;
    private boolean active;
    private Integer createdBy;
    private Date createdOn ;
    private Integer updatedBy;
    private Date updatedOn;
}
