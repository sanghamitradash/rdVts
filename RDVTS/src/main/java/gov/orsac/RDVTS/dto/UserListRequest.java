package gov.orsac.RDVTS.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListRequest {

    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Long mobile1;
    private Long mobile2;
    private String designation;
    public String userLevel;
    public String role ;
    public int contractor ;
    private boolean isactive;

    private int searchId;
    private int deptId;

    private int userLevelId;
    private int distId;
    private int blockId;
    private int gpId;
    private int villageId;
    private int divisionId;
    private int subDivisionId;
    private int sectionId;
    private int page;
    private int size;
    private String sortOrder;
    private String sortBy;


}
