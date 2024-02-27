package com.example.graphqldynamicquery.dto.graphqlquery.expression.impl;

import com.example.graphqldynamicquery.dto.graphqlquery.expression.ComparableExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.expression.EquableExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.expression.NullCheckableExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.operator.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntegerExpression implements EquableExpression<Integer>, ComparableExpression<Integer>, NullCheckableExpression {

    private Equals<Integer> equals;
    private NotEquals<Integer> notEquals;
    private In<Integer> in;
    private NotIn<Integer> notIn;

    private GreaterThan<Integer> greaterThan;
    private GreaterThanOrEqualTo<Integer> greaterThanOrEqualTo;
    private LessThan<Integer> lessThan;
    private LessThanOrEqualTo<Integer> lessThanOrEqualTo;

    private IsNull isNull;
    private IsNotNull isNotNull;
}
