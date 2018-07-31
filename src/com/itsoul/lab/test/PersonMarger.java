package com.itsoul.lab.test;

@FunctionalInterface
public interface PersonMarger<P extends Person> {
	public P marge(P personA, P personB);
}
