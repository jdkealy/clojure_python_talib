## Python TAlib with Clojure Dockerfile 

This project is just a Dockerfile with a reagent-template, python, talib, numpy. 

Once you use the clojure REPL, nothing else compares. To be able to evaluate functions in your editor in an immutable way is something developers who use other languages just don't understand. 

Copy/pasting your code into a python/Ruby repl is not at all a comparable experience. The immutability constructs of clojure make REPL-driven development possible mostly because `(defonce (atom {}))` allows you to re-evaluate your functions, but not re-evaluate your variables that you don't want reloaded between refreshes. e.g. if you have a variable that was X days of stock ticker data and you adjusted a funciton in the same file as the variable was defined, that's no problem with Clojure. 

Now that you have your `(atom {})` of stock market data ready to play with, you want to use some of Clojure's amazing technical analysis libraries that are out there to get Bollinger Bands, Simple Moving Averages, etc, but oh... There are none. There are wrappers for Java technical analysis libraries that were last maintained 5 years ago, and Java interop in Clojure is... while a nice to have... not always that nice. 

The technical analysis libraries in PYTHON on the other hand and best in class. So what do we do? 

Thankfully, there's a library `https://github.com/clj-python/libpython-clj` that allows us to import python libraries in Clojure. 

