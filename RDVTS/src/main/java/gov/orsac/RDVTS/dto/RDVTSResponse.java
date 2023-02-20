package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RDVTSResponse {
        private int status;
        private ResponseEntity statusCode;
        private String message;
        public Object data ;

}