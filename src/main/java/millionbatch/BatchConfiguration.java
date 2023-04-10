package millionbatch;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfiguration {
	
	@Autowired
	private DataSource datasource;
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public JobCompletionNotificationListener listener;
	
	@Bean
	public JdbcPagingItemReader<Person> createReader() {
		JdbcPagingItemReader<Person> reader = new JdbcPagingItemReader<Person>();
		final PersonMapper personMapper = new PersonMapper();
		reader.setDataSource(datasource);
		reader.setFetchSize(1000);
		reader.setPageSize(1000);
		reader.setRowMapper(personMapper);
		reader.setQueryProvider(createQuery());
		return reader;
	}
	
	private MySqlPagingQueryProvider createQuery() {
		final Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("id", Order.ASCENDING);
		final MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, name");
		queryProvider.setFromClause("from arjootan.person");
		queryProvider.setSortKeys(sortKeys);
		return queryProvider;
	}
	
	@Bean
	public PersonItemProcessor createProcessor() {
		return new PersonItemProcessor();
	}
/*	
	@Bean 
	JdbcBatchItemWriter<Person> createWriter(DataSource dataSource) {
		  return new JdbcBatchItemWriterBuilder<Person>()
		    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
		    .sql("INSERT INTO arjootan.people (id, name) VALUES (:id, :firstName)")
		    .dataSource(dataSource)
		    .build(); 
    }
    */
	
	@Bean
	MessageWriter createWriter() {
		return new MessageWriter();
	}
	
	
	@Bean
	public Job importUserJob() {
	  return jobBuilderFactory.get("importUserJob")
	    .incrementer(new RunIdIncrementer())
	    .preventRestart()
	    .listener(listener)
	    .flow(createStep())
	    .end()
	    .build();
	}

	@Bean
	public Step createStep() {
	  return stepBuilderFactory.get("step1")
	    .<Person, Person> chunk(1000)
	    .reader(createReader())
	    .processor(createProcessor())
	    .writer(createWriter())
	    .build();
	}	

}
