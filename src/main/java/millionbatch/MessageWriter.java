package millionbatch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageWriter implements ItemWriter<Person> {
	
	@Autowired
	JmsTemplate jmsTemplate;
	

	@Override
	public void write(List<? extends Person> items) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		String newJsonData = mapper.writeValueAsString(items);
		jmsTemplate.convertAndSend("person_queue", newJsonData);
		
	}
	
	

}
