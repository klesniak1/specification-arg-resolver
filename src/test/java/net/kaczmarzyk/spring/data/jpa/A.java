package net.kaczmarzyk.spring.data.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class A {

	@Id
	@GeneratedValue
	Long id;

	String note;

	public A(String note) {
		this.note = note;
	}

	public A() {

	}

	public Long getId() {
		return id;
	}

	public String getNote() {
		return note;
	}
}
