package smart.parking.cot.services;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;
import smart.parking.cot.Entity.ParkingSlot;
import smart.parking.cot.Ressources.ParkingAvailabiltyWebsocket;

import javax.net.ssl.SSLSocketFactory;

@Singleton
@Startup
public class MqttConnection {
    @Inject
    private ReservationService service;
    @PostConstruct
    public void start() {
        try {
            System.out.println("starting mqtt");

            //CLIENT CONNECTION OPTIONS
            MqttClient client = new MqttClient(
                    "wss://mqtt.smart-parking.me:8083", // serverURI in format: "protocol://name:port"
                    MqttClient.generateClientId(), // ClientId
                    new MemoryPersistence()); // Persistence

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setUserName("broker");
            mqttConnectOptions.setPassword("broker".toCharArray());
            mqttConnectOptions.setSocketFactory(SSLSocketFactory.getDefault());
            // using the default socket factory
            //mqttConnectOptions.setConnectionTimeout(1000);
            client.connect(mqttConnectOptions);


            client.setCallback(new MqttCallback() {

                @Override
                // Called when the client lost the connection to the broker
                public void connectionLost(Throwable cause) {
                    System.out.println("client lost connection " + cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                   // System.out.println(new String(message.getPayload()));
                   System.out.println(topic + "hello");
if (topic.equals("IRSensor")) {
    try {
        //System.out.println(topic + "::::: " + new String(message.getPayload()));
        System.out.println(new String(message.getPayload()));
        JSONObject obj = new JSONObject(new String(message.getPayload()));
        int avai =   obj.getInt("isAvailable");
        String id = obj.getString("id");
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setId(id);
        parkingSlot.setAvailable(avai);
        ParkingAvailabiltyWebsocket.broadcastMessage(parkingSlot);
    }
    catch (Exception e ) {
        System.out.println(e);
    }
}
                    else if (topic.equals("verification")) {
                        try {
                            //System.out.println(topic + "::::: " + new String(message.getPayload()));
                            JSONObject obj_1 = new JSONObject(new String(message.getPayload()));
                            String code = obj_1.getString("code");
                            System.out.println(code);
                         boolean result = service.check_reservation(code);
                        System.out.println(result);
                        }
                        catch (Exception e ) {
                            System.out.println(e);
                        }
                    }
                }


                @Override
                // Called when an outgoing publish is complete
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("delivery complete " + token);
                }
            });

            client.subscribe("verification", 1);
            client.subscribe("IRSensor", 1);
           // client.subscribe("verification", 1);
        } catch (MqttException e) {

        }
    }
        }




