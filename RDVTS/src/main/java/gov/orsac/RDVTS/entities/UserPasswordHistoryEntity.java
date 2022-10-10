package gov.orsac.RDVTS.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordHistoryEntity {

    private Integer id;
    private Integer userId;
    private String password;
    private Boolean isActive;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
}
