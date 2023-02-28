package gov.orsac.RDVTS.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    @JsonProperty("STATE_NAME")
    private String stateName;
    @JsonProperty("DISTRICT_NAME")
    private String districtName;
    @JsonProperty("PIU_NAME")
    private String piuName;
    @JsonProperty("Road_Code")
    private String roadCode;
    @JsonProperty("ROAD_NAME")
    private String roadName;
    @JsonProperty("CONTRACTOR_NAME")
    private String contractorName;
    @JsonProperty("PACKAGE_No")
    private String packageNo;
    @JsonProperty("SANCTION_LENGTH")
    private Double sanctionLength;
    @JsonProperty("COMPLETED_ROAD_LENGTH")
    private Double completedRoadLength;
    @JsonProperty("SANCTION_DATE")
    private String sanctionDate;
    @JsonProperty("AWARD_DATE")
    private String awardDate;
    @JsonProperty("COMPLETION_DATE")
    private String completionDate;
    @JsonProperty("PMIS_FINALIZE_DATE")
    private String pMisFinalizeDate;
    @JsonProperty("ACTIVITY_NAME")
    private String activityName;
    @JsonProperty("ACTIVITY_QUANTITY")
    private Double activityQuantity;
    @JsonProperty("ACTIVITY_START_DATE")
    private String activityStartDate;
    @JsonProperty("ACTIVITY_COMPLETION_DATE")
    private String activityCompletionDate;
    @JsonProperty("ACTUAL_ACTIVITY_START_DATE")
    private String actualActivityStartDate;
    @JsonProperty("ACTUAL_ACTIVITY_COMPLETION_DATE")
    private String actualActivityCompletionDate;
    @JsonProperty("EXECUTED_QUANTITY")
    private Double executedQuantity;
}
