package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoMasterDto {

    private Integer id;

    private Integer geoWorkId;

    private Integer geoDistId;

    private Integer geoBlockId;

    private Integer geoPiuId;

    private Integer geoContractorId;

    private Integer workId;

    private Integer piuId;

    private Integer distId;

    private Integer blockId;

    private Integer roadId;

    private Integer contractorId;

    private Integer stateId;

    private Boolean isActive=true;

    private Integer createdBy;

    private String createdOn;

    private Integer updatedBy;

    private String updatedOn;
}
