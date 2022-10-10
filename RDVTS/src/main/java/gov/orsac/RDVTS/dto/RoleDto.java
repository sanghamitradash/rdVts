package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private Integer id;
    private String name;
    private String description;
    private Integer  userLevelId;
    private  Integer parentRoleId;
    private boolean canEdit;
    private boolean canView;
    private boolean canAdd;
    private boolean canDelete;
    private boolean canApprove;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private boolean active;
}
