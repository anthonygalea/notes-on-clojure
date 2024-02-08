;; # Getting Started
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.getting-started
  (:require
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 This notebook will give an introduction
;; >  to [Clojure](https://clojure.org):
;; > 1. In [data](#data) we give a high-level overview of some of the types of
;; > data available
;; > 1. Once we have covered data we will show how to use [functions](#functions)
;; > to operate on it
;; > 1. Finally we will show how we can give [names](#let) to things and create
;; > our own functions
;; >
;; > ℹ️ If you want to try the examples below, one way to do that is to [set up a
;; > Clojure REPL](https://clojure.org/guides/install_clojure). Once installed just
;; > run `clj`:
;; >
;; > ```sh
;; > $ clj
;; > user=> _
;; > ```

;; ## Data

;; If you type "Hello World" into the REPL and press the return key, it will
;; evaluate to itself:

;; ```clj
;; "Hello World"
;; ```

;; It's the same with other primitive values like booleans, integers, floats:

;; ```clj
;; true
;; false
;; 42
;; 1.61803
;; ```

;; They will all evaluate to themselves.

;; Besides primitive values we have collections, of which the most often used
;; are:
;; 1. Vectors (aka arrays)
;; 1. Maps (aka hashmaps or dictionaries)
;; 1. Sets

;; We can create vectors using `[]`:
^{:nextjournal.clerk/visibility {:result :hide}}
[5 9 7]

;; `{}` for maps:
^{:nextjournal.clerk/visibility {:result :hide}}
{"key1" "value1"
 "key2" "value2"}

;;`#{}` for sets:
^{:nextjournal.clerk/visibility {:result :hide}}
#{2 13 7 3 11 5}

;; Collections can contain different types:
^{:nextjournal.clerk/visibility {:result :hide}}
[5 "hello" false]

;; And they can be nested inside other collections:
^{:nextjournal.clerk/visibility {:result :hide}}
{"key1" "value1"
 "key2" #{1 true 42}}

;; That's all we need to know about data for now.

;; ## Functions

;; Once we have data, we can start calling functions on it. For example, say we
;; want to find the largest of two numbers:
(max 1 42)

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[200px] top-[-110px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "call the function max, passing it the arguments 1 and 42"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "when the expression returns we get 42"]])

;; > ℹ️ All of the functions you will see here have documentation. You can look
;; > at it by calling `doc`:
;; > ```clj
;; > user> (doc max)
;; > -------------------------
;; > clojure.core/max
;; > ([x] [x y] [x y & more])
;; >   Returns the greatest of the nums.
;; > ```

;; If we want to concatenate some strings:
(str "Hello " "world")

;; Add a new element to a vector:
(conj ["a" "b" "c"] "d")

;; Get the value for a particular key from a map:
(get {"a" 1 "b" 2} "a")

;; Add a new entry to a map:
(assoc {"a" 1 "b" 2} "c" 3)

;; Merge two maps together:
(merge {"a" 1 "b" 2} {"b" 3 "c" 4})

;; Remove consecutive duplicates:
(dedupe ["a" "a" "a" "b" "b" "c" "a"])

;; Get a map showing how often the elements appear:
(frequencies ["a" "a" "a" "b" "b" "c" "a"])

;; Check whether a number is positive:
(pos? 42)

;; Check whether a set contains a particular element:
(contains? #{2 3 5 7} 3)

;; > ℹ️ Note that `pos?` and `contains?` return a boolean. Functions that return
;; > a boolean are called **predicates**, and it is a convention to end their
;; > name with a `?`

;; ### let

;; So far all ***expressions*** have been very straightforward ones like:
(max 1 42)

;; But as you write more complex expressions you might find it useful to name
;; intermediate values in order to refer to them later. So for example we can
;; write:
(let [a 1
      b 42]
  (max a b))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[200px] top-[-160px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "a has the value 1"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "b has the value 42"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "so when we evaluate (max a b) we evaluate (max 1 42)"]])

;; We refer to `a` and `b` as ***symbols***, and say that `a` is bound to the
;; value `1`. We can have as many of these ***bindings*** as we need within the
;; same `let`:
(let [a 1
      b 42
      c 43]
  (max a b c))

;; Let's rewrite some of the expressions above using `let`:
(let [who "John"]
  (str "Hello " who))

(let [m {"a" 1 "b" 2}
      k "c"
      v 3]
  (assoc m k v))

(let [some-numbers [42 42 42 7 7 15]]
  (dedupe some-numbers))

;; ### def

;; Aside from `let`, we can also use `def`:
(def age 27)

;; Now that we have the symbol `age`, instead of writing `(inc 27)`, we can write:
(inc age)

;; The difference between `def` and `let` is that:

;; 1. `def` is global: so once we have defined a symbol we can reuse it
;; anywhere afterwards

;; 2. `let` is local: the symbols are no longer valid as soon as we are outside
;; the scope of the `let` block. So if we were to try to evaluate `a` **outside**
;; the `let` block above, we would see:

;; ```
;; Unable to resolve symbol: a in this context
;; ```

;; We say that the bindings in a `let` are locally scoped, and the ones in a
;; `def` are globally scoped.

;; ### fn

;; We can also create our own functions:
^{:nextjournal.clerk/visibility {:result :hide}}
(fn [first-name]
  (str "Hello " first-name))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[300px] top-[-75px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "make a function that takes one parameter (which we name first-name)"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "if the function is called, create a string saying hello and return the result"]])

;; If we combine this with a `def`:
^{:nextjournal.clerk/visibility {:result :hide}}
(def say-hello (fn [first-name]
                 (str "Hello " first-name)))

;; we can use it like any of the functions above:
(say-hello "Olivia")

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
