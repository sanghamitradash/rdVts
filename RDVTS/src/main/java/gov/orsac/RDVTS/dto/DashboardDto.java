package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {

        private Integer id;
        private Integer activeCount;
        private Integer inActiveCount;
        private Integer areaTypeId;
        private Integer areaId;
        private String districtName;
        private String divisionName;
        private String processTime;
        private String geom ;
}
