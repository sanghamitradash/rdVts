package gov.orsac.RDVTS.controller;

import gov.orsac.RDVTS.dto.GeoSadakResponseObjDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/geoSadak")

public class GeoSadakController {
    @GetMapping("/vtscdac/{id}")
    public GeoSadakResponseObjDto FetchAllVTS(@PathVariable("id") Integer id) {
        String uri="http://localhost:8080/vtscdac/"+id;
        GeoSadakResponseObjDto roj=new GeoSadakResponseObjDto();
        try{
            RestTemplate rst=new RestTemplate();
            Object[] alldata = rst.getForObject(uri, Object[].class);
            System.out.println(alldata);
            if(alldata.length==0) {
                roj.setMsg("zero");
            }
            else {
                roj.setList(Arrays.asList(alldata));
                roj.setMsg("successfull");
            }
            System.out.println(roj.toString());
            return roj;
        }
        catch(Exception ex){
            roj.setMsg("failed "+ex.getMessage());
            return roj;
        }
    }
}
