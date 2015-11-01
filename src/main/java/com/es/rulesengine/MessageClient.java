package com.es.rulesengine;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import org.json.*;

public class MessageClient implements MqttCallback {

	private String broker;
	private String clientID;
	private MemoryPersistence persistence;
	private MqttClient myClient;

	public MessageClient(String broker, String clientID) {
		this.clientID = clientID;
		this.broker = broker;
		persistence = new MemoryPersistence();
	}

	public void runClient() {

		MqttConnectOptions connOpts = new MqttConnectOptions();
		try {

			myClient = new MqttClient(broker, clientID, persistence);
			connOpts.setCleanSession(true);
			// connOpts.setKeepAliveInterval(30);
			// connOpts.setUserName(userName);
			// connOpts.setPassword(password);
			System.out.println("Connecting to broker: " + broker);
			myClient.setCallback(this);
			myClient.connect(connOpts);
			System.out.println("Connected");

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}

	public void subscribeTopic(String topic, int qos) {
		try {
			myClient.subscribe(topic, qos);

		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	public void publishMessage(String topic, String msg, int qos) {

		MqttMessage message = new MqttMessage(msg.getBytes());
		message.setQos(qos);
		message.setRetained(false);

		System.out.println("Publishing to topic \"" + topic + "\" with QoS " + qos);

		try {

			myClient.publish(topic, message);
			Thread.sleep(150);
			System.out.println("Message published.");

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable arg0) {
		System.out.println("Connection lost!");

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		try {
			System.out.println("Delivered: " + new String(arg0.getMessage().getPayload()));
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		String fullMessage = new String(message.getPayload());
		
		System.out.println("------------ NEW MESSAGE ------------------------");
		System.out.println("| Topic: " + topic);
		System.out.println("| Message: " + fullMessage);
		System.out.println("-------------------------------------------------");
		
		
		MessageParse.parseMsg(fullMessage);
		
		
		
	}

}
