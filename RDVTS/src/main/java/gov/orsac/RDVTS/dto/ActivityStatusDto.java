package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityStatusDto {

    private Integer id;
    private String name;
    private Boolean isActive;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
}
