package com.example.graphqldynamicquery.dto.graphqlquery.operator;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class In<T> {

    private List<T> values;
}
