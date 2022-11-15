package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VtuLocationDto {

    private Integer contractorId;
    private Integer roadId;
    private Integer workId;
    private Integer vehicleId;
    private Integer deviceId;
    private String vehicleNo;


    private Integer id;
    private String firmwareVersion;

    private String packetType;


    private String alertId;

    private String packetStatus;


    private Long imei;

    private String vehicleReg;

    private String gpsFix;

    private Date dateTime;

    private String latitude;

    private String latitudeDir;

    private String longitude;

    private String longitudeDir;

    private String accuracy;

    private String speed;

    private String heading;


    private String noOfSatellites;

    private String altitude;

    private String pdop;

    private String hdop;
    private String networkOperatorName;
    private String ignition;
    private String mainPowerStatus;
    private String mainInputVoltage;
    private String internalBatteryVoltage;
    private String emergencyStatus;
    private String tamperAlert;
    private String gsmSignalStrength;
    private String mcc;
    private String mnc;
    private String lac;
    private String cellId;
    private String lac1;
    private String cellId1;
    private String cellIdSig1;
    private String lac2;
    private String cellId2;
    private String cellIdSig2;
    private String lac3;
    private String cellId3;
    private String cellIdSig3;
    private String lac4;
    private String cellId4;
    private String cellIdSig4;
    private String digitalInput1;
    private String digitalInput2;
    private String digitalInput3;
    private String digitalInput4;
    private String digitalOutput1;
    private String digitalOutput2;
    private String frameNumber;
    private String checksum;
    private String odoMeter;
    private String geofenceId;
    private Boolean isActive;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;



//    private Long UpdatedLatitude;
//    private Long UpdatedLongitude;
}
