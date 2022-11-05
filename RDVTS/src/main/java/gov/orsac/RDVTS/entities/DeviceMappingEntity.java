package gov.orsac.RDVTS.entities;


import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "device_area_mapping")
public class DeviceMappingEntity {

    @Id
    @SequenceGenerator(name = "device_area_mapping_sequence", sequenceName = "device_area_mapping_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_area_mapping_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "dist_id")
    private Integer distId;

    @Column(name = "block_id")
    private Integer blockId;

    @Column(name = "division_id")
    private Integer divisionId;

    @Column(name = "state_id")
    private Integer stateId;

    @Column(name = "g_dist_id")
    private Integer gdistId;

    @Column(name = "g_block_id")
    private Integer gblockId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_by")
    private Integer createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    @UpdateTimestamp
    private Date updatedOn;

    @Column(name = "g_division_id")
    private Integer gDivisionId ;



}
