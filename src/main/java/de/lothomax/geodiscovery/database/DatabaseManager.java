package de.lothomax.geodiscovery.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.lothomax.geodiscovery.model.DiscoveredRegion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {

    private final Logger logger;
    private HikariDataSource dataSource;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DatabaseManager(File dataFolder, Logger logger) {
        this.logger = logger;
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File dbFile = new File(dataFolder, "locations.db");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
        config.setPoolName("GeoDiscovery-Hikari");
        config.setMaximumPoolSize(1); // SQLite only supports single writer
        config.addDataSourceProperty("journal_mode", "WAL"); // Write-Ahead Logging for better concurrency
        config.setConnectionTestQuery("SELECT 1");

        this.dataSource = new HikariDataSource(config);
    }

    public void initialize() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            String createTableSql = "CREATE TABLE IF NOT EXISTS discovered_regions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "region_name VARCHAR NOT NULL, " +
                    "discoverer_uuid VARCHAR NOT NULL, " +
                    "discoverer_name VARCHAR NOT NULL, " +
                    "world_uuid VARCHAR NOT NULL, " +
                    "center_x DOUBLE NOT NULL, " +
                    "center_z DOUBLE NOT NULL, " +
                    "radius INTEGER NOT NULL DEFAULT 150, " +
                    "region_type VARCHAR NOT NULL DEFAULT 'BIOME', " +
                    "biome_key VARCHAR, " +
                    "timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                    ");";
            statement.execute(createTableSql);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to initialize database", e);
        }
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public CompletableFuture<Void> saveRegion(DiscoveredRegion region) {
        return CompletableFuture.runAsync(() -> {
            String sql = "INSERT INTO discovered_regions (region_name, discoverer_uuid, discoverer_name, " +
                    "world_uuid, center_x, center_z, radius, region_type, biome_key, timestamp) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, region.getRegionName());
                ps.setString(2, region.getDiscovererUuid().toString());
                ps.setString(3, region.getDiscovererName());
                ps.setString(4, region.getWorldUuid().toString());
                ps.setDouble(5, region.getCenterX());
                ps.setDouble(6, region.getCenterZ());
                ps.setInt(7, region.getRadius());
                ps.setString(8, region.getRegionType());
                ps.setString(9, region.getBiomeKey());
                ps.setString(10, region.getTimestamp().format(FORMATTER));

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        region.setId(rs.getInt(1));
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to save region: " + region.getRegionName(), e);
            }
        });
    }

    public CompletableFuture<List<DiscoveredRegion>> getAllRegions(String worldUuid) {
        return CompletableFuture.supplyAsync(() -> {
            List<DiscoveredRegion> regions = new ArrayList<>();
            String sql = "SELECT * FROM discovered_regions WHERE world_uuid = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, worldUuid);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        DiscoveredRegion region = new DiscoveredRegion.Builder()
                                .id(rs.getInt("id"))
                                .regionName(rs.getString("region_name"))
                                .discovererUuid(UUID.fromString(rs.getString("discoverer_uuid")))
                                .discovererName(rs.getString("discoverer_name"))
                                .worldUuid(UUID.fromString(rs.getString("world_uuid")))
                                .centerX(rs.getDouble("center_x"))
                                .centerZ(rs.getDouble("center_z"))
                                .radius(rs.getInt("radius"))
                                .regionType(rs.getString("region_type"))
                                .biomeKey(rs.getString("biome_key"))
                                .timestamp(LocalDateTime.parse(rs.getString("timestamp"), FORMATTER))
                                .build();
                        regions.add(region);
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to fetch regions for world: " + worldUuid, e);
            }

            return regions;
        });
    }

    public CompletableFuture<List<DiscoveredRegion>> getAllRegions() {
         return CompletableFuture.supplyAsync(() -> {
            List<DiscoveredRegion> regions = new ArrayList<>();
            String sql = "SELECT * FROM discovered_regions";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    DiscoveredRegion region = new DiscoveredRegion.Builder()
                            .id(rs.getInt("id"))
                            .regionName(rs.getString("region_name"))
                            .discovererUuid(UUID.fromString(rs.getString("discoverer_uuid")))
                            .discovererName(rs.getString("discoverer_name"))
                            .worldUuid(UUID.fromString(rs.getString("world_uuid")))
                            .centerX(rs.getDouble("center_x"))
                            .centerZ(rs.getDouble("center_z"))
                            .radius(rs.getInt("radius"))
                            .regionType(rs.getString("region_type"))
                            .biomeKey(rs.getString("biome_key"))
                            .timestamp(LocalDateTime.parse(rs.getString("timestamp"), FORMATTER))
                            .build();
                    regions.add(region);
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to fetch all regions", e);
            }

            return regions;
        });
    }
}
