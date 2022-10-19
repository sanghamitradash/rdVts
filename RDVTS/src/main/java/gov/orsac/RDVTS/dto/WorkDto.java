package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDto {

    private Integer id;

    private Integer userId;

    private Integer workId;

    private String workName;

    private Boolean isActive=true;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    private Integer piuId;
    private String piuName;
    private Integer packageId;
    private String packageName;
    private String contractorId;



    private Integer offSet;
    private Integer limit;
    private Integer draw;
    private Integer slNo;

}
