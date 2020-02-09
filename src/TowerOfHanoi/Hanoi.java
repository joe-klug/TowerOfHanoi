package TowerOfHanoi;


import java.text.MessageFormat;
import static java.lang.Math.pow;

/**
 *  Implements a generalized Tower of Hanoi game where towers and disks can take on assorted values.
 */
public class Hanoi {
    private Integer towerCount;
    private Integer diskCount;
    private Tower[] towers;
    private Disk[] disks;

    // USed for solving the tower of Hanoi problem
    private Tower sourceTower;
    private Tower destinationTower;
    private Tower workingTower;

    private SolutionType solutionMethod = SolutionType.Unknown;

    private boolean diag01 = true;

    // Types of solution processing based on the disks and towers
    public enum SolutionType
    {
        Unknown,                      // Can't be solved with the current system.
        ShiftAndStack,                // Simplest where there are more towers that disks.
        BufferUnbuffer,               // Disks are buffered into piles and moved to destination with
                                      //   No regular Hanoi processing.
        BufferHanoiUnbuffer,          // Disks are buffered, traditional Hanoi processing and the disks
                                      //    are unbuffered.
        TradionalHanoi                // Tradional Hanoi processing for testing the Hanoi Algorithm
    };

    /**
     * Looks at the towerCount and diskCount to see what type of solution we are solving.  This is done by looking
     *    at the towers to see how many disks they can buffer out of the original configuration based on the summation
     *    on the disks filling up the buffer towers.    Using Eulers equation
     *    for a series of number n/2 ( first_number + second_number )   where n os the number of
     *    numbers in the series.  So, when the number of Disks <= ( (Towers - 1 )/2 ( 1 + Towers - 1 )
     *    It reduces to Disks<= ( Towers - 1 ) /2 * ( Towers ).  This routine determine the type of
     *    processing required.
     *
     * @return The type of solution we must calculate.
     */
    public SolutionType calculateSolutionsType()
    {
        SolutionType solType = SolutionType.Unknown;
        int bufferedDiskCount = 0;

        if ( towerCount == 3 )
            solType = SolutionType.TradionalHanoi;
        else if ( diskCount < towerCount )
        {
            solType = SolutionType.ShiftAndStack;
        }
        else
        {
             // Calculate the maximum number of disk the system can buffer
             bufferedDiskCount =  ( ( towerCount - 1 ) *  towerCount ) /  2;

             if ( diskCount <= bufferedDiskCount )
             {
                 solType = SolutionType.BufferUnbuffer;
             }
             else
             {
                 solType = SolutionType.BufferHanoiUnbuffer;
             }
        }

        return solType;
    }

    public Hanoi( int towers, int discs )
    {
        this.towerCount = towers;
        this.diskCount = discs;

        this.towers = new Tower[towerCount + 1];
        this.disks = new Disk[diskCount + 1];

        // Solution Method
        solutionMethod = calculateSolutionsType();

        // Fill the array with towers
        for ( int i = 0; i< towerCount; i++)
        {
            Tower newTower = new Tower( i + 1 );
            this.towers[i+1] = newTower;

            if ( i == 0 )
            {
                sourceTower = newTower;
            }
            else if ( i == ( towerCount - 1 ))
            {
                 destinationTower = newTower;
            }
            else if ( i == ( towerCount - 2))
            {
                workingTower = newTower;
            }
        }

        // Fill the source tower with the initial setup and populate a vector to find the Disks int the towers
        for ( int i = ( diskCount - 1 ); i >= 0; i-- )
        {
            Disk newDisk = new Disk( i + 1);
            disks[i+1] = newDisk;
            sourceTower.pushDisk( newDisk );
        }
    }

