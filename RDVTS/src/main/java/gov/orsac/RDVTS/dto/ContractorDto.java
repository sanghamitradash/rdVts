package gov.orsac.RDVTS.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractorDto {

    private Integer id;
    private String name;
    private Long mobile;
    private String address;
    private Integer gContractorId;
    private Boolean isActive = true;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;


    private Integer userId;

}
