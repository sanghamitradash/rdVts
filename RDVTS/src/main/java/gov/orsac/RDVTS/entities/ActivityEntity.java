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
@Table(name = "activity_m")
public class ActivityEntity {

    @Id
    @SequenceGenerator(name = "activity_m_sequence", sequenceName = "activity_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_m_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_quantity")
    private Double activityQuantity;

    @Column(name="activity_start_date")
    private Date activityStartDate;

    @Column(name="activity_completion_date")
    private Date activityCompletionDate;

    @Column(name="actual_activity_start_date")
    private Date actualActivityStartDate;

    @Column(name="actual_activity_completion_date")
    private Date actualActivityCompletionDate;

    @Column(name = "executed_quantity")
    private Double executedQuantity;

    @Column(name = "work_id")
    private Integer workId;

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

    @Column(name = "activity_status")
    private Integer activityStatus;

    @Column(name = "g_activity_id")
    private Integer gActivityId;







}
