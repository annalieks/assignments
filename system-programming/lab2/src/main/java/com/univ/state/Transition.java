package com.univ.state;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transition {
    private final int to;
    private final char letter;
}
