/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package embedded_sensor_cloud;

/**
 *
 * @author Sam
 */
public	enum Status
{ 
		HTTP_OK(200), HTTP_INTERNAL_SERVER_ERROR(500), HTTP_NOT_FOUND(404);
		
		int number;
		
		Status(int n)
		{
			this.number=n;
		}
}
