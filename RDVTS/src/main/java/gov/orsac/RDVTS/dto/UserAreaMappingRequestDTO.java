package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAreaMappingRequestDTO {

    private Integer userId;
    private Integer stateId;
    private Integer gStateId;


    private Integer gDistId;
    private Integer distId;
    private Integer gBlockId;
    private Integer blockId;
    private Integer divisionId;
}
