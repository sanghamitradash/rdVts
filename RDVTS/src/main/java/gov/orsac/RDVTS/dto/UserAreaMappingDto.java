package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAreaMappingDto {

    private Integer id;
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


    private Date createdOn;
    private Date updatedOn;
    private int createdBy;
    private int updatedBy;
    private Boolean isActive;

    private int page;
    private int size;
    private String sortOrder;
    private String sortBy;

}
