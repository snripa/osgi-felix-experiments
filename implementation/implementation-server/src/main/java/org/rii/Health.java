package org.rii;

public class Health {
    private boolean healthy = true;

    public boolean isHealthy() {
        return healthy;
    }

    public Health setHealthy(boolean healthy) {
        this.healthy = healthy;
        return this;
    }

    @Override
    public String toString() {
        return "Health{" +
            "healthy=" + healthy +
            '}';
    }
}
