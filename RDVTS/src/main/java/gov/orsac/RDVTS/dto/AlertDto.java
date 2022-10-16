package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertDto {
   private Integer id;
    private Integer vmmId ;
    private Integer alertTypeId ;
    private Double latitude ;
    private Double longitude ;
    private Double altitude ;
    private Double accuracy ;
    private Double speed ;
    private Date gpsDtm;
    private boolean active ;
    private Integer createdBy;
    private Date createdOn ;
    private Integer updatedBy ;
    private Date  updatedOn ;
    private boolean resolve ;
    private Integer resolvedBy ;
}
