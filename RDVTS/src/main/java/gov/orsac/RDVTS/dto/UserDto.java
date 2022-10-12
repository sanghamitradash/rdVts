package gov.orsac.RDVTS.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
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
    public Integer otp ;



    private int page;
    private int size;
    private String sortOrder;
    private String sortBy;

}
