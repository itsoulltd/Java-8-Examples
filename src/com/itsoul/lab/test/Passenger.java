package com.itsoul.lab.test;

import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.it.soul.lab.service.ORMService;

@Entity
@Table(name="Passenger")
public class Passenger {
	@Column
	private String name;
	@Column @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private Integer age;
	@Column
	private String sex;
	
	public Passenger() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public Person insertPerson(Function<Passenger, Person> block) throws Exception {
		Person np = block.apply(this);
		ORMService<Person> service = new ORMService<>(JPAResourceLoader.entityManager(), Person.class);
		service.insert(np);
		return np;
	}
	
}
