package gov.orsac.RDVTS.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    private Integer id;
    private String name;
    private Integer parentId;
    private String module;
    private Integer menuOrder;
    private boolean isActive;
    private Integer createdBy;
    private Date createdOn ;
    private Integer updatedBy;
    private Date updatedOn;
    private String menuIcon;

}
