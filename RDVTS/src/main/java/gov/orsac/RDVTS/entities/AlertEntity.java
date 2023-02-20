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
@Table(name = "alert_data")
public class AlertEntity {

    @Id
    @SequenceGenerator(name = "alert_data_sequence", sequenceName = "alert_data_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alert_data_sequence")
    @Column(name = "id")
    private Integer id;



    @Column(name = "imei")
    private Long imei;
    @Column(name = "alert_type_id")
    private Integer alertTypeId ;

    @Column(name = "latitude")
    private Double latitude ;
    @Column(name = "longitude")
    private Double longitude ;
    @Column(name = "altitude")
    private Double altitude ;
    @Column(name = "accuracy")
    private Double accuracy ;
    @Column(name = "speed")
    private Double speed ;
    @Column(name = "gps_dtm")
    private Date gpsDtm;

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

    @Column(name = "is_resolve")
    private boolean resolve ;
    @Column(name = "resolved_by")
    private Integer resolvedBy ;









}
