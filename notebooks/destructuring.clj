;; # Destructuring
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.destructuring
  (:require
   [index :as i]))

;; > 👉 Destructuring allows us to bind names to parts of a piece of data

;; ## Associative

;; Say we want to write a function called `full-name`, which when given a map
;; containing the name and surname of an individual, returns a string with the
;; concatenation of the name and surname:

^{:nextjournal.clerk/visibility {:result :hide}}
(defn full-name [person]
  (str (get person :name) " " (get person :surname)))

(full-name {:name "John"
            :surname "McCarthy"})

;; With destructuring we can write the `full-name` function like this:

^{:nextjournal.clerk/visibility {:result :hide}}
(defn full-name [{:keys [name surname]}]
  (str name " " surname))

(full-name {:name "John"
            :surname "McCarthy"})

;; This is better because:
;; 1. the calls to `get` are gone reducing the noise in the function body
;; 1. it is very clear that `full-name` expects a map with two keys `:name` and
;; `:surname` just by looking at the argument list

;; ## Sequential

;; We refer to the destructuring in the example above as "associative"
;; destructuring, because it is applied to a map (which is an
;; associative (key-value) data structure), but we can also destructure
;; "sequential" things. For example, say we have a function that accepts a
;; vector of the shape `[x y]`, and returns the sum of `x` and `y`:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn add-vector [v]
  (+ (first v) (second v)))

(add-vector [3 2])

;; We can replace the parameter `v` with `[x y]` and the first and second values
;; of the vector will be bound to the names `x` and `y`, which we can then use
;; in the body of the function:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn add-vector [[x y]]
  (+ x y))

(add-vector [3 2])

;; This is better for several reasons:
;; 1. it is more readable because the calls to `first` and `second` are gone
;; 1. it is immediately clear that the argument expected is a vector of two
;; elements
;; 1. instead of `(first v)` and `(second v)` we have more meaningful names `x`
;; and `y`

;; > ℹ️ Note: The examples above have shown how to destructure in function
;; arguments, but it is also possible to destructure in a `let` block. So
;; instead of writing this:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn add-vector [v]
  (let [x (first v)
        y (second v)]
    (+ x y)))

(add-vector [3 2])

;; we can do:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn add-vector [v]
  (let [[x y] v]
    (+ x y)))

(add-vector [3 2])

;; ## Further reading

;; https://clojure.org/guides/destructuring

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
