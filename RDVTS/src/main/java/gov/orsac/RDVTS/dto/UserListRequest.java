package gov.orsac.RDVTS.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListRequest {
    private Integer userId;
    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Long mobile1;
    private Long mobile2;
    private Integer designationId;
    private String designation;

    public Integer userLevelId;
    public String userLevel;

    public Integer roleId ;
    public String role ;

    public Integer contractorId ;
    public String contractor ;
    private boolean isactive;



    private Integer distId;
    private Integer blockId;
    private Integer gpId;
    private Integer villageId;
    private Integer divisionId;
    private Integer subDivisionId;
    private Integer sectionId;
    private Integer offSet;
    private Integer limit;
    private Integer draw;


}
