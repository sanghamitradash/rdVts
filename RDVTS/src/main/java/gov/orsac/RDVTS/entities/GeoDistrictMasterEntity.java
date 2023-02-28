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
@Table(name = "geo_district_m")
public class GeoDistrictMasterEntity {

    @Id
    @SequenceGenerator(name = "geo_district_m_sequence", sequenceName = "geo_district_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geo_district_m_sequence")
    @Column(name = "id")
    private Integer id;
    @Column(name = "g_district_name")
     private String gDistrictName;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;







}
