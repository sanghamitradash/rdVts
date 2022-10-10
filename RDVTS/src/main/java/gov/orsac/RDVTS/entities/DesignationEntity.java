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
@Table (name = "designation_m")
public class DesignationEntity {

    @Id
    @SequenceGenerator(name = "designation_m_sequence", sequenceName = "designation_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "designation_m_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "parent_id")
    private Integer parentId;

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

    @Column(name = "user_level_id")
    private Integer userLevelId;

}
