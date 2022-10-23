package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.WorkEntity;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/work")
public class WorkController {
    @Autowired
    private WorkService workService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ContractorService contractorService;

    @Autowired
    private LocationService locationService;


    @Autowired
    private DeviceService deviceService;

    //Work Master
    @PostMapping("/addWork")
    public RDVTSResponse addWork(@RequestBody WorkEntity work) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            WorkEntity workEntity = workService.addWork(work);

            result.put("workEntity", workEntity);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getWorkList")
    public RDVTSListResponse getWorkList(@RequestParam(name = "id", required = false) Integer id,
                                         @RequestParam(name = "userId", required = false) Integer userId,
                                         @RequestParam(name = "activityId", required = false) Integer activityId,
                                         @RequestParam(name = "start") Integer start,
                                         @RequestParam(name = "length") Integer length,
                                         @RequestParam(name = "draw") Integer draw) {
        WorkDto workDto = new WorkDto();
        workDto.setId(id);
        workDto.setUserId(userId);
        workDto.setActivityId(activityId);
        workDto.setOffSet(start);
        workDto.setLimit(length);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<WorkDto> workDtoPage = workService.getWorkList(workDto);
            List<WorkDto> workDtoList = workDtoPage.getContent();
            Integer start1 = start;
            for (int i = 0; i < workDtoList.size(); i++) {
                start1 = start1 + 1;
                workDtoList.get(i).setSlNo(start1);
            }
            result.put("WorkDtoList", workDtoList);
            response.setData(workDtoList);
            response.setMessage("List of Work.");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(workDtoPage.getTotalElements());
            response.setRecordsTotal(workDtoPage.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSListResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getWorkById")
    public RDVTSResponse getWorkById(@RequestParam int id, @RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<WorkDto> workDto = workService.getWorkById(id);
            List<VehicleMasterDto> vehicle = vehicleService.getVehicleHistoryList(id);
            List<LocationDto> location = vehicleService.getLocationArray(id);
            List<AlertDto> alertDtoList = vehicleService.getAlertArray(id);
            List<RoadMasterDto> roadMasterDtoList = vehicleService.getRoadArray(id);
            List<ContractorDto> contractorDtoList = contractorService.getContractorByWorkId(id);

            List<Map<String, Object>> resultlocation = new ArrayList<>();
            Date startDate = null;
            Date endDate = null;
            Date vehicleStartDate = null;
            Date vehicleendDate = null;
            startDate = null;
            endDate = null;
            ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
            Map<String, Object> coordinates = new HashMap<>();


            List<Integer> workids = new ArrayList<>();
            Double totalDistance = 0.0;
            workids.add(id);
            for (Integer workitem : workids) {
                List<ActivityDto> activityDtoList = workService.getActivityByWorkId(workitem);
                for (ActivityDto activityId : activityDtoList) {
                    List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId);
                    for (VehicleActivityMappingDto vehicleList : veActMapDto) {

                        List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime());
                        for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                            List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                            //int i = 0;
                            for (DeviceDto imei : getImeiList) {

                                totalDistance += locationService.getDistance(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());

     //                        List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
//                                // i++;
//                                for (VtuLocationDto vtuobj : vtuLocationDto) {
//                                    ArrayList<String> inner = new ArrayList<String>();
//                                    inner.add(vtuobj.getLatitude());
//                                    inner.add(vtuobj.getLongitude());
//                                    outer.add(inner);
////                                    LocationLatLonDto latLonDtos= new LocationLatLonDto();
////                                    latLonDtos.setLatitude(vtuobj.getLatitude());
////                                    latLonDtos.setLongitude(vtuobj.getLongitude());
//                                }
//                                Map<String, Object> itemVal = new HashMap<>();
//                                itemVal.put("vehicleLocation", vtuLocationDto);
//                                resultlocation.add(itemVal);
                            }
                            // deviceIds.add(vehicleid.getDeviceId());

                        }
                    }

                }
            }
            coordinates.put("coordinates", outer);
            coordinates.put("type", outer);
            // outer.add(inner);
            // System.out.println(outer);

            result.put("contractorDto", contractorDtoList);
            result.put("workDto", workDto);
            result.put("vehicleArray", vehicle);
            result.put("locationArray", totalDistance);
            result.put("roadArray", roadMasterDtoList);
            result.put("alertArray", alertDtoList);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;

    }

    @PostMapping("/getWorkDDById")
    public RDVTSResponse getWorkDDById(@RequestParam int id) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<WorkDto> workDto = workService.getWorkById(id);

            result.put("workDto", workDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;

    }

    @PostMapping("/updateWork")
    public RDVTSResponse updateDesignation(@RequestParam int id,
                                           @RequestParam(name = "data") String data) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            WorkDto updateWorkDto = mapper.readValue(data, WorkDto.class);
            WorkEntity updateWorkEntity = workService.updateWork(id, updateWorkDto);
            rdvtsResponse.setData(result);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            rdvtsResponse.setMessage("Work Updated Successfully");
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

    @PostMapping("/getActivityByWorkId")
    public RDVTSResponse getActivityByWorkId(@RequestParam int id) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityDto> activityDtoList = workService.getActivityByWorkId(id);
            result.put("activityDtoList", activityDtoList);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }
}
