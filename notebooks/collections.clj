;; # Collections
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.collections
  (:require
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 This notebook will introduce the most commonly used collection types:
;; > 1. [Vectors](#vectors)
;; > 1. [Maps](#maps)
;; > 1. [Sets](#sets)
;; > 1. [Lists](#lists)

;; ## Vectors

;; Vectors store elements sequentially:
^{:nextjournal.clerk/visibility {:result :hide}}
["foo" "bar" "baz"]

;; They are sometimes called arrays in other languages.

;; ### nth

;; They allow efficient retrieval of elements using their index:
(nth ["foo" "bar" "baz"] 2)

;; ### conj

;; When we add an element to a vector it grows at the end:
(conj ["foo" "bar" "baz"] 42)

;; ## Maps

;; Maps store map entries (key value pairs):
^{:nextjournal.clerk/visibility {:result :hide}}
{:a 1
 :b 2}

;; They are heavily used to represent entities in the domain. In situations
;; where in other languages we would create a class, in Clojure we very often
;; just use a map.

;; ### assoc

;; To add an entry to a map:
(assoc {:a 1 :b 2} :c 3)

;; ### dissoc

;; We can also remove an entry:
(dissoc {:a 1 :b 2} :b)

;; ### get

;; Maps also allow efficient retrieval of a value using the key:
(get {:a 1 :b 2} :a)

;; ## Sets

;; Sets store an unordered collection of unique elements:
^{:nextjournal.clerk/visibility {:result :hide}}
#{2 13 7 3 11 5}

;; ### contains?

;; We can check whether a set contains an element:
(contains? #{1 2 3} 1)

;; ### conj

;; We can also add an element to a set:
(conj #{1 2 3} 4)

;; ### disj

;; or remove an element:
(disj #{1 2 3} 3)

;; ## Lists

;; Lists store a linked list of elements, allowing for efficient insertion at
;; the head of the list:
^{:nextjournal.clerk/visibility {:result :hide}}
'(1 2 3)

;; The quote `'` you see before the first parenthesis is necessary to signal
;; that we are not trying to evaluate the function `1`. We will learn more about
;; this later.

;; Note that since a list is a chain of elements, finding the nth element is
;; inefficient (has a linear cost). In practice this means we use vectors more
;; often.

;; ### first

;; But getting the first element of a list is efficient:
(first '(:a :b :c))

;; Remember, as we said before, collections can contain different types:
^{:nextjournal.clerk/visibility {:result :hide}}
[5 "hello" false]

;; and they can be nested inside other collections:
^{:nextjournal.clerk/visibility {:result :hide}}
{"key1" "value1"
 "key2" #{1 true 42}}

;; ## Generic

;; There are a few functions that can be used with **all** these collections:

;; ### coll?

;; To check if something is a collection:
^{:nextjournal.clerk/visibility {:result :hide}}
(coll? 42)
(coll? "foo")
^{:nextjournal.clerk/visibility {:result :hide}}
(coll? [1 2 3])
^{:nextjournal.clerk/visibility {:result :hide}}
(coll? {:a 1 :b 2})
(coll? #{:a :b :c})

;; ### count

;; To count the number of elements in a collection:
(count [:a :b :c])

;; Also works with a set:
(count #{:a :b :c :d})

;; And with a map we are counting the entries i.e. key-value pairs:
(count {:a 1
        :b 2
        :c 3})

;; If we pass `nil`:
(count nil)

;; At this point we can introduce a high-level view of the collections:
^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:svg {:width 600 :height 400 :view-box "-300 -200 600 400"
        :class "fill-gray-700 dark:fill-white stroke-gray-700 dark:stroke-white"}
  [:defs
   [:circle {:id "region" :r 120 :stroke-width 5
             :class "fill-none"}]]
  [:g {:transform "translate(-75 50)"
       :class "stroke-red-500 fill-red-500"}
   [:use {:href "#region"}]
   [:text {:x -125 :y 140} "sequential?"]]

  [:g {:transform "translate(75 50)"
       :class "stroke-blue-500 fill-blue-500"}
   [:use {:href "#region"}]
   [:text {:x 40 :y 140} "associative?"]]

  [:g {:transform "translate(0 -50)"
       :class "stroke-green-500 fill-green-500"}
   [:use {:href "#region"}]
   [:text {:x 30 :y -130} "counted?"]]

  [:text {:x -95 :y -20} "list"]
  [:text {:x -25 :y 20} "vector"]
  [:text {:x 50 :y -20} "map"]
  [:text {:x -10 :y -120} "set"]])

;; ### counted?

;; All collections can be counted:
^{:nextjournal.clerk/visibility {:result :hide}}
(counted? '(1 2 3))
^{:nextjournal.clerk/visibility {:result :hide}}
(counted? [1 2 3])
^{:nextjournal.clerk/visibility {:result :hide}}
(counted? #{1 2 3})
(counted? {:a 1 :b 2})

;; ### sequential?

;; We say that a collection is `sequential` if it holds a series of values
;; without reordering them.

;; Lists and vectors are sequential:
^{:nextjournal.clerk/visibility {:result :hide}}
(sequential? '(1 2 3))
(sequential? [1 2 3])

;; but sets and maps are not:
^{:nextjournal.clerk/visibility {:result :hide}}
(sequential? #{1 2 3})
(sequential? {:a 1 :b 2})

;; ### associative?

;; Maps and vectors are associative:
(associative? {:a 1 :b 2})
(associative? [5 "hello" false])

;; Maps associate keys with values and vectors associate indexes with values.

;; ### empty?

;; To check whether a collection is empty:
(empty? [1 2 3 4 5])
(empty? [])
(empty? nil)

;; ### conj

;; To add (conjoin) an element to a collection:
(conj [1 2 3] 4)

;; or more than one element to a collection:
(conj [1 2 3] 4 5)

;; The position where the element is added is the most efficient one for the
;; type of the collection:
;; 1. for a vector, the addition happens at the end
;; 1. for a list, elements are added at the front
(conj '(1 2 3) 8)

;; For a map we need to pass a key value pair:
(conj {:a 1 :b 2} [:c 3])

;; When the collection is `nil` an empty list `()` is used by default:
(conj nil 42)

;; With no parameters:
(conj)

;; ### into

;; Sometimes we want to pour/conjoin the contents of one collection into
;; another:
(into [1 2 3 4] [3 4 5])

;; It's ok for the collections to be of different types:
(into #{1 2 3 4} [3 4 5])
^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[300px] top-[-110px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "when we pour into a set any duplicates get removed"]])

;; If we pour into `nil`, the elements are conjoined into a list, and since
;; `conj` adds elements to a list at the front, the order is effectively
;; reversed:
(into nil [1 2 3])

;; ### empty

;; If we have a collection, and we want an empty one of the same type:
(empty [:a :b :c])
(empty {:a 1 :b 2})
(empty #{:a :b :c})
(empty '(:a :b :c))
(empty nil)

;; ### not-empty

;; When we want to make sure we have a collection with something in it, we can
;; call `not-empty`:
(not-empty [1 2 3])

;; If the collection is empty we get `nil`:
(not-empty [])
(not-empty nil)

;; ## Immutability

;; Note that all collections are immutable, i.e. they cannot be updated in
;; place. Functions that accept data always return a copy, and never modify the
;; original. i.e. if we have a map:
^{:nextjournal.clerk/visibility {:result :hide}}
(def m {:a 1
        :b 2})

;; and we want the same map, but with an additional entry `:c`, we can get one:
(def m2 (assoc m :c 3))

;; but if something somewhere refers to the original map, it still has the
;; original contents:
m

;; Creating a copy for each change sounds inefficient. The reason it isn't is
;; because the underlying implementation uses [structural
;; sharing](https://en.wikipedia.org/wiki/Persistent_data_structure#Trees).
;; As a very simple example just to get the idea:
;; * if we have the vector `(def v [1 2 3])`
;; * and we create a new vector `(def v2 (conj v 4))`

;; it looks to us like we have this:
^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:svg {:width 400 :height 200 :view-box "-200 -100 400 200"
        :class "fill-gray-500 dark:fill-gray-300 stroke-gray-500 dark:stroke-gray-300"}
  [:defs
   [:circle {:id "node"
             :r 20 :transform "translate(5 -5)"
             :class "fill-none"}]]

  [:g {:transform "translate(-150 50)"}
   [:use {:href "#node"}]
   [:text "1"]]
  [:g {:transform "translate(-100 50)"}
   [:use {:href "#node"}]
   [:text "2"]]
  [:g {:transform "translate(-50 50)"}
   [:use {:href "#node"}]
   [:text "3"]]
  [:g {:class "fill-green-500 dark:fill-green-500 stroke-green-500 dark:stroke-green-500"}
   [:g {:transform "translate(-100 -50)"}
    [:rect {:width 50 :height 40 :fill :none :transform "translate(-20 -25)"}]
    [:text "v"]]
   [:g {:transform "translate(-50 0)"}
    [:line {:x1 -45 :y1 -35 :x2 -95 :y2 25}]
    [:line {:x1 -45 :y1 -35 :x2 -45 :y2 25}]
    [:line {:x1 -45 :y1 -35 :x2 5 :y2 25}]]]

  [:g {:transform "translate(0 50)"}
   [:use {:href "#node"}]
   [:text "1"]]
  [:g {:transform "translate(50 50)"}
   [:use {:href "#node"}]
   [:text "2"]]
  [:g {:transform "translate(100 50)"}
   [:use {:href "#node"}]
   [:text "3"]]
  [:g {:transform "translate(150 50)"}
   [:use {:href "#node"}]
   [:text "4"]]
  [:g {:class "fill-blue-500 dark:fill-blue-500 stroke-blue-500 dark:stroke-blue-500"}
   [:g {:transform "translate(70 -50)"}
    [:rect {:width 50 :height 40 :fill :none :transform "translate(-16 -25)"}]
    [:text "v2"]]
   [:line {:x1 75 :y1 -35 :x2 5 :y2 25}]
   [:line {:x1 75 :y1 -35 :x2 55 :y2 25}]
   [:line {:x1 75 :y1 -35 :x2 105 :y2 25}]
   [:line {:x1 75 :y1 -35 :x2 155 :y2 25}]]])

;; but in memory, we have something closer to this:
^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:svg {:width 300 :height 200 :view-box "-150 -100 300 200"
        :class "fill-gray-500 dark:fill-gray-300 stroke-gray-500 dark:stroke-gray-300"}
  [:defs
   [:circle {:id "node"
             :r 20 :transform "translate(5 -5)"
             :class "fill-none"}]]

  [:g {:transform "translate(-100 50)"}
   [:use {:href "#node"}]
   [:text "1"]]
  [:g {:transform "translate(-50 50)"}
   [:use {:href "#node"}]
   [:text "2"]]
  [:g {:transform "translate(0 50)"}
   [:use {:href "#node"}]
   [:text "3"]]
  [:g {:transform "translate(70 50)"}
   [:use {:href "#node"}]
   [:text "4"]]

  [:g {:class "fill-green-500 dark:fill-green-500 stroke-green-500 dark:stroke-green-500"}
   [:g {:transform "translate(-50 -50)"}
    [:rect {:width 50 :height 40 :fill :none :transform "translate(-20 -25)"}]
    [:text "v"]]
   [:line {:x1 -45 :y1 -35 :x2 -95 :y2 25}]
   [:line {:x1 -45 :y1 -35 :x2 -45 :y2 25}]
   [:line {:x1 -45 :y1 -35 :x2 5 :y2 25}]]

  [:g {:class "fill-blue-500 dark:fill-blue-500 stroke-blue-500 dark:stroke-blue-500"}
   [:g {:transform "translate(50 -50)"}
    [:rect {:width 50 :height 40 :fill :none :transform "translate(-16 -25)"}]
    [:text "v2"]]
   [:line {:x1 55 :y1 -35 :x2 -95 :y2 25}]
   [:line {:x1 55 :y1 -35 :x2 -45 :y2 25}]
   [:line {:x1 55 :y1 -35 :x2 5 :y2 25}]
   [:line {:x1 55 :y1 -35 :x2 75 :y2 25}]]])

;; To be explicit, the elements `1`, `2` and `3` are only stored *once*.

;; ## Further reading

;; https://clojure.org/reference/data_structures#Collections

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
