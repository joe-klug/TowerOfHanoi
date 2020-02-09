package TowerOfHanoi;

/** Disks on the Tower of Hanoi */
public class Disk {

    /**  Identifier for the disk */
    private int id;                 // This is the size of the disk   7 is bigger than 6.
    private int lastTowerID;

     /** The tower where the disk currently resides.   This allows for quickly determining the source
     * tower when looking for the new source tower.
     */
    //private Tower currentTower;

    public Disk( int id ) {
        this.id = id;

        lastTowerID = -1;
     }

    public void setLastTowerID(int lastTowerID) {
        this.lastTowerID = lastTowerID;
    }

       public Integer getId()
    {
        return id;
    }
}
