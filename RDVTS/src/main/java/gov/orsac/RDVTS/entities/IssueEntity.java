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
@Table(name="issue_m")
public class IssueEntity {

    @Id
    @SequenceGenerator(name = "issue_m_sequence", sequenceName = "issue_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_m_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "activity_work_id")
    private Integer activityWorkId;

    @Column(name = "issue_reason")
    private String issueReason;

    @Column(name ="resolved_status")
    private Integer resolvedStatus;

    @Column(name = "resolved_date")
    private Date resolvedDate;

    @Column(name = "resolved_by")
    private Integer resolvedBy;

    @Column(name = "issue_image")
    private String issueImage;

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
