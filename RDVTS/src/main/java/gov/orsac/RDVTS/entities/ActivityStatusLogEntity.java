package gov.orsac.RDVTS.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activity_status_log")
public class ActivityStatusLogEntity {

    @Id
    @SequenceGenerator(name = "activity_status_log_sequence", sequenceName = "activity_status_log_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_status_log_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "road_id")
    private Integer roadId;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "completed_road_length")
    private Double completedRoadLength;

    @Column(name = "executed_quantity")
    private Double executedQuantity;

    @Column(name = "activity_date")
    private Date activityDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_by")
    private Integer createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    @CreationTimestamp
    private Date createdOn;


}
