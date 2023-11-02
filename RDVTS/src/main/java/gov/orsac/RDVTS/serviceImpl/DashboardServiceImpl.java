package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.DashboardCronEntity;
import gov.orsac.RDVTS.entities.PackageMasterEntity;
import gov.orsac.RDVTS.repository.DashboardCron;
import gov.orsac.RDVTS.repository.DashboardRepository;
import gov.orsac.RDVTS.repositoryImpl.DashboardRepositoryImpl;
import gov.orsac.RDVTS.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {
    Calendar cal = Calendar.getInstance();
    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private DashboardCron dashboardCronRepository;
    @Autowired
    private DashboardRepositoryImpl dashboardRepositoryImpl;
    @Override
    public ActiveAndInactiveVehicleDto getActiveAndInactiveVehicle(Integer userId) {
        ActiveInactiveDto data=dashboardRepositoryImpl.getActiveInactiveVehicle(userId);
        Double activePercentage=(Double.valueOf(data.getActive())/Double.valueOf(data.getTotal()))*100;
        Double inActivePercentage=(Double.valueOf(data.getInActive())/Double.valueOf(data.getTotal()))*100;
        /*Integer totalVehicle=dashboardRepository.getTotalVehicle();
        Integer totalActive=dashboardRepository.getTotalActive();
        Integer totalInactive=totalVehicle-totalActive;
        Double activePercentage= (Double.valueOf(totalActive)/Double.valueOf(totalVehicle))*100;
        Double inActivePercentage= (Double.valueOf(totalInactive)/Double.valueOf(totalVehicle))*100;*/
        ActiveAndInactiveVehicleDto vehicle=new ActiveAndInactiveVehicleDto();
        vehicle.setActiveCount(data.getActive());
        vehicle.setInActiveCount(data.getInActive());
        vehicle.setActivePercentage(activePercentage);
        vehicle.setInActivePercentage(inActivePercentage);
        vehicle.setTotalVehicle(data.getTotal());
        return vehicle;
    }
    @Override
    public List<DashboardCronEntity> getActiveAndInactiveVehicleCron(Integer userId) {
        List<DashboardCronEntity> allData=new ArrayList<>();
        List<Integer> totalActiveIds = dashboardRepository.totalActiveIds();
        List<Integer> totalInActiveIds = dashboardRepository.totalInactiveIds();
        List<DistrictWiseVehicleDto> activeCount=dashboardRepository.getDistrictWiseVehicleCount(totalActiveIds,userId);
        List<DistrictWiseVehicleDto> inActiveCount=dashboardRepository.getDistrictWiseVehicleCount(totalInActiveIds,userId);
        Date date=new Date();
        for(int i=0;i<activeCount.size();i++) {
            DashboardCronEntity dw = dashboardRepositoryImpl.findDashBoardCronByAreaIdForDistrict(activeCount.get(i).getDistrictId(),1);
            dw.setActive(activeCount.get(i).getCount());
            dw.setInActive(inActiveCount.get(i).getCount());
            dw.setAreaId(activeCount.get(i).getDistrictId());
            dw.setAreaTypeId(1);
            dw.setProcessTime(date);
            allData.add(dw);
            dashboardCronRepository.save(dw);
        }
        List<DivisionWiseVehicleDto> activeCountDivisionWise=dashboardRepository.getDivisionWiseVehicleCount(totalActiveIds,userId);
        List<DivisionWiseVehicleDto> inActiveCountDivisionWise=dashboardRepository.getDivisionWiseVehicleCount(totalInActiveIds,userId);
        for(int i=0;i<activeCountDivisionWise.size();i++){
            DashboardCronEntity dw = dashboardRepositoryImpl.findDashBoardCronByAreaIdForDivision(activeCountDivisionWise.get(i).getDivId(),2);
            dw.setActive(activeCountDivisionWise.get(i).getCount());
            dw.setInActive(inActiveCountDivisionWise.get(i).getCount());
            dw.setAreaId(activeCountDivisionWise.get(i).getDivId());
            dw.setAreaTypeId(2);
            dw.setProcessTime(date);
            allData.add(dw);
            dashboardCronRepository.save(dw);
        }
        return allData;
    }


    @Override
    public CompletedAndNotCompletedWorkDto getStatusWiseWorkCount(Integer userId) {
//        Integer totalWork=dashboardRepository.getTotalWork();
//        Integer totalCompleted=dashboardRepository.getCompletedWork();
//        Integer totalIncomplete=totalWork-totalCompleted;
//        Double completedPercentage= (Double.valueOf(totalCompleted)/Double.valueOf(totalWork))*100;
//        Double inCompletedPercentage= (Double.valueOf(totalIncomplete)/Double.valueOf(totalWork))*100;
//        CompletedAndNotCompletedWorkDto work=new CompletedAndNotCompletedWorkDto();
//        work.setTotalWork(totalWork);
//        work.setTotalCompletedWork(totalCompleted);
//        work.setTotalInCompletedWork(totalIncomplete);
//        work.setCompletedPercentage(completedPercentage);
//        work.setInCompletedPercentage(inCompletedPercentage);
       // return work;

        Integer totalWork=dashboardRepositoryImpl.getPackageById();
        int totalIncomplete= dashboardRepositoryImpl.getPackageIncompled();
        Integer totalCompleted=totalWork-totalIncomplete;
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
    public CompletedAndNotCompletedRoadDto getStatusWiseRoadCount(Integer userId) {

        Integer totalRoadCount=dashboardRepositoryImpl.getTotalRoadCountById();
        int totalIncompleteRoad= dashboardRepositoryImpl.getRoadIncompleted();
        Integer totalRoadCompleted=totalRoadCount-totalIncompleteRoad;
        Double completedPercentage= (Double.valueOf(totalRoadCompleted)/Double.valueOf(totalRoadCount))*100;
        Double inCompletedPercentage= (Double.valueOf(totalIncompleteRoad)/Double.valueOf(totalRoadCount))*100;
        CompletedAndNotCompletedRoadDto road =new CompletedAndNotCompletedRoadDto();
        road.setTotalRoad(totalRoadCount);
        road.setTotalCompletedRoad(totalRoadCompleted);
        road.setTotalInCompletedRoad(totalIncompleteRoad);
        road.setCompletedPercentage(completedPercentage);
        road.setInCompletedPercentage(inCompletedPercentage);


        return road;
    }


    @Override
    public List<DistrictWiseVehicleDto> getDistrictWiseVehicleCount(Integer userId) {
        List<DistrictWiseVehicleDto> count = new ArrayList<>();
        List<DistrictWiseVehicleDto> finalCount=new ArrayList<>();
        List<DashboardDto> dashboardData = dashboardRepositoryImpl.getDistrictWiseDashboardData();
       /* List<Integer> totalActiveIds = dashboardRepository.totalActiveIds();
        List<Integer> totalInActiveIds = dashboardRepository.totalInactiveIds();
        *//* DistrictWiseVehicleDto count1=new DistrictWiseVehicleDto();*//*
         List<DistrictWiseVehicleDto> activeCount=dashboardRepository.getDistrictWiseVehicleCount(totalActiveIds,userId);
        List<DistrictWiseVehicleDto> inActiveCount=dashboardRepository.getDistrictWiseVehicleCount(totalInActiveIds,userId);*/
        for(int i=0;i<dashboardData.size();i++){
            DistrictWiseVehicleDto dw=new DistrictWiseVehicleDto();
            dw.setActive(dashboardData.get(i).getActiveCount());
            dw.setInActive(dashboardData.get(i).getInActiveCount());
            dw.setDistrictId(dashboardData.get(i).getAreaId());
            dw.setDistrictName(dashboardData.get(i).getDistrictName());
            dw.setProcessTime(dashboardData.get(i).getProcessTime());
            dw.setCount(dashboardData.get(i).getActiveCount() + dashboardData.get(i).getInActiveCount());
           // dw.setGeom(activeCount.get(i).getGeom());
            finalCount.add(dw);
        }

//        district.setInActive(totalInActiveIds);
        return finalCount;
    }

    @Override
    public List<DivisionWiseVehicleDto> getDivisionWiseVehicleCount(Integer userId) {
        List<DivisionWiseVehicleDto> count = new ArrayList<>();
        List<DivisionWiseVehicleDto> finalCount=new ArrayList<>();
        List<DashboardDto> dashboardData = dashboardRepositoryImpl.getDivisionWiseDashboardData();
       /* List<Integer> totalActiveIds = dashboardRepository.totalActiveIds();
        List<Integer> totalInActiveIds = dashboardRepository.totalInactiveIds();
        *//* DistrictWiseVehicleDto count1=new DistrictWiseVehicleDto();*//*
        List<DivisionWiseVehicleDto> activeCount=dashboardRepository.getDivisionWiseVehicleCount(totalActiveIds,userId);
        List<DivisionWiseVehicleDto> inActiveCount=dashboardRepository.getDivisionWiseVehicleCount(totalInActiveIds,userId);*/
        for(int i=0;i<dashboardData.size();i++){
            DivisionWiseVehicleDto dw=new DivisionWiseVehicleDto();
            dw.setActive(dashboardData.get(i).getActiveCount());
            dw.setInActive(dashboardData.get(i).getInActiveCount());
            dw.setDivId(dashboardData.get(i).getAreaId());
            dw.setDivName(dashboardData.get(i).getDivisionName());
            dw.setCount(dashboardData.get(i).getActiveCount() + dashboardData.get(i).getInActiveCount());
            // dw.setGeom(activeCount.get(i).getGeom());
            finalCount.add(dw);
        }

//        district.setInActive(totalInActiveIds);
        return finalCount;
    }

    @Override
    public List<DashboardDto> getDashboardData(Integer typeId) {
        List<DashboardDto> dashboardData=new ArrayList<>();
        if(typeId==1){
            dashboardData=dashboardRepositoryImpl.getDistrictWiseDashboardData();
        }
        if(typeId==2){
            dashboardData=dashboardRepositoryImpl.getDivisionWiseDashboardData();
        }
        return dashboardData;
    }

    public List<RoadLengthDto> getRoadLengthByDistIdOrPackageId(Integer distId,Integer packageId) {
        return dashboardRepositoryImpl.getRoadLengthByDistIdOrPackageId(distId,packageId);
    }
}



        //        for(int i=0;i<5;i++){
//            DistrictWiseVehicleDto count1=new DistrictWiseVehicleDto();
//            if(i==0){
//              count1.setDistrictId(1);
//              count1.setDistrictName("Baleswar");
//              count1.setVehicleCount(5);
//            }
//            if(i==1){
//                count1.setDistrictId(1);
//                count1.setDistrictName("Bolangir");
//                count1.setVehicleCount(10);
//            }
//            if(i==2){
//                count1.setDistrictId(1);
//                count1.setDistrictName("Cuttack");
//                count1.setVehicleCount(4);
//            }
//            if(i==3){
//                count1.setDistrictId(1);
//                count1.setDistrictName("Dhenkanala");
//                count1.setVehicleCount(6);
//            }
//            if(i==4){
//                count1.setDistrictId(1);
//                count1.setDistrictName("Khurda");
//                count1.setVehicleCount(8);
//            }
//           count.add(count1);
//        }
        //return count;


