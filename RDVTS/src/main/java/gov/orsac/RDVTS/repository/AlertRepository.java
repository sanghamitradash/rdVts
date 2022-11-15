package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlertRepository extends JpaRepository<AlertEntity,Integer> {

//    @Modifying
//    @Query("Update rdvts_oltp.alert_data al  SET is_resolve = true WHERE al.imei =:imei and al.alert_id_type =:noDataAlertId")
//    public void updateResolve(@Param("imei") Long imei1, @Param("noDataAlertId") Integer noDataAlertId);

}
