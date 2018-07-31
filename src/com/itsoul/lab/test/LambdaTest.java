package com.itsoul.lab.test;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.it.soul.lab.connect.JDBConnection;
import com.it.soul.lab.connect.JDBConnection.DriverClass;
import com.it.soul.lab.service.ORMService;
import com.it.soul.lab.sql.SQLExecutor;
import com.it.soul.lab.sql.query.models.Property;

public class LambdaTest {
	
	ORMService<Passenger> service;
	ORMService<Person> personService;
	SQLExecutor exe;
	
	@Before
	public void before() {
		try{
			Connection conn = new JDBConnection.Builder("jdbc:mysql://localhost:3306/testDB")
					.driver(DriverClass.MYSQL)
					.credential("root", "towhid@123")
					.build();
			if(exe == null) {exe = new SQLExecutor(conn);}
			JPAResourceLoader.configure();
			if(service == null) {service = new ORMService<>(JPAResourceLoader.entityManager(), Passenger.class);}
			if(personService == null) {personService = new ORMService<>(JPAResourceLoader.entityManager(), Person.class);}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void after() {
		//
	}

	//@Test
	public void test() {
		Assert.assertTrue(exe != null && service != null && personService != null);
	}
	
	//@Test
	public void test2() {
		Assert.assertTrue("Still Exist",exe != null && service != null && personService != null);
	}
	
	@Test
	public void FetchALLPass() {
		try {
			List<Passenger> items = (List<Passenger>) service.findAll();
			List<String> names = items.stream().map(p -> p.getId() + ":" + p.getName()).collect(Collectors.toList());
			names.forEach(val -> System.out.println(val));
			Assert.assertTrue(names.size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void CreateFromPassenger() {
		try {
			Passenger passenger = service.findBy(new Property("name", "towhid"));
			if(passenger != null) {
				Person nPerson = passenger.insertPerson(p -> {
					Person x = new Person();
					x.setUuid(UUID.randomUUID().toString() + "#" + p.getId());
					x.setName(p.getName());
					return x;
				});
				Assert.assertTrue(nPerson.getName() + " Created.", personService.exist(nPerson.getUuid()) == true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
