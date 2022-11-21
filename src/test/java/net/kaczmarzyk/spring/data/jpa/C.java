package net.kaczmarzyk.spring.data.jpa;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class C extends B {

	@OneToOne
	D d;

	public C(String note, D d) {
		super(note);
		this.d = d;
	}

	public C() {

	}

	public D getD() {
		return d;
	}
}