    /** Displays the state of the towers in simple characters */
    void display( boolean maxHeightFlag )
    {
        // Build the text we will display and display to stdout
        String divider = "---------------------------------------------------------------------";
        String top = "----   ";
        String numbers = "";    // Tower numbers for headings
        String topHeader = "";
        Integer towerNum = 0;
        Integer maxHeight = 0;
        Integer index = 0;

        // Build numbers and big top
        for ( int i = 0; i < towerCount; i++ )
        {
            topHeader += "----   ";
            towerNum = i + 1;
            numbers += "  " + towerNum.toString() + "    ";
        }

        System.out.println( divider );
        System.out.println( "Towers: " + towerCount.toString() + "  Disks: " + diskCount.toString() );
        System.out.println( numbers );
        System.out.println( topHeader );

        if ( maxHeightFlag )
            maxHeight = diskCount;
        else
        {
            for ( int i = 0; i < towerCount; i++ )
            {
                if ( towers[i + 1].disks.size() > maxHeight )
                    maxHeight = towers[i + 1].disks.size();
            }
        }

        String towerDisplay[] = new String[maxHeight];

        // Initialize the array to null characters
        for ( int i = 0; i < maxHeight; i++ )
            towerDisplay[i] = "";

        /* For each tower display their disks */
        for ( int i = 0; i < towerCount; i++)
        {

            for ( int j = 0; j < maxHeight; j++)
            {
                // When not negative, it indexes into the disks
                index = j - ( maxHeight - towers[i + 1].disks.size() );

                if ( index < 0 )
                {
                    towerDisplay[j] += "       ";
                }
                else
                {
                     Integer diskId =  towers[i + 1].disks.get(index).getId();
                    if ( diskId < 10 )
                        towerDisplay[j] += "  " + diskId.toString() + "    ";
                    else
                        towerDisplay[j] +=  " " + diskId.toString() + "    ";
                }
            }
        }

        // Display the contents of the towers
        for ( int i = 0; i < maxHeight; i++ )
            System.out.println( towerDisplay[i] );
    }

    /** Moves the disk from tower fromTowerID to to */
    void MoveDisk( int fromTowerID, int toTowerID )
    {
        Tower fromTower = towers[fromTowerID];
        Tower toTower = towers[toTowerID];

        moveDisk( fromTower, toTower );
    }

    /** Moves a disk from one tower to another using references.  If any references are null, false is returned.
     *
     * @param fromTower :  The top disk is taken from here.
     * @param toTower :  Ends up on the top of this
     * @return :  If the arguments are defined, true
     */
    boolean moveDisk( Tower fromTower, Tower toTower )
    {
        if ( fromTower == null || toTower == null )
            return false;

        Disk moveDisk = fromTower.popDisk();
        toTower.pushDisk( moveDisk );
        moveDiskDisplay( moveDisk, fromTower, toTower );

        return true;
    }

    /** Display the info about a disk move
     *
     */
    void moveDiskDisplay( Disk moveDisk, Tower fromTower, Tower toTower )
    {
        System.out.println(MessageFormat.format("MOVING Disk:{0} From Tower:{1} To Tower:{2}",
                                                   moveDisk.getId(), fromTower.towerId, toTower.towerId)
        );
        //display( false);
    }

    /** Is the tower empty based on its id
     *
     * @return True if the tower is empty.
     */
    boolean isTowerEmpty( int towerID )
    {
        return ( towers[towerID].isEmpty());
    }

