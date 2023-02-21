package gov.orsac.RDVTS.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dashboard_cron")
public class DashboardCronEntity {
    @Id
    @SequenceGenerator(name = "dashboard_cron_sequence", sequenceName = "dashboard_cron_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dashboard_cron_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "active")
    private Integer active;

    @Column(name = "in_active")
    private Integer inActive;

    @Column(name = "area_id")
    private Integer areaId;

    @Column(name = "area_type_id")
    private Integer areaTypeId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "process_time")
    @CreationTimestamp
    private Date processTime = new Date(System.currentTimeMillis());

}
