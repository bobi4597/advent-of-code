package com.github.bobi4597.adventofcode;

import java.util.Arrays;
import java.util.List;

import static com.github.bobi4597.adventofcode.BigIntCodeComputer.readInput;
import static com.github.bobi4597.adventofcode.BigIntCodeComputer.strToInput;
import static com.github.bobi4597.adventofcode.Starship.opositeDirection;

public class Year2019Day25 {

    public static void main(String[] args) {
        String inputString = readInput();
        System.out.printf("Part 1: %s\n", solve1(inputString));
    }

    private static String solve1(String inputString) {
        BigInt25Computer a = new BigInt25Computer(inputString);
        Starship starship = new Starship();

        BigRunResult runResult = a.run(null);
        StarshipCell startCell = StarshipCell.fromRunResult(runResult);
        starship.addCell(startCell);
        explore(a, startCell, starship, "");

        gotoSecurityCheckAndTakeAllItemsAlongTheWay(a);
        return tryToEnterInPressureSensitiveFloor(a);
    }

    private static void explore(BigInt25Computer a, StarshipCell cell, Starship starship, String directionFrom) {
        starship.visited.add(cell.name);

        for (String direction: cell.directions)  {
            if (direction.equals(opositeDirection.get(directionFrom))) {
                // do not go back to the cell where the droid came from
                continue;
            }
            BigRunResult runResult = a.run(strToInput(direction + "\n"));
            StarshipCell newCell = StarshipCell.fromRunResult(runResult);

            if (!starship.visited.contains(newCell.name)) {
                starship.addCell(newCell);
                starship.addEdge(cell, newCell, direction);
                if (newCell.text.split("==").length > 3) {
                    // we have reached the weight check without the correct weight, and were automatically moved back.
                    continue;
                }
                explore(a, newCell, starship, direction);
            }
            // go back
            a.run(strToInput(opositeDirection.get(direction)));
        }
    }

    private static void gotoSecurityCheckAndTakeAllItemsAlongTheWay(BigInt25Computer a) {
        List<String> commands = Arrays.asList(
            // explore west of Hull Breach
            "west", "west", "take loom", "east", "east", // Hull Breach
            // explore north-east of Stables
            "north", "north", "east", "take tambourine", "west", "take mutex", "south", // Stables
            // explore south of Holodeck
            "west", "south", "take hologram", "west", "take astronaut ice cream", "east", "south", "take mug", "north", "north", "take antenna", // Holodeck
            // explore north of Holodeck
            "north", "north", "north", "take space heater", "north", "east", // Security Check
            "inv",
            // we have 8 items
            "drop mutex", "drop loom", "drop tambourine", "drop hologram", "drop space heater", "drop antenna", "drop astronaut ice cream", "drop mug",
            "inv"
        );

        for (String command : commands) {
            a.run(strToInput(command + "\n"));
        }
        System.out.println("We are now in the Security Check room with all the items we could take with use (8 items).");
        System.out.println("Lets try all combinations of items and see if we can enter the Pressure Sensitive Floor room...");
        System.out.println();
    }

    /**
     * Try to enter the Pressure Sensitive Floor with every combination of items (2^8)
     */
    private static String tryToEnterInPressureSensitiveFloor(BigInt25Computer a) {
        String[] items = new String[] {"mutex", "loom", "tambourine", "hologram", "space heater", "antenna", "astronaut ice cream", "mug"};
        for (int i = 0; i < (1 << items.length); ++i) {
            // take the items
            System.out.printf("%3d) Try to enter with: ", i);
            for (int j = 0; j < items.length; ++j) {
                if ((i & (1 << j)) != 0) {
                    System.out.printf("%s ", items[j]);
                    a.run(strToInput("take " + items[j] + "\n"));
                }
            }
            System.out.println();
            // try to enter the room
            BigRunResult runResult = a.run(strToInput("east\n"));
            if (runResult.toAscii().contains("You may proceed.")) {
                runResult.printAscii();
                String response = runResult.toAscii();
                return response.substring(response.indexOf("on the keypad") - 9, response.indexOf("on the keypad") - 1);
            }
            // drop the items
            for (int j = 0; j < items.length; ++j) {
                if ((i & (1 << j)) != 0) {
                    a.run(strToInput("drop " + items[j] + "\n"));
                }
            }
        }
        return "password";
    }

//////////////////////////////////////////////////////////////////////////////////////////////
//    Correct combination of items: hologram, space heater, antenna, astronaut ice cream    //
//////////////////////////////////////////////////////////////////////////////////////////////
//        THE RESPONSE AFTER ENTERING THE ROOM WITH THE CORRECT COMBINATION OF ITEMS        //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    == Pressure-Sensitive Floor ==
//    Analyzing...
//
//    Doors here lead:
//        - west
//
//    A loud, robotic voice says "Analysis complete! You may proceed." and you enter the cockpit.
//    Santa notices your small droid, looks puzzled for a moment, realizes what has happened, and radios your ship directly.
//    "Oh, hello! You should be able to get in by typing 25166400 on the keypad at the main airlock."
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void tryToEnterInPressureSensitiveFloor2(BigInt25Computer a) {
        a.run(strToInput("take hologram\n"));
        a.run(strToInput("take space heater\n"));
        a.run(strToInput("take antenna\n"));
        a.run(strToInput("take astronaut ice cream\n"));
        BigRunResult runResult = a.run(strToInput("east\n"));
        runResult.printAscii();
    }

}
