package com.cts.dal;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.cts.processor.datamodel.ShipmentTrackingResponse;
import com.google.gson.Gson;
import com.impetus.client.cassandra.common.CassandraConstants;
import com.impetus.client.cassandra.thrift.ThriftClient;
import com.impetus.kundera.client.Client;

@SuppressWarnings("unchecked")
public class ShipmentTrackingDetailsDAO {
	static EntityManagerFactory emf = null;
	static EntityManager manager = null;

	static {
		emf = Persistence.createEntityManagerFactory("cassandra_pu2");
		manager = emf.createEntityManager();
		System.setProperty("cassandra.join_ring", "false");
		@SuppressWarnings("rawtypes")
		Map<String, Client> clientMap = (Map<String, Client>) manager
				.getDelegate();
		ThriftClient pc = (ThriftClient) clientMap.get("cassandra_pu2");
		pc.setCqlVersion(CassandraConstants.CQL_VERSION_3_0);
	}

	public static ShipmentTrackingDetails findByTrackingNumber(
			String trackingNumber) {
		ShipmentTrackingDetails bean = manager.find(
				ShipmentTrackingDetails.class, trackingNumber);
		if (bean != null) {
			String responseJson = bean.getDetails();
			if (responseJson != null && !"".equals(responseJson)) {
				ShipmentTrackingResponse res = new Gson().fromJson(
						responseJson, ShipmentTrackingResponse.class);
				bean.setResponse(res);
			}
		}
		return bean;
	}

	public static void insert(String trackingNumber, String details) {
		ShipmentTrackingDetails bean = new ShipmentTrackingDetails();
		bean.setDetails(details);
		bean.setTrackingNumber(trackingNumber);
		manager.persist(bean);
	}

	public static void insert(ShipmentTrackingResponse response) {
		if (response == null) {
			return;
		}
		ShipmentTrackingDetails bean = new ShipmentTrackingDetails();
		bean.setDetails(new Gson().toJson(response));
		bean.setTrackingNumber(response.getTrackingNumber());
		manager.persist(bean);
	}

	public static void main(String args[]) {
		findByTrackingNumber("799922380926");
	}
}