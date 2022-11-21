package net.kaczmarzyk.spring.data.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class D {

	@Id
	@GeneratedValue
	Long id;

	@OneToOne
	E e;

	public D(E e) {
		this.e = e;
	}

	public D() {

	}

	public E getE() {
		return e;
	}
}
