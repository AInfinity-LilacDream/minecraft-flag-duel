package minigame.minecraftFlagDuel.miniDuel;

import org.bukkit.Location;

public class CuboidRegion {
    private final Location point1;
    private final Location point2;

    public CuboidRegion(Location point1, Location point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public boolean contains(Location loc) {
        double minX = Math.min(point1.getX(), point2.getX());
        double minY = Math.min(point1.getY(), point2.getY());
        double minZ = Math.min(point1.getZ(), point2.getZ());
        double maxX = Math.max(point1.getX(), point2.getX());
        double maxY = Math.max(point1.getY(), point2.getY());
        double maxZ = Math.max(point1.getZ(), point2.getZ());

        return (loc.getX() >= minX && loc.getX() <= maxX &&
                loc.getY() >= minY && loc.getY() <= maxY &&
                loc.getZ() >= minZ && loc.getZ() <= maxZ);
    }

    public Location getPoint1() {
        return point1;
    }

    public Location getPoint2() {
        return point2;
    }
}
