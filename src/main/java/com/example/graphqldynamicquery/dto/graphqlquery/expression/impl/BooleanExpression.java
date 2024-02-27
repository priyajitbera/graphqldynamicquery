package com.example.graphqldynamicquery.dto.graphqlquery.expression.impl;

import com.example.graphqldynamicquery.dto.graphqlquery.expression.EquableExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.expression.NullCheckableExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.operator.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooleanExpression implements EquableExpression<Boolean>, NullCheckableExpression {

    private Equals<Boolean> equals;
    private NotEquals<Boolean> notEquals;
    private In<Boolean> in;
    private NotIn<Boolean> notIn;

    private IsNull isNull;
    private IsNotNull isNotNull;

    private IsTrue isTrue;
    private IsFalse isFalse;
}