    /**
     *  Takes the disks from the source tower and makes sub piles of them
     *  in order from least to highest ID.  It will configure them based on the number of disks and
     *  towers and the subsequent processing.  Subsequent processing will include 1) Immediate unbuffering
     *  and shifting to the destination.  2) Buffering as much as possible and leaving the destination and working
     *  towers empty.
     */
    void bufferDisks()
    {
        Integer maxTowerPile = 0;    // Specifies where the last buffer pile will be created.   The will
                                     //   allow us to make room for the empty towers when employing the
                                     //   Tower of Hanoi Algorithm.

        // We can fill the towers to capacity with disks as the Tower of Hanoi algorithm does not have to
        //   work on this system.  When done tower 1 will be empty as will possibly some other towers.
        if ( solutionMethod == SolutionType.BufferUnbuffer )
        {
            maxTowerPile = towerCount;
        }
        /* Must leave room for the empty working tower and the empty destination tower. */
        else if ( solutionMethod == SolutionType.BufferHanoiUnbuffer )
        {
            maxTowerPile = towerCount - 2;
        }

        // Create buffer piles from pile to 2 to pile maxTowerPile;
        for ( int t = 2; t <= maxTowerPile; t++)
        {
            // IF there are no more source disks, we are done.
            if ( isTowerEmpty(1))
                break;

            // Spread the values out
            for ( int d = towerCount; ( d >= t ) && !isTowerEmpty( 1 ); d-- )
                MoveDisk( 1, d );

            // Make the disks into a tower
            for ( int s = t + 1; s <= towerCount; s++ )
            {
                if ( !isTowerEmpty( s ))
                {
                    MoveDisk( s, t );
                }

            }
        }
    }

    /**
     *  Takes the system of disks with buffers and spreads them out on the empty towers and places them
     *    on the destination pile.  This may be called after the piles are made or after the Tower of Hanoi
     *    gets called.
     */
    void processBufferedDisks()
    {
        Tower[] freeTower = new Tower[towerCount];
        int freeTowerCount = 0;                          // Free Tours to move discs to.

        freeTower[freeTowerCount++] = towers[1];     // The Source which should now be empty

            // Process the little tower buffers, place the buffers onto empty towers and move them to the destination.
            for ( int i = towerCount -1; i >=  2; i-- )
            {
                // Nothing here move it to the freeTower list.
                if ( towers[i].isEmpty())
                {
                    // Add it to the freeTower array
                    freeTower[freeTowerCount++] = towers[i];
                }
                else
                {
                    // Move N -1 of the disks to the Empty towers
                    int discsOnTower = towers[i].getDiskCount();
                    discsOnTower--;            // We will be leaving one disk on the tower.  It should be the max on the
                                               //  tower.

                    // Move the disks in the buffer pile to the empty towers
                    for ( int d = 0; d < discsOnTower; d++ )
                    {
                          // Move the disks to the free towers
                          moveDisk( towers[i], freeTower[d]);
                    }
                    // Move the disk that was on the bottom to the destination
                    moveDisk( towers[i], destinationTower );

                    // Move all the other disks to the destination in the reverse order in which they were put out.
                    for ( int d = discsOnTower - 1; d >= 0; d--)
                    {
                        moveDisk( freeTower[d], destinationTower);
                    }
                    // The tower should now be empty, add it to the list of free towers.
                    freeTower[freeTowerCount++] = towers[i];
                }
            }
    }

    /** Determines the move between towers based on what is on top of the tower and maintain that a
     *  larger disk can't be put on top of a smaller disk.  Id is the size of the disk.
     */
    void moveDiskBetweenTowers( Tower aTower, Tower bTower )
    {
        // If bad input return false
        if ( aTower == null || bTower == null )
            return;

        // If A is empty, move the top entry for bTower to aTower
        if ( aTower.isEmpty() )
        {
            moveDisk( bTower, aTower );
        }
        // If bTower is empty, move the top entry from aTower tp bTower
        else if ( bTower.isEmpty() )
        {
            moveDisk( aTower, bTower );
        }
        else if ( aTower.getTopDisk().getId() > bTower.getTopDisk().getId() )
        {
            moveDisk( bTower, aTower );
        }
        else if ( aTower.getTopDisk().getId() < bTower.getTopDisk().getId() )
        {
            moveDisk( aTower, bTower );
        }
    }

