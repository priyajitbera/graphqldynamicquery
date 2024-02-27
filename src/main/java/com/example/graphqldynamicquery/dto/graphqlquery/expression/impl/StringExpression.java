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
public class StringExpression implements EquableExpression<String>, NullCheckableExpression {

    private Equals<String> equals;
    private NotEquals<String> notEquals;
    private In<String> in;
    private NotIn<String> notIn;

    private IsNull isNull;
    private IsNotNull isNotNull;

    private Like like;
    private NotLike notLike;
}
