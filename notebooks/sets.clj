;; # Sets
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.sets
  (:require
   [clojure.set :as set]
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 This notebook will focus on sets. Sets:
;; > * store an unordered collection of unique values
;; > * can use all the functions we described
;; >   in [Collections](/notebooks/collections)
;; >   and [Sequences](/notebooks/collections) as well as the functions in this
;; >   notebook
;; >
;; > 1. First we will look at some additional ways we can [create](#create) sets
;; > 1. Next we will look at some set-specific [operations](#operations)
;; > 1. Finally we will look at some [relational-like operations](#relational)

;; ## Create

;; We often create sets using the literal syntax `#{}`:
^{:nextjournal.clerk/visibility {:result :hide}}
#{"John" true 42}

;; ### set

;; But we can also create sets from other collections using `set`:
(set [1 6 1 8 0 3 3])

;; Note that we have lost the duplicates, as well as any order in the original
;; vector.

;; ### hash-set

;; We can also create a set using the elements directly with `hash-set`:
(hash-set 1 6 1 8 0 3 3)

;; ### sorted-set

;; If we want ordering we have `sorted-set`, which will use the default
;; comparator `compare` to determine the order of its elements. For example in
;; the case of integers `compare` sorts in ascending order:
(sorted-set 1 6 1 8 0 3 3)

;; ### sorted-set-by

;; Sometimes we want a sorted set but also want to influence what the comparator
;; is (instead of the default `compare`):
(sorted-set-by > 1 6 1 8 0 3 3)

;; ## Operations

;; > ℹ️ There are several commonly used operations we can do on sets available in
;; > the [clojure.set](https://github.com/clojure/clojure/blob/master/src/clj/clojure/set.clj)
;; > namespace. To use it we need to add a `require` in the `ns` form like this:
;; >
;; > ```clj
;; > (ns your.ns
;; >   (:require [clojure.set :as set]))
;; >```

;; ### union

;; Take the elements of two (or more) sets and return another set containing all
;; the unique elements:

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:svg {:width 200 :height 150 :view-box "-100 -75 200 150"
        :class "fill-blue-500 stroke-black dark:stroke-gray-700"}
  [:defs
   [:circle {:id "region" :r 60 :stroke-width 5}]]
  [:use {:href "#region" :x -35}]
  [:use {:href "#region" :x 35}]
  [:use {:href "#region" :x -35 :fill "none"}]])

(set/union #{1 2 3} #{3 4 5})

;; ### difference

;; Remove all the elements of a set from another set:

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:svg {:width 200 :height 150 :view-box "-100 -75 200 150"
        :class "fill-white dark:fill-gray-900 stroke-black dark:stroke-gray-700"}
  [:use {:href "#region" :x -35 :fill "#3b82f6"}]
  [:use {:href "#region" :x 35}]
  [:use {:href "#region" :x -35 :fill "none"}]])

(set/difference #{1 2 3} #{3 4 5})

;; ### intersection

;; Take the elements of two (or more) sets and return another set containing all
;; those elements present in all of the sets:

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:svg {:width 200 :height 150 :view-box "-100 -75 200 150"
        :class "fill-none stroke-black dark:stroke-gray-700"}
  [:defs
   [:clipPath {:id "clip-path"}
    [:use {:href "#region" :x -70}]]]
  [:use {:href "#region" :x 35 :fill "#3b82f6" :clipPath "url(#clip-path)"}]
  [:use {:href "#region" :x -35}]
  [:use {:href "#region" :x 35}]])

(set/intersection #{1 2 3} #{3 4 5})

;; ### subset?

;; Is A a subset of B?

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:svg {:width 150 :height 150 :view-box "-75 -75 150 150" :stroke-width 5
        :class "fill-black dark:fill-gray-700 stroke-black dark:stroke-gray-700"}
  [:circle {:r 70 :class "fill-blue-500"}]
  [:text {:x 20 :y -40 :stroke-width 1} "B"]

  [:circle {:r 30 :cx -15 :cy 5 :fill "#22c55e"}]
  [:text {:x -10 :y 5 :stroke-width 1} "A"]])

(set/subset? #{1 2} #{1 2 3})

;; ### superset?

;; Is A a superset of B?

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:svg {:width 150 :height 150 :view-box "-75 -75 150 150" :stroke-width 5
        :class "fill-black dark:fill-gray-700 stroke-black dark:stroke-gray-700"}
  [:circle {:r 70 :fill "#22c55e"}]
  [:text {:x 20 :y -40 :stroke-width 1} "A"]

  [:circle {:r 30 :cx -15 :cy 5 :class "fill-blue-500"}]
  [:text {:x -10 :y 5 :stroke-width 1} "B"]])

(set/superset? #{1 2 3} #{1 2})

;; ### disj

;; Finally to remove an element from a set:
(disj #{1 6 8 0 3} 1)

;; ## Relational

;; Sometimes we have data in this shape:
(def people #{{:name "John" :age 32}
              {:name "Jane" :age 43}
              {:name "Jill" :age 56}})

;; Notice that this looks very similar to the data you could find in a
;; relational database:
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/width :prose}
(clerk/table (vec people))

;; The functions in this section perform relation-like queries on such data.

;; ### select

;; If we want to select the people whose age is greater than `40`:
(set/select (fn [m]
              (<= 40 (get m :age))) people)

;; ### rename

;; If we want to rename some of the keys:
^{:nextjournal.clerk/auto-expand-results? true}
(set/rename people {:name :first-name})

;; ### project

;; If we only want some of the keys we could write:
(into #{} (map (fn [m]
                 (select-keys m [:name]))) people)

;; or use `project`:
(set/project people [:name])

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
