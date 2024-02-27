package com.example.graphqldynamicquery.dto.graphqlquery.operator;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GreaterThan<T> {

    private T value;
}
