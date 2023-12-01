package com.bichotitas.votacionesmvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Vote {
    String date;
    String productName;
    String time;

    @Override
    public String toString() {
        return productName + " - " + date + " - " + time;
    }
}
