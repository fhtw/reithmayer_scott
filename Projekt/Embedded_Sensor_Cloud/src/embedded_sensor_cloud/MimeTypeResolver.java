/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package embedded_sensor_cloud;

import static embedded_sensor_cloud.MimeTypeResolver.types;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sam
 */
public class MimeTypeResolver 
{

	public static Map<String, String> types = new HashMap<String, String>();
	
	static{
		//text Types
		types.put("html","text/html");
		types.put("htm","text/html");
		
		types.put("txt", "text/plain");
		
		types.put("css", "text/css");
		
		//image Types
		types.put("jpg", "image/jpeg");
		types.put("jpeg", "image/jpeg");
		
		types.put("gif","image/gif");
		
		types.put("png", "image/png");
	}
}