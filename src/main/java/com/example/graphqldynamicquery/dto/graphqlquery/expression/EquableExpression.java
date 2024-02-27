package com.example.graphqldynamicquery.dto.graphqlquery.expression;

import com.example.graphqldynamicquery.dto.graphqlquery.operator.Equals;
import com.example.graphqldynamicquery.dto.graphqlquery.operator.In;
import com.example.graphqldynamicquery.dto.graphqlquery.operator.NotEquals;
import com.example.graphqldynamicquery.dto.graphqlquery.operator.NotIn;

public interface EquableExpression<T> {

    Equals<T> getEquals();

    NotEquals<T> getNotEquals();

    In<T> getIn();

    NotIn<T> getNotIn();
}
