package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.repository.ContractorRepository;
import gov.orsac.RDVTS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractorRepositoryImpl implements ContractorRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    @Autowired
    private UserService userService;

    public int count(String qryStr, MapSqlParameterSource sqlParam) {
        String sqlStr = "SELECT COUNT(*) from (" + qryStr + ") as t";
        Integer intRes = namedJdbc.queryForObject(sqlStr, sqlParam, Integer.class);
        if (null != intRes) {
            return intRes;
        }
        return 0;
    }

   /* public Page<ContractorDto> getContractorDetails(ContractorDto contractorDto) {
        UserDto userDto = new UserDto();
        userDto.setId(contractorDto.getUserId());


        PageRequest pageable = PageRequest.of(contractorDto.getPage(), contractorDto.getSize(), Sort.Direction.fromString(contractorDto.getSortOrder()), contractorDto.getSortBy());
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer  resultCount = 0;
        String queryString = " ";


        queryString = "SELECT cm.id,cm.name,cm.mobile,cm.address,cm.g_contractor_id,cm.created_by,cm.created_on,um.id as userId, cm.updated_by,cm.updated_on from rdvts_oltp.contractor_m as cm  " +
                       "left join rdvts_oltp.user_m as um on um.contractor_id = cm.id  " +
                       "where cm.is_active = true " ;

        if(contractorDto.getId() != null && contractorDto.getId() > 0){
            queryString += " AND cm.id=:id ";
            sqlParam.addValue("id", contractorDto.getId());
        }

        if(contractorDto.getUserId() != null && contractorDto.getUserId() > 0){
            queryString += " AND um.id=:userId ";
            sqlParam.addValue("userId", contractorDto.getUserId());
        }

        if(contractorDto.getGContractorId() != null && contractorDto.getGContractorId() > 0){
            queryString += " AND cm.g_contractor_id=:gContractorId ";
            sqlParam.addValue("gContractorId", contractorDto.getGContractorId());
        }

        queryString += " ORDER BY " + order.getProperty() + " " + order.getDirection().name();
        resultCount = count(queryString, sqlParam);
        queryString += " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();

        List<ContractorDto> contractInfo = namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(ContractorDto.class));
        return new PageImpl<>(contractInfo,pageable,resultCount);

    }
*/
    public List<ContractorDto> getContractById(Integer contractId,Integer userId) {
        List<ContractorDto> contractor;
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT cm.id,cm.name,cm.mobile,cm.address,cm.g_contractor_id,cm.created_by,cm.created_on,cm.updated_by,cm.updated_on from rdvts_oltp.contractor_m as cm  " +
                     "WHERE cm.is_active=true ";

        if(contractId>0){
            qry+=" AND cm.id=:contractId";
        }
        sqlParam.addValue("contractId", contractId);
        sqlParam.addValue("userId",userId);
        try {
            contractor = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ContractorDto.class));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
        return contractor;
    }

    //Swarup
    public List<ContractorDto> getContractorByWorkId(Integer workId) {
        List<ContractorDto> contractor;
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT cm.id,cm.name,cm.mobile,cm.address,cm.g_contractor_id,cm.created_by,cm.created_on, cm.updated_by,cm.updated_on,gm.work_id from rdvts_oltp.contractor_m as cm  " +
                "left join rdvts_oltp.geo_master as gm on gm.contractor_id = cm.id and gm.is_active = true " +
                "where cm.is_active = true ";

        if(workId > 0){
            qry+=" AND gm.work_id= :workId";
        }
        sqlParam.addValue("workId", workId);

        try {
            contractor = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ContractorDto.class));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
        return contractor;
    }


    public Page<ContractorDto> getContractorDetails(ContractorFilterDto contractor) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
  /*      PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        pageable = PageRequest.of(contractor.getOffSet(),contractor.getLimit(), Sort.Direction.fromString("desc"), "id");
        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");*/

        int pageNo = contractor.getOffSet()/contractor.getLimit();
        PageRequest pageable = PageRequest.of(pageNo, contractor.getLimit(), Sort.Direction.fromString("desc"), "id");
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        int resultCount = 0;


        String qry = "SELECT cm.id,cm.name,cm.mobile,cm.address,cm.g_contractor_id,cm.created_by,cm.created_on, cm.updated_by,cm.updated_on from rdvts_oltp.contractor_m as cm  " +
                //"left join rdvts_oltp.user_m as um on um.contractor_id = cm.id  " +
                "where cm.is_active = true  ";


        if(contractor.getUserId() != null && contractor.getUserId() > 0){
            qry += " AND um.id=:userId ";
            sqlParam.addValue("userId", contractor.getUserId());
        }

        if(contractor.getGContractorId() != null && contractor.getGContractorId() > 0){
            qry += " AND cm.g_contractor_id=:gContractorId ";
            sqlParam.addValue("gContractorId", contractor.getGContractorId());
        }

        if (contractor.getMobile() != null && contractor.getMobile() > 0) {
            qry += " AND cm.mobile=:mobile ";
            sqlParam.addValue("mobile", contractor.getMobile());
        }

        if (contractor.getName() != null && contractor.getName().length() > 0) {
            qry += " AND cm.name=:name ";
            if (contractor.getName() != null) {
                qry += " AND cm.name LIKE(:name) ";
                sqlParam.addValue("name", contractor.getName());
            }
        }
        resultCount = count(qry, sqlParam);
        if (contractor.getLimit() > 0){
            qry += " LIMIT " +contractor.getLimit() + " OFFSET " + contractor.getOffSet();
        }
        List<ContractorDto> list=namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ContractorDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }

    @Override
    public List<ContractorDto> getContractorDropDown() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT cm.id,cm.name,cm.mobile,cm.g_contractor_id from rdvts_oltp.contractor_m as cm ";
        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(ContractorDto.class));
    }
}

