package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDto {


    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Long mobile1;
    private Long mobile2;
    private Integer designationId;
    public Integer userLevelId;
    public Integer roleId ;
    public Integer contractorId ;
    private boolean isactive;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private Integer userId;


    private Integer stateId;
    private Integer gStateId;


    private Integer gdistId;
    private Integer distId;
    private Integer gblockId;
    private Integer blockId;
    private Integer divisionId;


    private String gDistName;
    private String distName;
    private String gBlockName;
    private String blockName;
    private String divisionName;
    private String gStateName;


    private Boolean isActive;


    private int page;
    private int size;
    private String sortOrder;
    private String sortBy;
}
