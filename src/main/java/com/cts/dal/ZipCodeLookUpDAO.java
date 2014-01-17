package com.cts.dal;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.impetus.client.cassandra.common.CassandraConstants;
import com.impetus.client.cassandra.thrift.ThriftClient;
import com.impetus.kundera.client.Client;

public class ZipCodeLookUpDAO {

	static EntityManagerFactory emf = null;
	static EntityManager manager = null;

	static {
		/*new ClassPathXmlApplicationContext(
				new String[] { "Spring-Customer.xml" });*/

		emf = Persistence.createEntityManagerFactory("cassandra_pu");
		manager = emf.createEntityManager();
		System.setProperty("cassandra.join_ring", "false");
		Map<String, Client> clientMap = (Map<String, Client>) manager
				.getDelegate();
		ThriftClient pc = (ThriftClient) clientMap.get("cassandra_pu");
		pc.setCqlVersion(CassandraConstants.CQL_VERSION_3_0);
	}

	public static ZipLocationBean findByZipCode(String zipcode) {
		ZipLocationBean bean = manager.find(ZipLocationBean.class, zipcode);
		return bean;
	}

	public static void main(String args[]) {
		findByZipCode("95126");
	}
}