package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RDVTSListResponse {
    private int status;
    private ResponseEntity statusCode;
    private String message;
    public Object data ;
    private Long recordsFiltered;
    private Long recordsTotal;
    private Integer draw;

    public <T> RDVTSListResponse(int i, ResponseEntity<T> tResponseEntity, String message, Map<String, Object> result) {
    }

}
