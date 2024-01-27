;; # Vectors
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.vectors
  (:require
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 This notebook will focus on vectors. Vectors:
;; > * store elements sequentially
;; > * allow efficient retrieval of elements using their index
;; > * can use all the functions we described
;; >   in [Collections](/notebooks/collections)
;; >   and [Sequences](/notebooks/collections) as well as the functions in this
;; >   notebook
;; >
;; > 1. First we will look at some additional ways we can [create](#create)
;; > vectors
;; > 1. Next we will look at some additional ways we can [process](#process)
;; > them
;; > 1. Finally we will look at some functions that are useful when we want
;; > a [vector returned](#vector-return) instead of a seq

;; ## Create

;; We often create vectors using the literal square-bracket syntax `[]`:

^{:nextjournal.clerk/visibility {:result :hide}}
["John" true 42]

;; ### vec

;; We can also create vectors from other collections using `vec`:
(vec #{"John" true 42})

;; ### vector

;; or using the elements directly:
(vector "John" true 42)

;; which returns a vector equal to the one built using the literal form:
(= (vector "John" true 42) ["John" true 42])

;; This can be useful when passed to higher-order functions, for example with
;; `map`:
(map vector [:a :b :c] [1 2 3])

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[350px] top-[-110px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "first (vector :a 1) then (vector :b 2) and finally (vector :c 3)"]])

;; ### vector-of

;; We can also constrain the type of the elements in a vector by using
;; `vector-of`:
(vector-of :int 42 43 44)

;; When elements are added into a vector produced by `vector-of` they are coerced:
(into (vector-of :int) "abc")

;; and a `ClassCastException` is thrown if it is not possible to coerce:
(try
  (vector-of :int :some-keyword)
  (catch ClassCastException e
    (.getMessage e)))

;; ### subvec

;; Sometimes we want a vector containing a slice of another vector:
(subvec [:a :b :c :d] 1 3)
^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[300px] top-[-110px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "take elements from index 1 (inclusive) to index 3 (exclusive)"]])

;; ### vector?

;; Lastly, if we want to check if something is a vector:
(vector? [1 2 3])

;; The entries of a map are also vectors:
(vector? (first {:a 1}))

;; If we pass something that is not a vector:
(vector? {:a 1})
(vector? #{1 2 3})

;; ## Process

;; Once we have a vector we can perform operations on it:

;; ### nth
(nth [0 1 2 3 4] 2)

;; Throws exception when out of bounds:
(try
  (nth [] 2)
  (catch Exception e
    (class e)))

;; unless provided with a default:
(nth [] 2 0)

;; Also works with strings:
(nth "abcdef" 2)

;; Doesn't work on associative data structures:
(try
  (nth {} 0)
  (catch UnsupportedOperationException e
    (.getMessage e)))

;; Returns `nil` when passed `nil`:
(nth nil 3)

;; ### peek
(peek ["John" "Alan" "Bob"])

;; ### pop
(pop ["John" "Alan" "Bob"])

;; ## Vector return

;; `map` and `filter` both return a [sequence](/notebooks/sequences):
(map odd? [1 2 3 4 5 6 7])

;; When we want a vector, we can wrap the call in `vec`:
(vec (map odd? [1 2 3 4 5 6 7]))

;; ### mapv

;; A better alternative is to use `mapv`:
(mapv odd? [1 2 3 4 5 6 7])

;; ### filterv

;; Similarly when filtering:
(filterv odd? [1 2 3 4 5 6 7])

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
