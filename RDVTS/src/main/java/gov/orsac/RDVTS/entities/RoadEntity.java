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
@Table(name = "geo_construction_m")
public class RoadEntity {

    @Id
    @SequenceGenerator(name = "geo_construction_m_seq", sequenceName = "geo_construction_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geo_construction_m_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "road_name")
    private String roadName;

    @Column(name = "road_length")
    private Double roadLength;

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

    @Column(name = "geo_master_id")
    private Integer geoMasterId;

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

    @Column(name = "completed_road_length")
    private Double completedRoadLength;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sanction_date")
    @UpdateTimestamp
    private Date sanctionDate;

    @Column(name = "road_code")
    private String roadCode;

    @Column(name = "road_status")
    private Integer roadStatus;

    @Column(name = "approval_status")
    private Integer approvalStatus;

    @Column(name = "approved_by")
    private Integer approvedBy;
}