    /**  Given Three towers, the source, working and destination towers, perform the tower of Hanoi
     *         algorithm in an iterative manner. This algorithm is based on the one found at
     *         www.geeksforgeeks.org/iterative-tower-of-hanoi/
     *
     * @param srcTower   Source Tower
     * @param wrkTower   Working Tower
     * @param dstTower   Destination Tower
     */
    void processTowerHanoi( Tower srcTower, Tower wrkTower, Tower dstTower)
    {
        int srcDiskCount = srcTower.getDiskCount();

        // If the disks are even, swap the working and destination tower.
        if ( ( srcDiskCount % 2 ) == 0 )
        {
            Tower tmp;  // Used when swapping
            tmp = dstTower;
            dstTower = wrkTower;
            wrkTower = tmp;
        }

        // Iterations to solve the system
        long iterations = (long)pow( 2.0, srcDiskCount ) - 1;

        /* Based on the iteration number, move the disks in the patter used to move
         *   from the source to the destination towers.
         */
         for ( long i = 1; i <= iterations; i++ )
        {
            // Move the top disk between source and destination towers
            if ( ( i % 3 ) == 1)
            {
                moveDiskBetweenTowers( srcTower, dstTower );
            }

            // Move the top disk between the source and working towers
            else if ( ( i % 3) == 2 )
            {
                moveDiskBetweenTowers( srcTower, wrkTower );
            }
            // Move the top disk between the working and destination towers
            else if ( ( i % 3 ) == 0 )
            {
                moveDiskBetweenTowers( wrkTower, dstTower );
            }
        }
    }

    /**  Solves the Tower of Hanoi in an iterative manner doing the moves based on a set of rules.
     *
     *  Determine hoW to solvEthe problem    based on    the number    of towers    and disks.
     *    3 methods of solutions exist, and a problem at hirer levels will
     * require the solutions of the previous 3 methods.  To complete
     * 1 )  Where the Disks <= to the number of towers.
     *     This requires some spreading and stacking with O( N )
     * 2 )  Where the disks can be stacked onto the towers in ordered sub stacks.
     *       This takes place when the we can stack all of the disks can be stacked and
     *       the disks can be stacked and shuffled off to the destination pile.
     *       So, when the number of Disks <= ( (Towers - 1 )/2 ( 1 + Towers - 1 )
     *       Reduces to Disks<= ( Towers - 1 ) /2 * ( Towers ).   The order of this is O(N) based on the
     *       number of disks.   Most of the disks will move 4 times.   Each pile will reduce down to
     *       problem type 1.
     *  3)  Finally there is the situation that takes place when Disks >= ( Towers - 1 ) /2 ( 1 +Towers )
     *        This requires using the traditional Tower of Hanoi solution.   Once the 1st stage of creating
     *        sub piles takes place.  The Hanoi solution take place to sort the remaining pile and the
     *        sub piles are then processed. This is of the O ( 2 ^ ( Disks - Towers*( Towers -1 ) / 2 ) ).  IF the
     *        exponent goes negative, we are in case 1) or 2).
     * @return  True indicates no exceptions encountered.
     */
    boolean solve()
    {
        boolean success = false;


        try
        {
            // Solving a spread and stack
            if (solutionMethod == SolutionType.ShiftAndStack) {
                // Put the disks out
                for (int i = 0; i < diskCount; i++) {
                    MoveDisk(1, i + 2);
                }

               // display(true);

                // Stack the disks: skip the destination disk as it might already be there.
                for (int i = (diskCount + 1); i >= 2; i--) {
                    MoveDisk(i, towerCount);
                }
            }

            //  Solving a spread, stack, spread, stack
            else if (solutionMethod == SolutionType.BufferUnbuffer) {
                bufferDisks();
                //display(true);
                processBufferedDisks();
                //display(true);
            }
            //  Solve the buffered situation with a Hanoi solve in between.
            else if (solutionMethod == SolutionType.BufferHanoiUnbuffer) {
                bufferDisks();
                //display(true);
                processTowerHanoi(sourceTower, workingTower, destinationTower);
                processBufferedDisks();
                //display(true);
            }
            // Solve the traditional Hanoi Tower
            else if (solutionMethod == SolutionType.TradionalHanoi) {
                processTowerHanoi(sourceTower, workingTower, destinationTower);
            }

            success = true;
        }
        catch ( Exception ignored)
        {

        }
        return success;
    }

}
