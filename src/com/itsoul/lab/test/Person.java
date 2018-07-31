package com.itsoul.lab.test;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.it.soul.lab.service.ORMService;
import com.it.soul.lab.sql.query.models.Property;

@Entity
@Table(name="Person")
public class Person {
	@Column @Id
	private String uuid;
	@Column
	private String name;
	@Column
	private Integer age;
	@Column
	private Boolean isActive;
	@Column
	private Double salary;
	@Column
	private Timestamp dob;
	@Column
	private Date dobDate;
	@Column
	private Float height;
	@Column
	private Timestamp createDate;
	@Column
	private Time createTime;
	
	public Person() {
		super();
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public Timestamp getDob() {
		return dob;
	}
	public void setDob(Timestamp dob) {
		this.dob = dob;
	}
	public Date getDobDate() {
		return dobDate;
	}
	public void setDobDate(Date dobDate) {
		this.dobDate = dobDate;
	}
	public Float getHeight() {
		return height;
	}
	public void setHeight(Float height) {
		this.height = height;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Time getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Time createTime) {
		this.createTime = createTime;
	}
	
	public Passenger insertPassenger(Function<Person, Passenger> block) throws Exception {
		Passenger np = block.apply(this);
		ORMService<Passenger> service = new ORMService<>(JPAResourceLoader.entityManager(), Passenger.class);
		service.insert(np);
		return np;
	}
	
	public static Person marge(Property pa, Property pb, PersonMarger<Person> marger) throws Exception {
		
		if(marger == null) {return null;}
		
		ORMService<Person> personService = new ORMService<>(JPAResourceLoader.entityManager(), Person.class);
		Person personA = personService.findBy(pa);
		Person personB = personService.findBy(pb);
		if(personA == null || personB == null) {return null;}
		
		Person np = marger.marge(personA, personB);
		if (np != null) {
			if(personService.exist(np.getUuid())) {
				personService.update(np);
				if (np.getUuid().equalsIgnoreCase(personA.getUuid())) {personService.delete(personB);}
				else {personService.delete(personA);}
			}
			else {
				personService.insert(np);
				personService.delete(personA);
				personService.delete(personB);
			}
		}
		return np;
	}
	
	public enum MargeAlgo{
		Semantic,
		Sequence
	}
	
	public void margeWith(Person p, MargeAlgo algo) {
		//TODO:
	}
	
}
