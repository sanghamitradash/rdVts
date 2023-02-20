package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractorFilterDto {

    private Integer id;
    private Integer userId;
    private Integer gContractorId;

    private String name;
    private Long mobile;

    private Integer offSet;
    private Integer limit;
    private Integer draw;
}

