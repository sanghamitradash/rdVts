package gov.orsac.RDVTS.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "vtu_location")
public class VtuLocationEntity {

    @Id
    @SequenceGenerator(name = "vtu_main_protocol", sequenceName = "vtu_main_protocol_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vtu_main_protocol")

    @Column(name = "id")
    private Integer id;

    @Column(name="firmware_version")
    private String firmwareVersion;

    @Column(name = "packet_type")
    private String packetType;

    @Column(name="alert_id")
    private String alertId;

    @Column(name="packet_status")
    private String packetStatus;

    @Column(name="imei")
    private Long imei;

    @Column(name="vehicle_reg")
    private String vehicleReg;

    @Column(name="gps_fix")
    private String gpsFix;

    @Column(name="date_time")
    private Date dateTime;

    @Column(name="latitude")
    private String latitude;

    @Column(name="latitude_dir")
    private String latitudeDir;
    @Column(name="longitude")
    private String longitude;
    @Column(name="longitude_dir")
    private String longitudeDir;
    @Column(name="speed")
    private String speed;
    @Column(name="heading")
    private String heading;

    @Column(name="no_of_satellites")
    private String noOfSatellites;
    @Column(name="altitude")
    private String altitude;
    @Column(name="pdop")
    private String pdop;
    @Column(name="hdop")
    private String hdop;
    @Column(name="network_operator_name")
    private String networkOperatorName;
    @Column(name="ignition")
    private String ignition;
    @Column(name="main_power_status")
    private String mainPowerStatus;
    @Column(name="main_input_voltage")
    private String mainInputVoltage;
    @Column(name="internal_battery_voltage")
    private String internalBatteryVoltage;
    @Column(name="emergency_status")
    private String emergencyStatus;
    @Column(name="tamper_alert")
    private String tamperAlert;
    @Column(name="gsm_signal_strength")
    private String gsmSignalStrength;
    @Column(name="mcc")
    private String mcc;
    @Column(name="mnc")
    private String mnc;
    @Column(name="lac")
    private String lac;
    @Column(name="cell_id")
    private String cellId;
    @Column(name="lac1")
    private String lac1;
    @Column(name="cell_id1")
    private String cellId1;

    @Column(name="cell_id_sig1")
    private String cellIdSig1;
    @Column(name="lac2")
    private String lac2;
    @Column(name="cell_id2")
    private String cellId2;
    @Column(name="cell_id_sig2")
    private String cellIdSig2;
    @Column(name="lac3")
    private String lac3;
    @Column(name="cell_id3")
    private String cellId3;
    @Column(name="cell_id_sig3")
    private String cellIdSig3;
    @Column(name="lac4")
    private String lac4;
    @Column(name="cell_id4")
    private String cellId4;
    @Column(name="cell_id_sig4")
    private String cellIdSig4;
    @Column(name="digital_input1")
    private String digitalInput1;
    @Column(name="digital_input2")
    private String digitalInput2;
    @Column(name="digital_input3")
    private String digitalInput3;
    @Column(name="digital_input4")
    private String digitalInput4;
    @Column(name="digital_output_1")
    private String digitalOutput1;
    @Column(name="digital_output_2")
    private String digitalOutput2;

    @Column(name="frame_number")
    private String frameNumber;

    @Column(name="checksum")
    private String checksum;
    @Column(name="odo_meter")
    private String odoMeter;
    @Column(name="geofence_id")
    private String geofenceId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_by")
    private Integer createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    @UpdateTimestamp
    private Date updatedOn;






}
