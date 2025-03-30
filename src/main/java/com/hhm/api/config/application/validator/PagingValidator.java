package com.hhm.api.config.application.validator;

import com.hhm.api.model.dto.request.PagingRequest;
import com.hhm.api.support.enums.SortOrder;
import jakarta.persistence.Column;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PagingValidator implements ConstraintValidator<ValidatePaging, PagingRequest> {
    private List<String> allowedSorts;

    @Override
    public void initialize(ValidatePaging constraintAnnotation) {
        Class<?> clazz = constraintAnnotation.sortModel();
        List<Field> fields = FieldUtils.getAllFieldsList(clazz);

        List<String> sortableFields = fields.stream()
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(Field::getName)
                .toList();

        List<String> ascSortableFields = sortableFields.stream()
                .map(field -> String.format("%s.%s", field, SortOrder.ASC.name()))
                .toList();

        List<String> descSortableFields = sortableFields.stream()
                .map(field -> String.format("%s.%s", field, SortOrder.DESC.name()))
                .toList();

        allowedSorts = new ArrayList<>();

        allowedSorts.addAll(sortableFields);
        allowedSorts.addAll(ascSortableFields);
        allowedSorts.addAll(descSortableFields);
    }

    @Override
    public boolean isValid(PagingRequest criteria, ConstraintValidatorContext context) {
        if (CollectionUtils.isEmpty(allowedSorts) || Objects.isNull(criteria.getSortBy())) return true;

        String sort = criteria.getSortBy() + "." +
                (Objects.nonNull(criteria.getSortOrder()) ? criteria.getSortOrder().name() : SortOrder.ASC.name());

        if (!allowedSorts.contains(sort)) {
            String sortErrorMessage = "NOT_ALLOW_SORT";

            context.disableDefaultConstraintViolation();

            context
                    .buildConstraintViolationWithTemplate(sortErrorMessage)
                    .addPropertyNode("sortBy")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
