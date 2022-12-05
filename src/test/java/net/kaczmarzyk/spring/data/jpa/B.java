package net.kaczmarzyk.spring.data.jpa;

import javax.persistence.*;

@Entity
public class B {

	@Id
	@GeneratedValue
	Long id;

	@JoinColumn(name = "a_id")
	@OneToOne
	A a;

	public B(A a) {
		this.a = a;
	}

	public B() {

	}

	public Long getId() {
		return id;
	}

	public A getA() {
		return a;
	}
}
