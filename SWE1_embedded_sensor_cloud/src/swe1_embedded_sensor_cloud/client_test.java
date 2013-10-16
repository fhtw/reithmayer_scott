/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swe1_embedded_sensor_cloud;


import java.io.*;
import java.net.*;
import java.security.*;

/**
 *
 * @author if12b061
 */
public class client_test {
    public static void main(String[] args) throws IOException {
//    Socket c = new Socket("127.0.0.1",4444);

    Socket echoSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    try {
            echoSocket = new Socket("127.0.0.1",4444);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: taranis.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: taranis.");
            System.exit(1);
        }

    BufferedReader stdIn = new BufferedReader(
                                   new InputStreamReader(System.in));
	String userInput;

	while ((userInput = stdIn.readLine()) != null) {
	    out.println(userInput);
	    System.out.println("echo: " + in.readLine());
	}

	out.close();
	in.close();
	stdIn.close();
	echoSocket.close();

}
}
