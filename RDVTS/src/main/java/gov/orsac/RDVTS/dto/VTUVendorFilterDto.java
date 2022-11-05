package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VTUVendorFilterDto {

    private Integer vendorId;
    private Integer userId;
    private Integer deviceId;
    private String vtuVendorName;
    private Integer distId;
    private Integer divisionId;

    private Integer offSet;
    private Integer limit;
    private Integer draw;
}
