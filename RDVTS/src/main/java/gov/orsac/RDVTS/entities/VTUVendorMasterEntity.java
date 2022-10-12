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
@Table(name = "vtu_vendor_m")
public class VTUVendorMasterEntity {

    @Id
    @SequenceGenerator(name = "vtu_vendor_m_seq", sequenceName = "vtu_vendor_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vtu_vendor_m_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "vtu_vendor_name")
    private String vtuVendorName;

    @Column(name = "vtu_vendor_address")
    private String vtuVendorAddress;

    @Column(name = "vtu_vendor_phone")
    private Long vtuVendorPhone;

    @Column(name = "customer_care_number")
    private Long customerCareNumber;

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
}
