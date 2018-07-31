package com.itsoul.lab.test;

import javax.persistence.EntityManager;

import com.it.soul.lab.service.ORMController;

public class JPAResourceLoader {
	
	private static JPAResourceLoader shared;
	private ORMController controller;

	private JPAResourceLoader() {
		super();
		//We must Create ORMController for each session.
		//One user One or per/thread EntityManager is suitable.
		//Production Mode: Container Injected EntityManager per session.
		controller = new ORMController("testDB");
	}
	
	public static synchronized void configure() {
		if(shared == null){
			shared = new JPAResourceLoader();
        }
	}
	
	public static synchronized EntityManager entityManager() {
		JPAResourceLoader.configure();
		return shared.controller.getEntityManager();
	}

}
