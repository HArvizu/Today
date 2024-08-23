import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.*;
import java.awt.*;
import org.json.JSONObject;
import org.json.JSONArray;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Today 
{
    private static final Map<String, String> cityToTimeZoneMap = new HashMap<>();
    private static Timer timer;

    //List of Cities
    static 
    {
    	//Japan Cities
        cityToTimeZoneMap.put("Fukuoka", "Asia/Tokyo");
        cityToTimeZoneMap.put("Kawasaki", "Asia/Tokyo");
        cityToTimeZoneMap.put("Kobe", "Asia/Tokyo");
        cityToTimeZoneMap.put("Kyoto", "Asia/Tokyo");
        cityToTimeZoneMap.put("Nagoya", "Asia/Tokyo");
        cityToTimeZoneMap.put("Osaka", "Asia/Tokyo");
        cityToTimeZoneMap.put("Saitama", "Asia/Tokyo");
        cityToTimeZoneMap.put("Sapporo", "Asia/Tokyo");
        cityToTimeZoneMap.put("Tokyo", "Asia/Tokyo");
        cityToTimeZoneMap.put("Yokohama", "Asia/Tokyo");
        cityToTimeZoneMap.put("Okinawa", "Asia/Tokyo");
        
        //NorCal Cities
        cityToTimeZoneMap.put("Berkeley", "America/Los_Angeles");
        cityToTimeZoneMap.put("Chico", "America/Los_Angeles");
        cityToTimeZoneMap.put("Daly City", "America/Los_Angeles");
        cityToTimeZoneMap.put("Elk Grove", "America/Los_Angeles");
        cityToTimeZoneMap.put("Fairfield", "America/Los_Angeles");
        cityToTimeZoneMap.put("Fremont", "America/Los_Angeles");
        cityToTimeZoneMap.put("Fresno", "America/Los_Angeles");
        cityToTimeZoneMap.put("Hayward", "America/Los_Angeles");
        cityToTimeZoneMap.put("Modesto", "America/Los_Angeles");
        cityToTimeZoneMap.put("Napa", "America/Los_Angeles");
        cityToTimeZoneMap.put("Oakland", "America/Los_Angeles");
        cityToTimeZoneMap.put("Palo Alto", "America/Los_Angeles");
        cityToTimeZoneMap.put("Redding", "America/Los_Angeles");
        cityToTimeZoneMap.put("Roseville", "America/Los_Angeles");
        cityToTimeZoneMap.put("Sacramento", "America/Los_Angeles");
        cityToTimeZoneMap.put("San Francisco", "America/Los_Angeles");
        cityToTimeZoneMap.put("San Jose", "America/Los_Angeles");
        cityToTimeZoneMap.put("Santa Clara", "America/Los_Angeles");
        cityToTimeZoneMap.put("Santa Rosa", "America/Los_Angeles");
        cityToTimeZoneMap.put("Stockton", "America/Los_Angeles");
        cityToTimeZoneMap.put("Sunnyvale", "America/Los_Angeles");
        cityToTimeZoneMap.put("Vallejo", "America/Los_Angeles");
        
        //SoCal Cities
        cityToTimeZoneMap.put("Anaheim", "America/Los_Angeles");
        cityToTimeZoneMap.put("Burbank", "America/Los_Angeles");
        cityToTimeZoneMap.put("Chino", "America/Los_Angeles");
        cityToTimeZoneMap.put("Corona", "America/Los_Angeles");
        cityToTimeZoneMap.put("Escondido", "America/Los_Angeles");
        cityToTimeZoneMap.put("Fullerton", "America/Los_Angeles");
        cityToTimeZoneMap.put("Glendale", "America/Los_Angeles");
        cityToTimeZoneMap.put("Hemet", "America/Los_Angeles");
        cityToTimeZoneMap.put("Irvine", "America/Los_Angeles");
        cityToTimeZoneMap.put("Long Beach", "America/Los_Angeles");
        cityToTimeZoneMap.put("Los Angeles", "America/Los_Angeles");
        cityToTimeZoneMap.put("Menifee", "America/Los_Angeles");
        cityToTimeZoneMap.put("Mission Viejo", "America/Los_Angeles");
        cityToTimeZoneMap.put("Murrieta", "America/Los_Angeles");
        cityToTimeZoneMap.put("Norwalk", "America/Los_Angeles");
        cityToTimeZoneMap.put("Oceanside", "America/Los_Angeles");
        cityToTimeZoneMap.put("Ontario", "America/Los_Angeles");
        cityToTimeZoneMap.put("Orange", "America/Los_Angeles");
        cityToTimeZoneMap.put("Palmdale", "America/Los_Angeles");
        cityToTimeZoneMap.put("Pasadena", "America/Los_Angeles");
        cityToTimeZoneMap.put("Pomona", "America/Los_Angeles");
        cityToTimeZoneMap.put("Riverside", "America/Los_Angeles");
        cityToTimeZoneMap.put("San Diego", "America/Los_Angeles");
        cityToTimeZoneMap.put("San Jose", "America/Los_Angeles");
        cityToTimeZoneMap.put("San Juan Capistrano", "America/Los_Angeles");
        cityToTimeZoneMap.put("Santa Ana", "America/Los_Angeles");
        cityToTimeZoneMap.put("Temecula", "America/Los_Angeles");
        cityToTimeZoneMap.put("Thousand Oaks", "America/Los_Angeles");
        cityToTimeZoneMap.put("Vista", "America/Los_Angeles");
        cityToTimeZoneMap.put("Whittier", "America/Los_Angeles");    	
        
        //US Cities
        cityToTimeZoneMap.put("Phoenix", "America/Phoenix");
        cityToTimeZoneMap.put("Chicago", "America/Chicago");
        cityToTimeZoneMap.put("Houston", "America/Chicago");
        cityToTimeZoneMap.put("San Antonio", "America/Chicago");
        cityToTimeZoneMap.put("Dallas", "America/Chicago");        
        cityToTimeZoneMap.put("New York", "America/New_York");
        cityToTimeZoneMap.put("Philadelphia", "America/New_York");
    	
        //World Cities
        cityToTimeZoneMap.put("London", "Europe/London");
        cityToTimeZoneMap.put("Rome", "Europe/Rome");
        cityToTimeZoneMap.put("Paris", "Europe/Paris");
        cityToTimeZoneMap.put("Sydney", "Australia/Sydney");  
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Weather Information");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setPreferredSize(new Dimension(350, 150)); // Adjust the size as needed

            JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel locationLabel = new JLabel("City:");
            JTextField cityField = new JTextField();
            cityField.setPreferredSize(new Dimension(150, 24)); // Set preferred size for the text field
            JWindow suggestionWindow = new JWindow(frame);

            inputPanel.add(locationLabel);
            inputPanel.add(cityField);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            JLabel dateTimeLabel = new JLabel("Date and Time:");
            JLabel weatherLabel = new JLabel("Weather: Fetching...");

            infoPanel.add(dateTimeLabel);
            infoPanel.add(weatherLabel);

            mainPanel.add(inputPanel, BorderLayout.NORTH);
            mainPanel.add(infoPanel, BorderLayout.CENTER);

            JButton fullDataButton = new JButton("Full Data");
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(fullDataButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            JScrollPane scrollPane = new JScrollPane(mainPanel);
            frame.add(scrollPane);
            frame.pack(); // Ensure the frame packs its components
            frame.setVisible(true);

            JPanel fullDataPanel = new JPanel();
            fullDataPanel.setLayout(new BoxLayout(fullDataPanel, BoxLayout.Y_AXIS));
            JLabel fullDataLabel = new JLabel("Full Data: Fetching...");
            fullDataPanel.add(fullDataLabel);
            JButton backButton = new JButton("Back");
            fullDataPanel.add(backButton);

            fullDataButton.addActionListener(e -> 
            {
                frame.getContentPane().removeAll();
                frame.add(new JScrollPane(fullDataPanel));
                frame.revalidate();
                frame.repaint();
            });

            backButton.addActionListener(e -> 
            {
                frame.getContentPane().removeAll();
                frame.add(new JScrollPane(mainPanel));
                frame.revalidate();
                frame.repaint();
            });

            cityField.getDocument().addDocumentListener(new DocumentListener() 
            {
                @Override
                public void insertUpdate(DocumentEvent e) 
                {
                    showSuggestions();
                }

                @Override
                public void removeUpdate(DocumentEvent e) 
                {
                    showSuggestions();
                }

                @Override
                public void changedUpdate(DocumentEvent e) 
                {
                    showSuggestions();
                }

                private void showSuggestions() 
                {
                    String text = cityField.getText();
                    suggestionWindow.getContentPane().removeAll();
                    if (!text.isEmpty()) 
                    {
                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.setBackground(Color.WHITE); // Set background color to white
                        for (String city : cityToTimeZoneMap.keySet()) 
                        {
                            if (city.toLowerCase().startsWith(text.toLowerCase())) 
                            {
                                JLabel item = new JLabel(city);
                                item.addMouseListener(new MouseAdapter() 
                                {
                                    @Override
                                    public void mouseClicked(MouseEvent e) 
                                    {
                                        cityField.setText(city);
                                        fetchWeatherData(city, weatherLabel, fullDataLabel, dateTimeLabel);
                                        suggestionWindow.setVisible(false);
                                    }
                                });
                                panel.add(item);
                            }
                        }
                        suggestionWindow.getContentPane().add(panel);
                        suggestionWindow.pack();
                        suggestionWindow.setLocation(cityField.getLocationOnScreen().x, cityField.getLocationOnScreen().y + cityField.getHeight());
                        suggestionWindow.setVisible(true);
                    } 
                    else 
                    {
                        suggestionWindow.setVisible(false);
                    }
                }
            });

            cityField.addMouseListener(new MouseAdapter() 
            {
                @Override
                public void mouseClicked(MouseEvent e) 
                {
                    if (suggestionWindow.isVisible()) 
                    {
                        suggestionWindow.setVisible(false);
                    }
                }
            });

            cityField.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    String city = cityField.getText();
                    if (cityToTimeZoneMap.containsKey(city)) 
                    {
                        fetchWeatherData(city, weatherLabel, fullDataLabel, dateTimeLabel);
                        suggestionWindow.setVisible(false);
                    } 
                    else 
                    {
                        weatherLabel.setText("Weather: City not found in the list.");
                        fullDataLabel.setText("Full Data: City not found in the list.");
                        dateTimeLabel.setText("Local Time Not Available");
                    }
                }
            });
        });
    }

    private static void fetchWeatherData(String cityName, JLabel weatherLabel, JLabel fullDataLabel, JLabel dateTimeLabel) {
        new Thread(() -> 
        {
            try {
                String apiKey = "INSERT API HERE";
                String urlString = "INSERT URL HERE" + cityName.replace(" ", "%20") + "&apikey=" + apiKey;

                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) 
                {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response
                JSONObject json = new JSONObject(response.toString());

                // Check if the response contains an error
                if (json.has("error")) 
                {
                    throw new Exception("City not found");
                }

                JSONArray timelines = json.getJSONObject("timelines").getJSONArray("daily");
                JSONObject today = timelines.getJSONObject(0).getJSONObject("values");

                double cloudCoverAvg = today.getDouble("cloudCoverAvg");
                double temperatureMaxCelsius = today.getDouble("temperatureMax");
                double windSpeedMaxKmh = today.getDouble("windSpeedMax");
                double humidityMax = today.getDouble("humidityMax");

                // Convert temperature to Fahrenheit
                double temperatureMaxFahrenheit = (temperatureMaxCelsius * 9/5) + 32;
                // Convert wind speed to miles per hour
                double windSpeedMaxMph = windSpeedMaxKmh * 0.621371;

                // Round to the nearest tenth
                final double[] roundedValues = new double[4];
                roundedValues[0] = Math.round(cloudCoverAvg * 10) / 10.0;
                roundedValues[1] = Math.round(temperatureMaxFahrenheit * 10) / 10.0;
                roundedValues[2] = Math.round(windSpeedMaxMph * 10) / 10.0;
                roundedValues[3] = Math.round(humidityMax * 10) / 10.0;

                // Get the time zone of the city
                String timeZoneId = cityToTimeZoneMap.get(cityName);
                if (timeZoneId != null) 
                {
                    ZoneId zoneId = ZoneId.of(timeZoneId);
                    ZonedDateTime now = ZonedDateTime.now(zoneId);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm:ss a");
                    String formattedDateTime = now.format(formatter);
                    SwingUtilities.invokeLater(() -> dateTimeLabel.setText("Date and Time: " + formattedDateTime));
                    startClock(cityName, dateTimeLabel);
                } else 
                {
                    SwingUtilities.invokeLater(() -> 
                    {
                        dateTimeLabel.setText("Local Time Not Available");
                        stopClock();
                    });
                }

                // Update the weather label with the fetched data
                SwingUtilities.invokeLater(() -> 
                {
                    weatherLabel.setText("<html>Weather:<br>Cloud Cover: " + roundedValues[0] + "%<br>Temperature: " + roundedValues[1] + "°F<br>Wind Speed: " + roundedValues[2] + " mph<br>Humidity: " + roundedValues[3] + "%</html>");
                    fullDataLabel.setText("<html>Full Data:<br>" + json.toString(4).replace("\n", "<br>").replace(" ", " ") + "</html>");
                });
            } catch (Exception e) 
            {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> 
                {
                    weatherLabel.setText("Weather: Error fetching data for the specified city. Please check the city name and try again.");
                    fullDataLabel.setText("Full Data: Error fetching data for the specified city.");
                    dateTimeLabel.setText("Local Time Not Available");
                    stopClock();
                });
            }
        }).start();
    }

    private static void startClock(String cityName, JLabel dateTimeLabel) 
    {
        if (timer != null) 
        {
            timer.stop();
        }
        if (cityToTimeZoneMap.containsKey(cityName)) 
        {
            timer = new Timer(1000, e -> updateDateTime(cityName, dateTimeLabel));
            timer.start();
        } 
        else 
        {
            dateTimeLabel.setText("Local Time Not Available");
        }
    }

    private static void stopClock() 
    {
        if (timer != null) 
        {
            timer.stop();
            timer = null;
        }
    }

    private static void updateDateTime(String cityName, JLabel dateTimeLabel) 
    {
        String timeZoneId = cityToTimeZoneMap.getOrDefault(cityName, "UTC");
        ZoneId zoneId = ZoneId.of(timeZoneId);
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm:ss a");
        String formattedDateTime = now.format(formatter);
        SwingUtilities.invokeLater(() -> dateTimeLabel.setText("Date and Time: " + formattedDateTime));
    }
}