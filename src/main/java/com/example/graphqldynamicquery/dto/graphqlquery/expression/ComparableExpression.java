package com.example.graphqldynamicquery.dto.graphqlquery.expression;

import com.example.graphqldynamicquery.dto.graphqlquery.operator.GreaterThan;
import com.example.graphqldynamicquery.dto.graphqlquery.operator.GreaterThanOrEqualTo;
import com.example.graphqldynamicquery.dto.graphqlquery.operator.LessThan;
import com.example.graphqldynamicquery.dto.graphqlquery.operator.LessThanOrEqualTo;

public interface ComparableExpression<T> {

    GreaterThan<T> getGreaterThan();

    GreaterThanOrEqualTo<T> getGreaterThanOrEqualTo();

    LessThan<T> getLessThan();

    LessThanOrEqualTo<T> getLessThanOrEqualTo();
}
