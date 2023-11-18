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
@Table(name = "activity_patch_details")
public class ActivityPatchDetailsEntity {

    @Id
    @SequenceGenerator(name = "activity_patch_details_sequence", sequenceName = "activity_patch_details_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_patch_details_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "road_id")
    private Integer roadId;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "patch_length_from")
    private String patchLengthFrom;

    @Column(name = "patch_length_to")
    private String patchLengthTo;

    @Column(name = "patch_name")
    private String patchName;

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
