package gov.orsac.RDVTS.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "road_m")
public class RoadMasterEntity {

    @Id
    @SequenceGenerator(name = "road_m_seq", sequenceName = "road_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "road_m_seq")

    @Column(name = "id")
    private Integer id;

    @Column(name = "road_name")
    private String roadName;

    @Column(name = "sanction_length")
    private Double sanctionLength;

    @Column(name = "road_location")
    private Double roadLocation;

    @Column(name = "road_allignment")
    private String roadAllignment;

    @Column(name = "geom")
    private Geometry geom;

    @Column(name = "road_width")
    private Double roadWidth;

    @Column(name = "g_road_id")
    private Integer groadId;

    @Column(name="is_active",nullable = false)
    private Boolean isActive=true;

    @Column(name = "createdBy")
    private Integer createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "updatedBy")
    private Integer updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    @UpdateTimestamp
    private Date updatedOn;

    @Column(name = "sanction_date")
    private Date sanctionDate;

    @Column(name = "road_code")
    private String roadCode;

    @Column(name = "road_status")
    private Integer roadStatus;

    @Column(name = "approval_status")
    private Integer approvalStatus;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "gis_length")
    private Double gisLength;



}
