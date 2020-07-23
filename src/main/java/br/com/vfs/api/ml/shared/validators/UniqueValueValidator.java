package br.com.vfs.api.ml.shared.validators;

import br.com.vfs.api.ml.shared.annotations.UniqueValue;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {

    private static final String QUERY = "select 1 from %s where %s=:value";
    private String fieldName;
    private Class<?> clazz;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        fieldName = constraintAnnotation.fieldName();
        clazz = constraintAnnotation.domainClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(Objects.isNull(value)) return true;
        final var query = entityManager.createQuery(String.format(QUERY, clazz.getName(), fieldName));
        query.setParameter("value", value);
        final var resultList = query.getResultList();
        Assert.isTrue(resultList.size() <= 1, String.format("Duplicate value (%s) in attribute %s and class %s", value, fieldName, clazz.getSimpleName()));
        return resultList.isEmpty();
    }
}
