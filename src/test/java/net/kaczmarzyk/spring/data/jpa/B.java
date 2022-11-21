package net.kaczmarzyk.spring.data.jpa;

import com.sun.istack.Interned;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class B extends A {
	public B(String note) {
		super(note);
	}

	public B() {
		super("default");
	}
}
