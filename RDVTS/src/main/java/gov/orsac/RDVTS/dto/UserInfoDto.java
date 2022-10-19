package gov.orsac.RDVTS.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {


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
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private String userName;

    private String password;


}
