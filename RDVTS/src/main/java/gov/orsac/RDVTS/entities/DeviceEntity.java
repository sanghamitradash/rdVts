package gov.orsac.RDVTS.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "device_m")
public class DeviceEntity {

    @Id
    @SequenceGenerator(name = "device_m_sequence", sequenceName = "device_m_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_m_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "imei_no_1")
    private Long imeiNo1;

    @Column(name = "sim_icc_id_1")
    private Long simIccId1;

    @Column(name = "mobile_number_1")
    private Long mobileNumber1;

    @Column(name = "imei_no_2")
    private Long imeiNo2;

    @Column(name = "sim_icc_id_2")
    private Long simIccId2;

    @Column(name = "mobile_number_2")
    private Long mobileNumber2;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "vtu_vendor_id")
    private Integer vtuVendorId;

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

    @Column(name = "device_no")
    private Integer deviceNo;


}
