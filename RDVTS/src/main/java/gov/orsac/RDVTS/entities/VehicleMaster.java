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
@Table(name = "vehicle_m")
public class VehicleMaster {
    @Id
    @SequenceGenerator(name = "vehicle_master_sequence", sequenceName = "vehicle_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_master_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "vehicle_no")
    private String vehicleNo;

    @Column(name = "vehicle_type_id")
    private Integer vehicleTypeId;

    @Column(name = "model")
    private String model;

    @Column(name = "speed_limit")
    private Double speedLimit;

    @Column(name = "chassis_no")
    private String chassisNo;

    @Column(name = "engine_no")
    private String engineNo;

    @Column(name = "is_active")
    private Boolean active;

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
