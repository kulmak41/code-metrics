To build the application use `./gradlew build`

To run the application use `./gradlew run --args="PATH_TO_THE_DIRECTORY_WITH_FILES"`

I approached the task by analysing abstract syntax trees of code files in the given directory.
To do that I got familiar with the JavaParser library.
I found a way to extend the VoidVisitorAdapter class to calculate the asked metrics while traversing the trees.
I think that other metrics can be computed in a similar way.