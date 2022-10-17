package gov.orsac.RDVTS.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class VehicleWorkMappingDto {
    private Integer id;
    private Integer vehicleId;
    private Integer workId;
    private String workName;
    private String startTime;
    private String endTime;
    private Date startDate;
    private Date endDate;
    private boolean active;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private Date currentDate;
    private Date deactivationDate;
}
