;; # Sequences
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.sequences
  (:require
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 We have already seen some functions that we can use with all the
;; > collection types in [Collections](/notebooks/collections#generic). There
;; > are in fact many more that we will explain below.
;; >
;; > We will start by [introducing](#terminology) **sequences** and then proceed
;; > through sequence functions, logically grouped as follows:
;; >
;; > 1. create [shorter](#shorter) sequences
;; > 1. create [longer](#longer) sequences
;; > 1. [content tests](#content-tests)
;; > 1. [extract](#extract-element) one element
;; > 1. [process](#process) sequences
;; > 1. [group](#grouping) sequences
;; > 1. [rearrange](#rearrange) sequences
;; > 1. [generate](#generate) sequences

;; ## Terminology

;; A **sequence** is a series of values that can be sequentially traversed. It
;; can be either:
;; 1. finite: `1, 6, 1, 8, 13, 0, 3, 3, 9`
;; 1. or infinite: `0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144...`

;; We can produce a sequence in several ways, for example:
;; 1. performing some computation to generate the next value
;; 1. turning a collection into a sequence

;; We will see how to do both in the [Generate](#generate) section. The main
;; takeaway here though, is that many things can be looked at as though they
;; were a series of values:

;; * the Fibonacci sequence
;; * collections
;; * the result of a database query
;; * a stream of data coming from a file
;; * etc...

;; We can think of all of these as **sequences** and we can use the same API to
;; operate on *all* of them. Let's look at `map` and `filter` as examples:

;; ### map

;; We can create a new sequence by applying a function to each element of an
;; existing sequence:
(map inc [5 6 7])

;; > ℹ️ Note: `map` is known as a ***higher-order function*** i.e. a function
;; > which either takes another function as a parameter and/or returns a
;; > function as a result.

;; We can apply `map` to `n` sequences as well if we provide a function that
;; takes `n` parameters:
(map + [5 6 7] [8 9 10])

;; `+` is applied to `5` and `8` yielding `13`, then `6` and `9` yielding `15`
;; etc.

;; ### filter

;; We often want to go through the elements of a collection and keep some of
;; them. For example, if we have a bunch of numbers, and we want all the
;; positive ones:
(filter pos? [1 3 -1 0 5 -8])

;; If we receive the numbers in a set we can still use `filter`:
(filter pos? #{1 3 -1 0 5 -8})

;; and we can also `filter` a map:
(filter (fn [entry]
          (when (odd? (val entry))
            entry))
        {:a 1 :b 2 :c 3 :d 4 :e 5})

;; ## Shorter

;; We have already seen `filter` above. There are other functions that produce a
;; shorter sequence from the given sequence:

;; ### remove

;; If you find that you're trying to keep the elements that *don't* match some
;; condition, you can use `remove` instead of `filter`. For example:
(filter (fn [n]
          (= 5 n)) [1 3 -1 0 5 -8 5])

;; Say we change our mind and decide we don't want any of the 5s instead (we
;; want everything else) we could write:
(filter (fn [n]
          (not= 5 n)) [1 3 -1 0 5 -8 5])

;; Using `remove` instead makes the intent clearer:
(remove (fn [n]
          (= 5 n)) [1 3 -1 0 5 -8 5])

;; ### take

;; The first 3 elements:
(take 3 [1 3 -1 4 5 -8])

;; ### butlast

;; Everything but the last element:
(butlast [1 3 -1 4 5 -8])

;; ### drop

;; Drop 3 elements and take the rest:
(drop 3 [1 3 -1 4 5 -8])

;; ### take-last

;; Just the last 3 elements:
(take-last 3 [1 3 -1 4 5 -8])

;; ### take-nth

;; Take every 3rd element:
(take-nth 3 [1 3 -1 4 5 -8 7])

;; ### drop-last

;; Everything except the last 3 elements:
(drop-last 3 [1 3 -1 4 5 -8])

;; ### take-while

;; Take elements as long as they are positive:
(take-while pos? [1 3 -1 4 5 -8])

;; ### drop-while

;; Drop elements while they are positive and then get the rest:
(drop-while pos? [1 3 -1 4 5 -8])

;; ### distinct

;; Get the distinct elements:
(distinct [1 6 1 8 0 3 3 9 8 8 7 4 9])

;; ### dedupe

;; Remove consecutive duplicates:
(dedupe [1 6 1 8 0 3 3 9 8 8 7 4 9])

;; ### next

;; Get all the elements in a collection after the first:
(next [1 2 3])
(next [1])

;; `next` will call `seq` on its argument:

;; If there are no more elements, returns `nil`:
(next [])
(next nil)

;; If you want to make sure you always get a seq back use [rest](#rest) instead:
(rest [1])
(rest [])
(rest nil)

;; ## Longer

;; Given a sequence we can produce a longer sequence:

;; ### cycle

;; Make an infinite sequence by repeating the same sequence over and over again:
(take 10 (cycle [1 2 3]))

;; ### interleave

;; Take the first element from a number of collections, then the second, then
;; the third etc...:
(interleave [1 2 3] [10 20 30] [100 200 300])

;; ### interpose

;; Create a sequence using the elements from another sequence separated by a
;; separator:
(interpose "," ["foo" "bar" "baz"])

;; If we look at the [implementation](https://github.com/clojure/clojure/blob/master/src/clj/clojure/core.clj#L5266)
;; of `interpose` we can see that it is using `interleave` with `repeat` on the
;; separator and the collection, and finally dropping the first separator:
(drop 1 (interleave (repeat ",") ["foo" "bar" "baz"]))

;; ### concat

;; Put two sequences together:
(concat [1 3 -1] [5 -8 2])

;; ## Content tests

;; Given a sequence there are a few types of checks we can perform on the
;; contents:

;; ### every?

;; Check whether all values are odd:
(every? odd? [1 3 5])
(every? odd? [1 3 -1 4 5 -8])

;; Note that every statement about an empty collection is true:
(every? pos? [])
(every? neg? [])

;; ### not-every?

;; Check for the presence of at least one non-positive value:
(not-every? pos? [1 4 -3 2])

;; ### not-any?

;; Check for the absence of negative values:
(not-any? neg? [1 2 3 4])

;; ### some

;; Check if we have at least one negative value:
(some neg? [1 3 -1 4 5 -8])

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[330px] top-[-122px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "some evaluates (neg? 1) which returns false"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "since (neg? 1) is false, some tries the next element 3"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "since (neg? 3) is false, some tries the next element -1"]
  [:div.border.border-emerald-400.absolute.px-1.rounded.w-96
   {:class "top-[75px]"}
   "since (neg? -1) is logically true, some stops checking elements and returns the result of evaluating (neg? -1): true"]])

;; Let's look at another example:
(some #{-1 5} [1 3 -1 4 5 -8])

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[400px] top-[-117px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "we have a set of favorite numbers #{-1 5}"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "(#{-1 5} 1) is nil, so some tries the next element 3"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "since (#{-1 5} 3) is nil, some tries the next element -1"]
  [:div.border.border-emerald-400.absolute.px-1.rounded.w-80
   {:class "top-[75px]"}
   "since (#{-1 5} -1) is -1, some stops checking elements and returns the result of evaluating (#{-1 5} -1): -1"]])

;; ## Extract element

;; We can extract an element from a sequence:

;; ### second

(second [1 3 -1 4 5 -8])

;; ### last

(last [1 3 -1 4 5 -8])

;; ### nth

(nth [1 3 -1 4 5 -8] 2)

;; ### rand-nth

;; Get a random element:
(rand-nth [1 3 -1 4 5 -8])

;; ## Process

;; Process the elements of a sequence:

;; ### keep

;; Sometimes we find ourselves mapping over a collection and the mapping
;; function occasionally returns `nil`:
(map (fn [n]
       (when (odd? n) n)) [1 2 3 4 5 6])

;; If we wanted to drop these nils we could do:
(remove nil? (map (fn [n]
                    (when (odd? n) n)) [1 2 3 4 5 6]))

;; But with `keep` we can avoid the extra step:
(keep (fn [n]
        (when (odd? n) n)) [1 2 3 4 5 6])

;; So in that sense `keep` works like [map](#map). One difference is that `keep`
;; doesn't accept multiple collections like `map` does.

;; ### map-indexed

;; Sometimes we want to `map` but also need the index in the mapping function:
(map-indexed (fn [i e]
               (when (even? i) e)) [:foo "baz" 42 true "asdf"])

;; ### mapcat

;; Sometimes we follow a call to `map` with a call to `apply concat`:
(apply concat (map reverse [[3 2 1] [5 4] [7 6]]))

;; We can do the same with `mapcat`:
(mapcat reverse [[3 2 1] [5 4] [7 6]])

;; ### pmap

;; `pmap` behaves like `map` but the mapping function we provide is applied in
;; parallel:
(pmap inc [1 2 3])

;; ### for

;; `for` is not the for loop typically found in other languages. It is a list
;; comprehension that generates a **sequence**:
(for [i (range 10)]
  i)

;; We can let within a `for`:
(for [i (range 10)
      :let [x (* 2 i)]]
  x)

;; as well as conditionally pick values:
(for [i (range 10)
      :let [x (* 2 i)]
      :when (odd? i)]
  x)

;; Both of these examples can be implemented using sequence functions:
(map (fn [n]
       (* 2 n)) (range 10))

;; but the second `for` is arguably more readable than the alternative of
;; chaining `map` and `filter` together:
(map (fn [n]
       (* 2 n))
     (filter odd? (range 10)))

;; If you find yourself using `for` purely to apply a mapping function:
(for [x [1 2 3 4]]
  (inc x))

;; use `map` instead:
(map inc [1 2 3 4])

;; But let's look at another example where `for` might be preferred. Say we have
;; a vector with the coordinates `[0 0]` and we want to find all `8` neighbours
;; that surround this point: `[-1 -1] [-1 0] [-1 1] [0 -1]...`:

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:svg {:width 200 :height 200 :view-box "-100 -100 200 200"
        :class "fill-red-500 stroke-red-500"}
  [:line {:x1 0 :y1 -100 :x2 0 :y2 100 :stroke-width 2
          :class "fill-blue-500 stroke-blue-500"}]
  [:line {:x1 -100 :y1 0 :x2 100 :y2 0 :stroke-width 2
          :class "fill-blue-500 stroke-blue-500"}]
  [:circle {:cx 50 :cy 50 :r 3}]
  [:circle {:cx -50 :cy 50 :r 3}]
  [:circle {:cx 50 :cy -50 :r 3}]
  [:circle {:cx -50 :cy -50 :r 3}]
  [:circle {:cx 0 :cy 50 :r 3}]
  [:circle {:cx 0 :cy -50 :r 3}]
  [:circle {:cx 50 :cy 0 :r 3}]
  [:circle {:cx -50 :cy 0 :r 3}]
  [:circle {:cx 0 :cy 0 :r 3}]
  [:text {:x 7 :y 20 :class "fill-green-500 stroke-green-500"}
         "0,0"]])

(for [x [-1 0 1]
      y [-1 0 1]
      :when (not= x y 0)]
  [x y])

;; ### split-at

;; Split a sequence in two sequences based on an index:
(split-at 3 [1 6 1 8 0])

;; If we look at
;; the [implementation](https://github.com/clojure/clojure/blob/master/src/clj/clojure/core.clj#L3008)
;; of `split-at` we can see that it is just using `take` and `drop`:
[(take 3 [1 6 1 8 0]) (drop 3 [1 6 1 8 0])]

;; ### split-with

;; or based on a predicate:
(split-with pos? [1 3 -1 4 5 -8])

;; The first time `pos?` returns `false` is where the split is done. If you want
;; to keep splitting *every* time `pos?` returns something different
;; see [partition-by](#partition-by).

;; ### reduce

;; Another way of combining elements of a sequence with a function is via
;; `reduce`:
(reduce + [4 5 6 7])

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[250px] top-[-117px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "(+ 4 5) => 9"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "(+ 9 6) => 15"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "(+ 15 7) => 22"]])

;; We can provide an initial value to `reduce` just before the sequence:
(reduce + 10 [4 5 6 7])

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[250px] top-[-117px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "(+ 10 4) => 14"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "(+ 14 5) => 19"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "(+ 19 6) => 25"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[75px]"}
   "(+ 25 7) => 32"]])

;; Let's look at one more example:
(reduce (fn [acc x]
          (+ acc (get x :total)))
        0
        [{:total 42} {:total 10} {:total 8}])

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[450px] top-[-163px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "(+ 0 42) => 42"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "(+ 42 10) => 52"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "(+ 52 8) => 60"]])

;; ### reductions

;; If we use `reductions` instead of `reduce`, we get a sequence of all the
;; intermediate results, instead of only the final result:
(reductions + [4 5 6 7])

;; ### apply

;; Sometimes we know what function we want to call but the parameters are
;; generated dynamically elsewhere.

;; For example, say we want to concatenate some strings together with `str`:
(str "foo" "bar" "baz")

;; If we want to use `str` on a vector of strings generated somewhere else we
;; can use `apply`:
(apply str ["foo" "bar" "baz"])

;; We can also provide arguments before the vector:
(apply str "bam" "cat" ["foo" "bar" "baz"])

;; Note that `apply` cannot be supplied with a macro. If we try to do something
;; like:
;; ```clj
;; (apply or [false 42 true])
;; => Can't take value of a macro: #'clojure.core/or
;; ```

;; We can achieve what we want with [some](#some):
(some identity [false 42 true])

;; > ℹ️ We will cover `identity` in [Functions](/notebooks/functions#identity)

;; ## Grouping

;; Sometimes we want to group elements of a sequence.

;; ### group-by

;; We can group based on the return value of a function:
(group-by pos? [1 3 -1 4 5 -8])

;; Say we want to find all the anagrams in a vector of words. A word `x` is an
;; anagram of word `y` if all the letters in `x` can be rearranged in a
;; different order to form `y`. ex: "silent" is an anagram of "listen". So say
;; we have these words:
^{:nextjournal.clerk/visibility {:result :hide}}
["veer" "lake" "item" "kale" "mite" "ever"]

;; After applying the function the result should be:
^{:nextjournal.clerk/visibility {:result :hide}}
[["veer" "ever"]
 ["lake" "kale"]
 ["item" "mite"]]

;; We can achieve this by grouping the words by their sorted form:
^{:nextjournal.clerk/auto-expand-results? true}
(group-by sort ["veer" "lake" "item" "kale" "mite" "ever"])

;; and then taking the values from the resulting map:
(vals (group-by sort ["veer" "lake" "item" "kale" "mite" "ever"]))

;; ### partition

;; We can also group into partitions, for example in pairs:
(partition 2 [1 3 -1 4 5 -8])

;; but notice that if we don't have enough elements to form a partition they are
;; discarded:
(partition 4 [1 3 -1 4 5 -8])

;; ### partition-all

;; If we want the remaining elements:
(partition-all 4 [1 3 -1 4 5 -8])

;; ### partition-by

;; We can also partition by the return value of a function:
(partition-by odd? [1 3 -1 4 5 -8])

;; ### frequencies

;; Another grouping function allows us to count the number of times distinct
;; elements appear:
(frequencies [1 2 3 2 1 1 2 3 2])

;; ## Rearrange

;; ### sort

;; Sort a sequence:
(sort [1 3 -1 4 5 -8])

;; `sort` uses `compare` to determine how to sort the elements. We can pass a
;; different comparator if we want:
(sort > [1 3 -1 4 5 -8])

;; ### sort-by

;; We can also sort by the result of applying a function to the element:
(sort-by count ["baz" "foobar" "yo"])

;; and we can also pass a comparator:
(sort-by count > ["baz" "foobar" "yo"])

;; ### shuffle

;; Shuffle an existing sequence:
(shuffle [1 2 3 4 5])

;; ### reverse

;; Reverse an existing sequence:
(reverse [1 2 3 4 5])

;; ## Generate

;; There are various ways we can generate a sequence:

;; ### sequence

;; We can turn a collection into a sequence:
(sequence [1 2 3])

;; Note that it looks like a list but it is not a list:
(type '(1 2 3))
(type (sequence [1 2 3]))

;; ### seq

;; We can also turn collections into a sequence using `seq`:
(seq [1 2 3 4 5 6 7 8 9 10])

;; The difference is that `sequence` always returns a sequence even if the
;; collection is empty:
(seq #{})
(sequence #{})

;; Note that we can limit how many elements of a seq are printed in the REPL:
;; ```clj
;; (set! *print-length* 5)
;; ```

;; After doing that:
;; ```clj
;; (seq [1 2 3 4 5 6 7 8 9 10])
;; ```

;; will show:
;; ```clj
;; (1 2 3 4 5 ...)
;; ```

;; ### range

;; Generate a range of values:
(range 20)

;; Start from a value other than `0`:
(range 10 20)

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[350px] top-[-59px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "includes lowerbound (10) excludes upperbound (20)"]])

;; Use a different step value:
(range 10 20 2)

;; ### repeat

;; Create an infinite sequence with the same element:
(take 5 (repeat 42))

;; ### repeatedly

;; Create an infinite sequence with the same function applied over and over
;; again:
^{:nextjournal.clerk/auto-expand-results? true}
(take 5 (repeatedly rand))

;; Notice that in the last 2 cases we did not just call `repeat` or `repeatedly`
;; but we wrapped it in a call to `take`. This is because `repeat` and
;; `repeatedly` are functions that return `lazy sequences` i.e. sequences where
;; the elements are not calculated until they are actually needed. This is great
;; when it is expensive to compute the elements or where the sequences simply
;; wouldn't fit in memory.

;; ### lazy-seq

;; For example, the sequence of "multiples of 2": `2, 4, 6, 8...` is
;; infinite. Let's create it with `lazy-seq`:
(defn multiples
  ([n]
   (multiples n 1))
  ([n x]
   (lazy-seq (cons (* n x) (multiples n (inc x))))))

^{:nextjournal.clerk/visibility {:result :hide}}
(def multiples-of-2 (multiples 2))

;; This returns instantly because nothing is computed so far. But if we invoke
;; `take` the necessary values are computed:
(take 10 multiples-of-2)

;; ### iterate

;; This pattern of creating a sequence which starts with a value and calculating
;; each subsequent element by applying a function to the previous element can be
;; expressed using `iterate`:
(take 5 (iterate inc 1))

;; ## Summary

;; We can now expand the high-level picture we introduced in collections to
;; include lazy sequences:

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:svg {:width 600 :height 400 :view-box "-300 -200 600 400"
        :class "fill-gray-700 dark:fill-white stroke-gray-700 dark:stroke-white"}
  [:defs
   [:circle {:id "region" :r 120 :stroke-width 5
             :class "fill-none"}]]
  [:g {:transform "translate(-75 -50)"
       :class "stroke-red-500 fill-red-500"}
   [:use {:href "#region"}]
   [:text {:x -125 :y -130} "sequential?"]]

  [:g {:transform "translate(75 -50)"
       :class "stroke-blue-500 fill-blue-500"}
   [:use {:href "#region"}]
   [:text {:x 40 :y -130} "associative?"]]

  [:g {:transform "translate(0 50)"
       :class "stroke-green-500 fill-green-500"}
   [:use {:href "#region"}]
   [:text {:x 30 :y 140} "counted?"]]

  [:g {:transform "translate(-165 50)"
       :class "stroke-yellow-500 fill-yellow-500"}
   [:use {:href "#region"}]
   [:text {:x -80 :y 140} "seq?"]]

  [:text {:x -170 :y -30} "lazy-seq"]
  [:text {:x -95 :y 20} "list"]
  [:text {:x -25 :y -10} "vector"]
  [:text {:x 50 :y 20} "map"]
  [:text {:x -10 :y 120} "set"]])

;; Note that `seq?` returns `true` only for `lazy-seq` and `list`:
(seq? [1 2 3])

;; but all of the elements in the diagram are `seqable?`, for example for a
;; vector:
(seqable? [1 2 3])

;; which means that we can call `seq` on it:
(seq [1 2 3])

;; for which `seq?` returns `true`:
(seq? (seq [1 2 3]))

;; In fact all collections support `first`, `rest`, and `cons`:

;; ### first

;; The first element:
(first [1 3 -1 4 5 -8])

;; ### rest

;; Everything after the first element:
(rest [1 3 -1 4 5 -8])

;; ### cons

;; Build a new sequence by adding a new element to the front of an existing
;; sequence:
(cons 42 [1 3 -1 4 5 -8])

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
