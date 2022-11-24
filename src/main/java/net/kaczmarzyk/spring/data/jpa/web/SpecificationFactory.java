package net.kaczmarzyk.spring.data.jpa.web;

import net.kaczmarzyk.spring.data.jpa.utils.TypeUtil;
import org.springframework.data.jpa.domain.Specification;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

public class SpecificationFactory {


	public Specification<?> createSpecificationDependingOn(ProcessingContext context) {
		List<Specification<Object>> specs = resolveSpec(context);

		if (specs.isEmpty()) {
			return null;
		}

		if (specs.size() == 1) {
			Specification<Object> firstSpecification = specs.iterator().next();

			if (Specification.class == context.getParameterType()) {
				return firstSpecification;
			} else {
				return (Specification<?>) EnhancerUtil.wrapWithIfaceImplementation(context.getParameterType(), firstSpecification);
			}
		}

		Specification<Object> spec = new net.kaczmarzyk.spring.data.jpa.domain.Conjunction<>(specs);

		return (Specification<?>) EnhancerUtil.wrapWithIfaceImplementation(context.getParameterType(), spec);
	}

	private List<Specification<Object>> resolveSpec(ProcessingContext context) {
		List<Specification<Object>> specAccumulator = new ArrayList<>();

		resolveSpecFromInterfaceAnnotations(context, specAccumulator);
		resolveSpecFromParameterAnnotations(context, specAccumulator);
		System.out.println();
		return specAccumulator;
	}

	private void resolveSpecFromParameterAnnotations(ProcessingContext context,
													 List<Specification<Object>> accum) {
		forEachSupportedSpecificationDefinition(
				context.getParameterAnnotations(),
				specDefinition -> {
					Specification<Object> specification = buildSpecification(context, specDefinition);
					if (nonNull(specification)) {
						accum.add(specification);
					}
				}
		);
	}

	private void resolveSpecFromInterfaceAnnotations(ProcessingContext context,
													 List<Specification<Object>> accumulator) {
		Collection<Class<?>> ifaceTree = TypeUtil.interfaceTree(context.getParameterType());

		for (Class<?> iface : ifaceTree) {
			forEachSupportedInterfaceSpecificationDefinition(iface,
					(specDefinition) -> {
						Specification<Object> specification = buildSpecification(context, specDefinition);
						if (nonNull(specification)) {
							accumulator.add(specification);
						}
					}
			);
		}
	}

	private Specification<Object> buildSpecification(ProcessingContext context, Annotation specDef) {
		SpecificationResolver resolver = resolversBySupportedType.get(specDef.annotationType());

		if (resolver == null) {
			throw new IllegalArgumentException(
					"Definition is not supported. " +
							"Specification resolver is not able to build specification from definition of type :" + specDef.annotationType()
			);
		}

		return resolver.buildSpecification(context, specDef);
	}

	private void forEachSupportedSpecificationDefinition(Annotation[] parameterAnnotations, Consumer<Annotation> specificationBuilder) {
		for (Annotation annotation : parameterAnnotations) {
			for (Class<? extends Annotation> annotationType : resolversBySupportedType.keySet()) {
				if (annotationType.isAssignableFrom(annotation.getClass())) {
					specificationBuilder.accept(annotation);
				}
			}
		}
	}

	private void forEachSupportedInterfaceSpecificationDefinition(Class<?> target, Consumer<Annotation> specificationBuilder) {
		for (Class<? extends Annotation> annotationType : resolversBySupportedType.keySet()) {
			if (target.getAnnotations().length != 0) {
				Annotation potentialAnnotation = target.getAnnotation(annotationType);
				if (potentialAnnotation != null) {
					specificationBuilder.accept(potentialAnnotation);
				}
			}
		}
	}

}
