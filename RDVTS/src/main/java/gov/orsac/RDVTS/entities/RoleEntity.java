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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_m")
public class RoleEntity {
    @Id
    @SequenceGenerator(name = "role_sequence", sequenceName = "role_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = " user_level_id")
    private Integer  userLevelId;

    @Column(name = "parent_role_id")
    private Integer parentRoleId;

    @Column(name = "can_edit")
    private boolean canEdit;

    @Column(name = "can_view")
    private boolean canView;

    @Column(name = "can_add")
    private boolean canAdd;

    @Column(name = "can_delete")
    private boolean canDelete;

    @Column(name = "can_approve")
    private boolean canApprove;

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
