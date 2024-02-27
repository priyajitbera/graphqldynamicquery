package com.example.graphqldynamicquery.dto.graphqlquery.operator;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotEquals<T> {

    T value;
}
