package Abgabe;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import java.util.StringTokenizer;

/**
 *
 * @author if12b061 & if12b052
 */
public class Request {

    private final Socket server;
    private String method = "";

    Request(Socket server) {
        this.server = server;
    }

    public UrlClass readRequest() {
        int contentLength = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream(), "UTF-8"));

            String line = in.readLine();
//            System.out.println(line);
            if (line != null) {
                StringTokenizer tok = new StringTokenizer(line);
                this.method = tok.nextToken();
                UrlClass url = new UrlClass(tok.nextToken());
//                protocol = tok.nextToken();
//                System.out.println("Method: " + method);
//                System.out.println("URL: " + url.getUrl());
                if ("POST".equalsIgnoreCase(method)) {
                    while ((line = in.readLine()) != null) {
//                        System.out.println("post line: " + line);
                        if (line.startsWith("Content-Length: ")) {
                            contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
//                            System.out.println("content length: " + contentLength);
                        }
                        if (line.isEmpty()) {
                            char[] cbuf = new char[contentLength];
                            int read = in.read(cbuf, 0, contentLength);
                            if (read > -1) {
                                String paramStr = new String(cbuf);
//                                System.out.println(paramStr);
                                url.addParameters(paramStr);
                            }
                            break;
                        }
                    }
                }

                return url;

            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getMethod() {
        return this.method;
    }

}
