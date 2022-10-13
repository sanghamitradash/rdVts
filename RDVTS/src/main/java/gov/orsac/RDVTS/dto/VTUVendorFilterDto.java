package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VTUVendorFilterDto {

    private Integer vendorId;
    private Integer deviceId;
    private String vtuVendorName;

    private Integer offSet;
    private Integer limit;
    private Integer draw;
}
