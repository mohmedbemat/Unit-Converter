import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class connectFrontEnd {
    public static void main(String[] args) throws IOException {
        //Creates an HttpServer on localhost:5500 that bounds to the InetSocketAddress
        HttpServer server = HttpServer.create(new InetSocketAddress(5500), 0);
        server.createContext("/", new FileHandler("index.html")); // Creates a Http context for index.html
        server.createContext("/convert", new ConvertHandler()); //sends user submission to ConverterLogic to perform conversion
        server.createContext("/siUnits.png", new FileHandler("siUnits.png")); //Creates a Http context for image.png
        server.createContext("/style.css", new FileHandler("style.css")); // Creates a Http context for style.css
        server.setExecutor(null);
        System.out.println("http://localhost:5500"); // when user compiles the backend, the address will print
        server.start(); //starts the server
    }

    static class FileHandler implements HttpHandler {
        //connects index.html and style.css
        private final String filePath;
        public FileHandler(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File(filePath);
            //Checks if file exists. If not then,
            if (!file.exists()) {
                //HTTP 404 error: page not found
                String error = "404. That's an error"; //Outpts error
                exchange.sendResponseHeaders(404, error.length());
                OutputStream os = exchange.getResponseBody();
                os.write(error.getBytes());
                os.close();
                return;
            }

            // MIME: Multipurpose Internet Mail Extensions
            // Will tell server the type of data that user is inputting
            String contentType = Type(filePath);
            exchange.getResponseHeaders().set("Content-Type", contentType);

            //will convert html, css, and image to bytes.
            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }

        // type of file
        private String Type(String path) {
            if (path.endsWith(".html")) {
                return "text/html";
            }
            if (path.endsWith(".png")) {
                return "image/png";
            }
            if (path.endsWith(".css")) {
                return "text/css";
            }
            if (path.endsWith(".js")) {
                return "application/javascript";
            }
            return "application/octet-stream";
        }
    }

    // When user clicks "convert"
    static class ConvertHandler implements HttpHandler {
        @Override
        //To read line by line:
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) { // data from user
                BufferedReader reader = new BufferedReader( //Reads character input from user line by line
                //UTF-8: encoding: unicode is translated into binary
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)); // reads input stream as text
                StringBuilder Data = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) { //to read all lines
                    Data.append(line); //reads each line
                }

                //map with key value pairs
                Map<String, String> userSelection = parseFormData(Data.toString());

                String category = userSelection.get("category"); //gets category of conversion
                String value = userSelection.get("value"); //gets value of conversion
                String originalUnit = userSelection.get("originalUnit"); //gets origianl unit
                String newUnit = userSelection.get("newUnit"); // gets new unit

                String result = ConverterLogic.convert(category, originalUnit, newUnit, value); //does the conversion

                //will output value + original unit = new value + new unit
                // click back to go back to main page
                String answer = "<html><body>" + "<h1>Result</h1>" + "<p><strong>" + value + " " + originalUnit + " = " + result + " " + newUnit + "</strong></p>" + "<a href='/'>Back</a>" + "</body></html>";

                //string to byte array UTF-8
                byte[] responseBytes = answer.getBytes(StandardCharsets.UTF_8);
                //HTTP 200: successfull response status
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody(); //byte array sent to browser
                os.write(responseBytes);
                os.close(); // close output stream
            }
        }

        //string to map
        //organizes data that the user inputs by category, value, orignal unit, and new unit
        private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
            Map<String, String> map = new HashMap<>(); // empty map to store key-value pairs
            String[] keyValuePairs = formData.split("&"); // splits string
            for (String pairString : keyValuePairs) {
                String[] formFields = pairString.split("=");
                if (formFields.length == 2) {
                    map.put(URLDecoder.decode(formFields[0], "UTF-8"), URLDecoder.decode(formFields[1], "UTF-8"));
                }
            }
            return map;
        }
    }
}
