package net.kaczmarzyk.spring.data.jpa;

import javax.persistence.*;

@Entity
public class A {

	@Id
	@GeneratedValue
	Long id;

	@OneToOne(mappedBy = "a")
	B b;

	String example;

	String name;

	public A(B b, String example, String name) {
		this.b = b;
		this.example = example;
		this.name = name;
	}

	public A() {

	}

	public B getB() {
		return b;
	}

	public Long getId() {
		return id;
	}

	public String getExample() {
		return example;
	}

	public String getName() {
		return name;
	}
}
