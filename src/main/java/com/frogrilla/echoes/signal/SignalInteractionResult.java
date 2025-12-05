package com.frogrilla.echoes.signal;

import jdk.jfr.Description;

public enum SignalInteractionResult {
    // Signal was not interacted with
    NONE,
    // Signal regular behaviour
    PASS,
    // Same as pass, but with additional context
    MODIFIED,
    // Signal will be removed
    DESTROY,
}
