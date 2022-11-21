package net.kaczmarzyk.spring.data.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class E {

	@Id
	@GeneratedValue
	Long id;

	String comment;

	public E(String comment) {
		this.comment = comment;
	}

	public E() {

	}

	public Long getId() {
		return id;
	}

	public String getComment() {
		return comment;
	}
}
