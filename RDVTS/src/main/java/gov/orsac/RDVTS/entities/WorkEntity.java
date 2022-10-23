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
@Table (name = "work_m")
public class WorkEntity {

    @Id
    @SequenceGenerator(name = "work_m_sequence", sequenceName = "work_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_m_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "g_work_id")
    private Integer geoWorkId;

    @Column(name = "g_work_name")
    private String geoWorkName;

    @Column(name = "award_date")
    private Date awardDate;

    @Column(name = "completion_date")
    private Date completionDate;

    @Column(name = "pmis_finalize_date")
    private Date pmisFinalizeDate;

    @Column(name = "work_status")
    private Integer workStatus;

    @Column(name = "approval_status")
    private Integer approvalStatus;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name="is_active",nullable = false)
    private Boolean isActive=true;

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
