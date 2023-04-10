package millionbatch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PersonMapper implements RowMapper<Person>  {
	  @Override
	  public Person mapRow(final ResultSet rs, final int rowNum) throws SQLException {
	      Person person = new Person(); 
	      person.setId(rs.getInt("id"));
	      person.setFirstName(rs.getString("name"));
	      return person;
	  } 
	}