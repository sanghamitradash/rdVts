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
@Table(name = "geo_master")
public class GeoMasterEntity {
    @Id
    @SequenceGenerator(name = "geo_master_sequence", sequenceName = "geo_master_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geo_master_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "g_work_id")
    private Integer geoWorkId;

    @Column(name = "g_dist_id")
    private Integer geoDistId;

    @Column(name = "g_block_id")
    private Integer geoBlockId;

    @Column(name = "g_piu_id")
    private Integer geoPiuId;

    @Column(name = "g_contractor_id")
    private Integer geoContractorId;

    @Column(name = "work_id")
    private Integer workId;

    @Column(name = "piu_id")
    private Integer piuId;

    @Column(name = "dist_id")
    private Integer distId;

    @Column(name = "block_id")
    private Integer blockId;

    @Column(name = "road_id")
    private Integer roadId;

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
