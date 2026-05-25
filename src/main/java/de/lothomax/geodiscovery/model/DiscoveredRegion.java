package de.lothomax.geodiscovery.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class DiscoveredRegion {
    private int id;
    private String regionName;
    private UUID discovererUuid;
    private String discovererName;
    private UUID worldUuid;
    private double centerX;
    private double centerZ;
    private int radius;
    private String regionType;
    private String biomeKey;
    private LocalDateTime timestamp;

    private DiscoveredRegion(Builder builder) {
        this.id = builder.id;
        this.regionName = builder.regionName;
        this.discovererUuid = builder.discovererUuid;
        this.discovererName = builder.discovererName;
        this.worldUuid = builder.worldUuid;
        this.centerX = builder.centerX;
        this.centerZ = builder.centerZ;
        this.radius = builder.radius;
        this.regionType = builder.regionType;
        this.biomeKey = builder.biomeKey;
        this.timestamp = builder.timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public UUID getDiscovererUuid() {
        return discovererUuid;
    }

    public void setDiscovererUuid(UUID discovererUuid) {
        this.discovererUuid = discovererUuid;
    }

    public String getDiscovererName() {
        return discovererName;
    }

    public void setDiscovererName(String discovererName) {
        this.discovererName = discovererName;
    }

    public UUID getWorldUuid() {
        return worldUuid;
    }

    public void setWorldUuid(UUID worldUuid) {
        this.worldUuid = worldUuid;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterZ() {
        return centerZ;
    }

    public void setCenterZ(double centerZ) {
        this.centerZ = centerZ;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getRegionType() {
        return regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }

    public String getBiomeKey() {
        return biomeKey;
    }

    public void setBiomeKey(String biomeKey) {
        this.biomeKey = biomeKey;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DiscoveredRegion{" +
                "id=" + id +
                ", regionName='" + regionName + '\'' +
                ", discovererUuid=" + discovererUuid +
                ", discovererName='" + discovererName + '\'' +
                ", worldUuid=" + worldUuid +
                ", centerX=" + centerX +
                ", centerZ=" + centerZ +
                ", radius=" + radius +
                ", regionType='" + regionType + '\'' +
                ", biomeKey='" + biomeKey + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public static class Builder {
        private int id;
        private String regionName;
        private UUID discovererUuid;
        private String discovererName;
        private UUID worldUuid;
        private double centerX;
        private double centerZ;
        private int radius = 150;
        private String regionType = "BIOME";
        private String biomeKey;
        private LocalDateTime timestamp = LocalDateTime.now();

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder regionName(String regionName) {
            this.regionName = regionName;
            return this;
        }

        public Builder discovererUuid(UUID discovererUuid) {
            this.discovererUuid = discovererUuid;
            return this;
        }

        public Builder discovererName(String discovererName) {
            this.discovererName = discovererName;
            return this;
        }

        public Builder worldUuid(UUID worldUuid) {
            this.worldUuid = worldUuid;
            return this;
        }

        public Builder centerX(double centerX) {
            this.centerX = centerX;
            return this;
        }

        public Builder centerZ(double centerZ) {
            this.centerZ = centerZ;
            return this;
        }

        public Builder radius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder regionType(String regionType) {
            this.regionType = regionType;
            return this;
        }

        public Builder biomeKey(String biomeKey) {
            this.biomeKey = biomeKey;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public DiscoveredRegion build() {
            return new DiscoveredRegion(this);
        }
    }
}
