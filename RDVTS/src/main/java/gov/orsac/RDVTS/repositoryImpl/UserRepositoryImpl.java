package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.UserDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl {


    @Value("${dbschema}")
    private String DBSCHEMA;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public Page<UserInfoDto> getUserList(UserDto userDto) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        if (userDto!=null){
            pageable = PageRequest.of(userDto.getPage(), userDto.getSize(), Sort.Direction.fromString(userDto.getSortOrder()), userDto.getSortBy());
            order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
        }
        //For Default Page Load



        // return new PageImpl<>(namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(UserInfo.class)));
//        List<UserInfo> userInfo=namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(UserInfo.class));
//        return new PageImpl<>(userInfo, pageable, resultCount);
        return null;
    }


}
