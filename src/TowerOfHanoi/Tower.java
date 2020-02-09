package TowerOfHanoi;

import java.util.*;

/**
 * Tower:  The individual towers that make up the tower of Hanoi system.
 */
public class Tower {
    public int towerId;
    public ArrayList<Disk> disks;

    public Tower(int towerId) {
        this.towerId = towerId;
        disks = new ArrayList<Disk>();
    }

    /**
     * Pops the disk of the top of the tower
     */
    public Disk popDisk() {
        Disk topDisk = null;

        if (disks.size() != 0) {
            topDisk = disks.get(0);

            topDisk.setLastTowerID(this.towerId);

            // Remove the top entry
            disks.remove(0);
        }

        return topDisk;
    }

    /** Pushes a disk onto the top of the tower
     *
     * @param disk   The disk to push
     */
    public void pushDisk(Disk disk) {
        disks.add(0, disk);
    }

    public boolean isEmpty() {
        return (disks.size() == 0);
    }

    /**
     * Used for looking at the top disk without removing it from the stack
     *
     * @return The disk on top
     */
    public Disk getTopDisk() {
        if (isEmpty())
            return null;
        else
            return (disks.get(0));
    }

    /**
     * Finds the count of disks in the tower.
     * @return   The number of disks on the tower.
     */
    public int getDiskCount()
    {
        return ( disks.size() );
    }
}
