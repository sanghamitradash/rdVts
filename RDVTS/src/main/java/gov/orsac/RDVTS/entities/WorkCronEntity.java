package gov.orsac.RDVTS.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "work_cron")
public class WorkCronEntity {
    @Id
    @SequenceGenerator(name = "work_cron_sequence", sequenceName = "work_cron_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_cron_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "imei_no")
    private Long imeiNo;

    @Column(name = "total_speed_work")
    private Double totalSpeedWork;

    @Column(name = "avg_speed_today")
    private Double avgSpeedToday;

    @Column(name = "total_active_vehicle")
    private Integer totalActiveVehicle;

    @Column(name = "total_distance")
    private Double totalDistance;

    @Column(name = "today_distance")
    private Double todayDistance;

}
