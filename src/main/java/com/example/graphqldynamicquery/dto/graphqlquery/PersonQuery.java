package com.example.graphqldynamicquery.dto.graphqlquery;

import com.example.graphqldynamicquery.dto.graphqlquery.expression.impl.BooleanExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.expression.impl.IntegerExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.expression.impl.LocalDateExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.expression.impl.StringExpression;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonQuery {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filter {
        private StringExpression id;
        private StringExpression name;
        private LocalDateExpression dateOfBirth;
        private BooleanExpression emailVerified;
        private IntegerExpression creditScore;
        private Filter and;
        private Filter or;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {
        private OrderType id;
        private OrderType name;
        private OrderType dateOfBirth;
        private OrderType emailVerified;
        private OrderType creditScore;
    }

    private List<Filter> and;
    private List<Filter> or;
    private List<Order> orders;

    Integer pageIndex;
    Integer pageSize;
}
