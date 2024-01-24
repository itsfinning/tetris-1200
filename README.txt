=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: juneguo
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays - The entire game board is represented by a 2D array, with different
  colors being different numbers and empty squares being represented by a "0" in the
  array. Other than that, the grid is also rendered by a 2D array changing colors
  based on the actual game state to save memory and time.

  2. File I/O - The user and import and export game states to practice different
  boards in Tetris that they're not comfortable at playing. The import and export
  functions work exactly how the user expects them to, with them being impossible to
  click without the game being paused, and with everything being saved-- including
  the game board, held block, and even the next blocks that are coming up.

  3. Inheritance and subtyping - All the child Tetrominoes have different shapes
  and although a large portion of their code can be generalized by a parent Tetromino class,
  their rotations, colors, and some of their other behavior must be still hard coded.

  4. JUnit testable component - Given all my code above, I was able to use an input
  file to supply my code with a set board state and check if it behaves as expected.
  My biggest concern was checking if rotations on the edges didn't crash the game, so
  I mostly implemented test cases to check if my Tetrominoes were coded correctly to
  avoid crashing and ArrayIndexOutOfBounds exceptions.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=
NOTE: The .tet files go in /src/java/org/cis1200/tetris/boards
!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=!=

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

orientationCoords holds the unique orientations for each block and rotation and
the parent Tetromino class has general methods that are overwritten in the child
classes along with general Tetromino properties that each child class should have.
The child Tetromino classes are used in the game itself, Blocks were implemented using
a modified version of the given code for the "Poison" in Mushroom of Doom
with GameObj, and GameCourt holds all the new code for the main logic and game of Tetris.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

Not in particular. Implementing my game went decently smooth from start to finish.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I really like how my design turned out in the end! I generalized as much as I could to
the parent Tetromino class to avoid mistypes while crashing, and I think all my classes
genuinely have a use in the final game. Other than that, I think there's a decent separation
of functionality with numerous classes being used to avoid all the code being compressed in
one file. Given a chance to refactor, I would most likely make my game in the exact same way.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

I made the images myself in Photoshop, and used the Java Oracle docs for everything else:
https://docs.oracle.com/javase/8/docs/api/javax/swing/JLabel.html