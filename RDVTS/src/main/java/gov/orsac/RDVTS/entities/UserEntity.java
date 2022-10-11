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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_m")
public class UserEntity {
    @Id
    @SequenceGenerator(name = "user_menu_sequence", sequenceName = "user_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_menu_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_1")
    private Long mobile1;

    @Column(name = "mobile_2")
    private Long mobile2;

    @Column(name = "designation_id")
    private Integer designationId;

    @Column(name = "user_level_id")
    public Integer userLevelId;

    @Column(name = "role_id")
    public Integer roleId ;

    @Column(name="contractor_id")
    public Integer contractorId ;

    @Column(name = "is_active")
    private boolean isactive;

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
