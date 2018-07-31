package com.itsoul.lab.test;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.it.soul.lab.connect.JDBConnection;
import com.it.soul.lab.connect.JDBConnection.DriverClass;
import com.it.soul.lab.service.ORMService;
import com.it.soul.lab.sql.SQLExecutor;
import com.it.soul.lab.sql.query.models.Property;
import com.itsoul.lab.test.Person.MargeAlgo;

public class Application {
	
	public static ORMService<Passenger> passengerService;
	public static ORMService<Person> personService;
	public static SQLExecutor exe;
	public static ExecutorService pool;
	
	public static void before() {
		try{
			Connection conn = new JDBConnection.Builder("jdbc:mysql://localhost:3306/testDB")
					.driver(DriverClass.MYSQL)
					.credential("root", "towhid@123")
					.build();
			if(exe == null) {exe = new SQLExecutor(conn);}
			JPAResourceLoader.configure();
			if(passengerService == null) {passengerService = new ORMService<>(JPAResourceLoader.entityManager(), Passenger.class);}
			if(personService == null) {personService = new ORMService<>(JPAResourceLoader.entityManager(), Person.class);}
			
			pool = Executors.newWorkStealingPool(5);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void after() {
		if(pool.isShutdown() == false) {
			try {
				pool.shutdown();
				pool.awaitTermination(120, TimeUnit.SECONDS);
				System.out.println("Application Shutdown.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		before();
		
		//Example of Executor Interface (Java Safe Concurrency models)
		Future<Boolean> created = pool.submit(() -> {
			return Application.CreateFromPassenger(new Property("name", "Sohana"));
		});
		
		try {
			//Calling the method get() blocks the current thread and waits until the callable completes.
			if (created.get()) { 
				pool.execute(() -> {
					Application.FetchALLPass();
				});
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		//Example of custom @FunctionalInterface as a Block of code.
		try {
			Person.marge(new Property("name", "towhid")
					, new Property("name", "towhid-islam")
					, (p1, p2) -> {
						p1.margeWith(p2, MargeAlgo.Semantic);
						return p1;
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		after();
	}
	
	public static void FetchALLPass() {
		try {
			List<Passenger> items = (List<Passenger>) passengerService.findAll();
			List<String> names = items.stream().map(p -> p.getId() + ":" + p.getName()).collect(Collectors.toList());
			System.out.println("Fetch ALL:");
			names.forEach(val -> System.out.println(val));
			System.out.println("Fetch ALL End");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Boolean CreateFromPassenger(Property search) {
		try {
			Passenger passenger = passengerService.findBy(search);
			if(passenger != null) {
				Person nPerson = passenger.insertPerson(p -> {
					Person x = new Person();
					x.setUuid(UUID.randomUUID().toString() + "#" + p.getId());
					x.setName(p.getName());
					return x;
				});
				if(personService.exist(nPerson.getUuid()) == true) {
					System.out.println(nPerson.getName() + " Created.");
					return true;
				}else {
					System.out.println(nPerson.getName() + " Failed.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
