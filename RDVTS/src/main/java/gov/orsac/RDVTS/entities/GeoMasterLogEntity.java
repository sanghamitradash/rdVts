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
@Table(name = "geo_master_log")
public class GeoMasterLogEntity {

    @Id
    @SequenceGenerator(name = "geo_master_log_sequence", sequenceName = "geo_master_log_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geo_master_log_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "state_name")
    private String stateName;
    @Column(name = "district_name")
    private String districtName;
    @Column(name = "piu_name")
    private String piuName;

    @Column(name = "road_code")
    private String roadCode;
    @Column(name = "road_name")
    private String roadName;
    @Column(name = "contractor_name")
    private String contractorName;

    @Column(name = "package_no")
    private String packageNo;


    @Column(name = "sanction_length")
    private Double sanctionLength;

    @Column(name = "completed_road_length")
    private Double completedRoadLength;

    @Column(name = "sanction_date")
    private String sanctionDate;
    @Column(name = "award_date")
    private String awardDate;
    @Column(name = "completion_date")
    private String completionDate;


    @Column(name = "PMIS_FINALIZE_DATE")
    private String pMisFinalizeDate;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_quantity")
    private Double activityQuantity;

    @Column(name = "activity_start_date")
    private String activityStartDate;

    @Column(name = "activity_completion_date")
    private String activityCompletionDate;

    @Column(name = "actual_activity_start_date")
    private String actualActivityStartDate;

    @Column(name = "actual_activity_completion_date")
    private String actualActivityCompletionDate;

    @Column(name = "executed_quantity")
    private Double executedQuantity;


    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    @CreationTimestamp
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    @UpdateTimestamp
    private Date updatedOn;




    public GeoMasterLogEntity(String stateName, String districtName, String piuName, String roadCode, String roadName, String contractorName, String packageNo, Double sanctionLength, Double completedRoadLength, String sanctionDate, String awardDate, String completionDate, String pMisFinalizeDate, String activityName, Double activityQuantity, String activityStartDate, String activityCompletionDate, String actualActivityStartDate, String actualActivityCompletionDate, Double executedQuantity) {
        this.stateName=stateName;
        this.districtName=districtName;
        this.piuName=piuName;

        this.roadCode          =roadCode;
        this.roadName  =roadName;
        this.contractorName  =contractorName;
        this.packageNo  =packageNo;
        this.sanctionLength  =sanctionLength;
        this.completedRoadLength  =completedRoadLength;
        this.sanctionDate  =sanctionDate;
        this.awardDate  =awardDate;
        this.completionDate  =completionDate;
        this.pMisFinalizeDate  =pMisFinalizeDate;
        this.activityName  =activityName;
        this.activityQuantity  =activityQuantity;
        this.activityStartDate  =activityStartDate;
        this.activityCompletionDate  =activityCompletionDate;
        this.actualActivityStartDate  =actualActivityStartDate;
        this.actualActivityCompletionDate  =actualActivityCompletionDate;
        this.executedQuantity  =executedQuantity;
    }
}
