package com.bizduo.toolbox.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
/**
 * Date转换
 * @author Administrator
 *
 */
public class CustomDateTimeSerializer  extends JsonSerializer<Date>{

	@Override
	public void serialize(Date value,JsonGenerator jgen,SerializerProvider provider) throws IOException,
			JsonProcessingException {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = formatter.format(value);
		jgen.writeString(formattedDate);
	} 
}
