package gov.orsac.RDVTS.entities;

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
@Table(name = "user_area_mapping")
public class UserAreaMappingEntity {

    @Id
    @SequenceGenerator(name = "user_area_mapping_sequence", sequenceName = "user_area_mapping_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_area_mapping_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "g_dist_id")
    private Integer gDistId;

    @Column(name = "dist_id")
    private Integer distId;

    @Column(name = "g_block_id")
    private Integer gBlockId;

    @Column(name = "block_id")
    private Integer blockId;

    @Column(name = "division_id")
    private Integer divisionId;

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





}
