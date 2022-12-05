package net.kaczmarzyk.spring.data.jpa;

import javax.persistence.*;

@Entity
public class A {

	@Id
	@GeneratedValue
	Long id;

	@OneToOne(mappedBy = "a", fetch = FetchType.EAGER)
	B b;

	public A(B b) {
		this.b = b;
	}

	public A() {

	}
}
