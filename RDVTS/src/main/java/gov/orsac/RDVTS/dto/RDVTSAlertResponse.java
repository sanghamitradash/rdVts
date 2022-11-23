package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RDVTSAlertResponse {
    private int status;
    private ResponseEntity statusCode;
    private String message;
    public Object data ;
    private Integer draw;

    private Long workRecordsFiltered;
    private Long workRecordsTotal;

    private Long vehicleRecordsFiltered;
    private Long vehicleRecordsTotal;

    private Long roadRecordsFiltered;
    private Long roadRecordsTotal;

    public <T> RDVTSAlertResponse(int i, ResponseEntity<T> tResponseEntity, String message, Map<String, Object> result) {
    }
}
