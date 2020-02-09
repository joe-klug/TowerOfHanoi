package TowerOfHanoi;

import java.io.*;
import java.util.*;

import static java.lang.System.exit;

public class Main {


    public static void main(String[] args) {
	// write your code here
        String towerInput;
        String diskInput;
        Integer towerCount = 0;
        Integer diskCount = 0;
        Boolean hasTowers = false;
        Boolean hasDisks = false;

        System.out.println( "\nTower of Hanoi" );
        System.out.println( "\tType Q or QUIT at the prompt to Quit" );
        System.out.println( "\tTowers range From 5 to 10");
        System.out.println( "\tDisk range from 3 to 40");

        // Prepare for keyboard reads
        InputStreamReader ir = new InputStreamReader( System.in );
        BufferedReader in = new BufferedReader(ir);


        while ( !hasTowers )
        {
            System.out.print( "Enter Number of Towers:" );
            try
            {
                towerInput = in.readLine();

                if (towerInput.equalsIgnoreCase("QUIT") || towerInput.equalsIgnoreCase("Q"))
                {
                    System.out.println("Quitting...  Bye!");
                    exit(1);
                }


                towerCount = Integer.parseInt( towerInput);

                if ( towerCount < 5 || towerCount > 10 )
                {
                    System.out.println("Bad Number of Towers Entered.  Try Again." );
                }
                else
                    hasTowers = true;
            }
            catch ( Exception e)
            {
                System.err.println( "Bad Number of Towers.   Try again or type QUIT.");
            }
        }

        while (!hasDisks)
        {
            System.out.print("Enter Number of Disks:");
            try
            {
                diskInput = in.readLine();

                if (diskInput.equalsIgnoreCase("QUIT") || diskInput.equalsIgnoreCase("Q")) { System.out.println("Quitting...  Bye!");
                    exit(1);
                }


                diskCount = Integer.parseInt( diskInput);


                if ( diskCount < 3 || diskCount > 40 )
                {
                    System.out.println("Bad Number of Discs Entered.  Try Again." );
                }
                else
                    hasDisks = true;

            }
            catch ( Exception e)
            {
                System.err.println( "Bad Number of Disks.   Try again or type QUIT.");
            }
        }

        Hanoi TowersOfHanoi = new Hanoi( towerCount, diskCount );
        TowersOfHanoi.display( true);
        TowersOfHanoi.solve();
        TowersOfHanoi.display( true);
    }
}
