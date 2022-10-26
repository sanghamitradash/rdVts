package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignationDto {

    private Integer id;

    private Integer userId;

    private String name;

    private String description;

    private Integer parentId;

    private String parentName;

    private Boolean isActive=true;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    private Integer userLevelId;

    private String userLevelName;

    private Integer offSet;
    private Integer limit;
    private Integer draw;
}
