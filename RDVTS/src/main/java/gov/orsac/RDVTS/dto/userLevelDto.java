package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userLevelDto {
    private Integer id;


        private Integer id;
        private String name;
        private boolean isactive;
        private Integer createdBy;
        private Date createdOn;
        private Integer updatedBy;
        private Date updatedOn;
}
