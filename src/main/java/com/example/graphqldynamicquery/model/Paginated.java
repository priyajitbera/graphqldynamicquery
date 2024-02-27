package com.example.graphqldynamicquery.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paginated<T> {

    private List<T> content;
    private Long pageIndex;
    private Long pageSize;
    private Long totalCount;
    private Long pageCount;
}
