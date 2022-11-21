package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BufferDto {
    private Integer deviceId;
    private Integer roadId;
    private String roadBuffer;
}
