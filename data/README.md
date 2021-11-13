The `old` directory contains tests conducted with the old answer to Question 3
which did not account for the chance of an unvisited cell being blocked--
i.e., `cell.getProbSuccess() = 0.5*cell.getProbGoal()`.

The directories `agent{6,7,8}` contain tests conducted with the new answer to Question 3
which do account for this chance--
i.e., `cell.getProbSuccess() = 0.35*cell.getProbGoal()`.
