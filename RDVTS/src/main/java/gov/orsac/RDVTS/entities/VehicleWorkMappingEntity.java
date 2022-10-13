package gov.orsac.RDVTS.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicle_device_mapping")
public class VehicleWorkMappingEntity {
    @Id
    @SequenceGenerator(name = "vehicle_work_mapping_sequence", sequenceName = "vehicle_work_mapping_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_work_mapping_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "work_id")
    private Integer workId;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_ime")
    private Date endTime;

    @Column(name = "startDate")
    private Date start_date;

    @Column(name = "end_date")
    private Date end_date;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "created_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    @CreationTimestamp
    private Date createdOn = new Date(System.currentTimeMillis());

    @Column(name = "updated_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    @UpdateTimestamp
    private Date updatedOn;
}
