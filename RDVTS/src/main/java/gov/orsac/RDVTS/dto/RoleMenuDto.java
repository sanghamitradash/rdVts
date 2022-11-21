package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuDto {

    private Integer roleId;
    private List<Integer> menuId;
    private Integer createdBy;
    private Boolean isDefault;
    private Integer userId;
}
