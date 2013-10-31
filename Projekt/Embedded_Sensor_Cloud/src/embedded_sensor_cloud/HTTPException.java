/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package embedded_sensor_cloud;

/**
 *
 * @author Sam
 */

public class HTTPException extends Exception {

	private static final long serialVersionUID = 4002159455826665262L;
	private Status status;

	public HTTPException(Status status) 
	{
		super();
		this.status = status;
	}
	
	public String getMessage()
	{
		String ret = "";
		ret += "HTTP 1.1 " + status.number + "\n\n";
		ret += "<html><body><h2>HTTP STATUS: " + status.name() + " (" + status.number+")</h2></body></html>";
		return ret;
	}
	
}