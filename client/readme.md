# Exam 2019 - CLIENT

## How to initialize
1. Open your terminal (or git bash or whatever) and navigate to the folder you want to store your main in.
2. Clone this project
3. Open IntelliJ and import project with existing sources and maven. Remember to import maven. This can be done by clicking the pop’up and selecting `import`. Else you can right click the `pom.xml` file and select `maven` and `import`
7. Run your Client by right clicking `Client.java` and run it's main method
8. If everything works, you should get nice menu.

# Note on trampolining
(optional, advanced, reading, may be skipped)
You may notice that the methods in MainView and in CustomerController have the return type Trampoline<Void> and
that they return something like Trampoline.more(() -> <<<method call>>>) instead of a normal value or just the method call.
This called 'trampolining' and is used to avoid uncontrollable stack growth. What happens is that the method call, to the view or controller method, is deferred
and handled after the return, thus avoiding the stack growing. You do not have to worry about this, just make sure all view and controller
methods return Trampoline<Void> and that they return Trampoline.more(() -> <<<call to view/controller method where the program continues>>>).

If you want to learn more about trampolines, you can read: https://medium.com/@johnmcclean/trampolining-a-practical-guide-for-awesome-java-developers-4b657d9c3076