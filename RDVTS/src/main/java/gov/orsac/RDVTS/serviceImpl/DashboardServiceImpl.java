package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.ActiveAndInactiveVehicleDto;
import gov.orsac.RDVTS.dto.CompletedAndNotCompletedWorkDto;
import gov.orsac.RDVTS.dto.DistrictWiseVehicleDto;
import gov.orsac.RDVTS.repository.DashboardRepository;
import gov.orsac.RDVTS.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;
    @Override
    public ActiveAndInactiveVehicleDto getActiveAndInactiveVehicle() {
        Integer totalVehicle=dashboardRepository.getTotalVehicle();
        Integer totalActive=dashboardRepository.getTotalActive();
        Integer totalInactive=totalVehicle-totalActive;
        Double activePercentage= (Double.valueOf(totalActive)/Double.valueOf(totalVehicle))*100;
        Double inActivePercentage= (Double.valueOf(totalInactive)/Double.valueOf(totalVehicle))*100;
        ActiveAndInactiveVehicleDto vehicle=new ActiveAndInactiveVehicleDto();
        vehicle.setActiveCount(totalActive);
        vehicle.setInActiveCount(totalInactive);
        vehicle.setActivePercentage(activePercentage);
        vehicle.setInActivePercentage(inActivePercentage);
        vehicle.setTotalVehicle(totalVehicle);
        return vehicle;
    }

    @Override
    public CompletedAndNotCompletedWorkDto getStatusWiseWorkCount() {
        Integer totalWork=dashboardRepository.getTotalWork();
        Integer totalCompleted=dashboardRepository.getCompletedWork();
        Integer totalIncomplete=totalWork-totalCompleted;
        Double completedPercentage= (Double.valueOf(totalCompleted)/Double.valueOf(totalWork))*100;
        Double inCompletedPercentage= (Double.valueOf(totalIncomplete)/Double.valueOf(totalWork))*100;
        CompletedAndNotCompletedWorkDto work=new CompletedAndNotCompletedWorkDto();
        work.setTotalWork(totalWork);
        work.setTotalCompletedWork(totalCompleted);
        work.setTotalInCompletedWork(totalIncomplete);
        work.setCompletedPercentage(completedPercentage);
        work.setInCompletedPercentage(inCompletedPercentage);
        return work;
    }

    @Override
    public List<DistrictWiseVehicleDto> getDistrictWiseVehicleCount() {
        List<DistrictWiseVehicleDto> count=new ArrayList<>();
        for(int i=0;i<5;i++){
            DistrictWiseVehicleDto count1=new DistrictWiseVehicleDto();
            if(i==0){
              count1.setDistrictId(1);
              count1.setDistrictName("Baleswar");
              count1.setVehicleCount(5);
            }
            if(i==1){
                count1.setDistrictId(1);
                count1.setDistrictName("Bolangir");
                count1.setVehicleCount(10);
            }
            if(i==2){
                count1.setDistrictId(1);
                count1.setDistrictName("Cuttack");
                count1.setVehicleCount(4);
            }
            if(i==3){
                count1.setDistrictId(1);
                count1.setDistrictName("Dhenkanala");
                count1.setVehicleCount(6);
            }
            if(i==4){
                count1.setDistrictId(1);
                count1.setDistrictName("Khurda");
                count1.setVehicleCount(8);
            }
           count.add(count1);
        }
        return count;
    }
}
