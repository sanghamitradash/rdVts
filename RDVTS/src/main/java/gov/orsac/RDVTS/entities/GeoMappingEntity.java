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
@Table(name = "geo_mapping")
public class GeoMappingEntity {

    @Id
    @SequenceGenerator(name = "geo_mapping_id_sequence", sequenceName = "geo_mapping_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geo_mapping_id_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "piu_id")
    private Integer workId;

    @Column(name = "road_id")
    private Integer roadId;
    @Column(name = "contractor_id")
    private Integer contractorId;
    @Column(name = "g_state_id")
    private Integer gStateId;
    @Column(name = "state_id")
    private Integer stateId;

    @Column(name = "g_dist_id")
    private Integer gDistId;
    @Column(name = "dist_id")
    private Integer distId;


    @Column(name = "activity_id")
    private Integer activityId;



    @Column(name = "activity_quantity")
    private Double activityQuantity;

    @Column(name = "activity_start_date")
    private Date activityStartDate;

    @Column(name = "activity_completion_date")
    private Date activityCompletionDate;

    @Column(name = "actual_activity_start_date")
    private Date actualActivityStartDate;

    @Column(name = "actual_activity_completion_date")
    private Date actualActivityCompletionDate;

    @Column(name = "executed_quantity")
    private Double executedQuantity;

    @Column(name = "activity_status")
    private Integer activityStatus;

    @Column(name = "completed_road_length")
    private Double completedRoadLength;

    @Column(name = "completion_date")
    private Date completionDate;

    @Column(name = "pmis_finalize_date")
    private Date pmisFinalizeDate;

    @Column(name = "award_date")
    private Date awardDate;


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
