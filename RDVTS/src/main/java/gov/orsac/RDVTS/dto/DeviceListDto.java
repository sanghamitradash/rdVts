package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DeviceListDto {

    private Integer id;
    private Long imeiNo1;
    private Long simIccId1;
    private Long mobileNumber1;
    private Long imeiNo2;
    private Long simIccId2;
    private Long mobileNumber2;
    private Integer vtuVendorId;

    private Integer distId;
    private Integer blockId;
    private Integer divisionId;
    private Integer gDistId;
    private Integer gBlockId;

    private Integer userId;

    private Integer offSet;
    private Integer limit;
    private Integer draw;
}
