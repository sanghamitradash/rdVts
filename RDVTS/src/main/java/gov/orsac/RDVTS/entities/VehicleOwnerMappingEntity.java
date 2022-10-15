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
@Table(name = "vehicle_owner_mapping")
public class VehicleOwnerMappingEntity {
    @Id
    @SequenceGenerator(name = "vehicle_owner_mapping_sequence", sequenceName = "vehicle_owner_mapping_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_owner_mapping_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "contractor_id")
    private Integer contractorId;


    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "is_contractor")
    private boolean Contractor;

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
