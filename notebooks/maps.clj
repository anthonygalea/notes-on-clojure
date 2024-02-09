;; # Maps
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.maps
  (:require
   [clojure.set :as set]
   [clojure.walk :as walk]
   [index :as i]))

;; > 👉 This notebook will focus on maps. Maps:
;; > * store map entries (key value pairs)
;; > * allow efficient retrieval of values using their keys
;; > * can use all the functions we described
;; >   in [Collections](/notebooks/collections)
;; >   and [Sequences](/notebooks/collections) as well as the functions in this
;; >   notebook
;; >
;; > 1. First we will look at some additional ways we can [create](#create) maps
;; > 1. Next we will look at various ways we can [access](#access) their
;; > contents
;; > 1. Then we will look at some additional functions for [processing](process)
;; > maps
;; > 1. Finally we will look at a couple of useful functions in
;; > the [clojure.walk](#clojure.walk) and [clojure.set](#clojure.set)
;; > namespaces that also operate on maps

;; ## Create

;; We often create maps using the literal syntax `{}`:
^{:nextjournal.clerk/visibility {:result :hide}}
{:a 1 :b 2}

;; ### hash-map

;; But we can also create maps using the elements directly using `hash-map`:
(hash-map :a 1 :b 2)

;; In such cases the literal syntax `{}` is preferable because it's less
;; verbose, but `hash-map` can be useful when passed to higher-order
;; functions. Say we have a vector and we want to make a map out of it:
(apply hash-map ["k1" "v1" "k2" "v2"])

;; Note that we need to ensure we have an even number of arguments, otherwise
;; we will get an `IllegalArgumentException`:
(try
  (hash-map :a 1 :b)
  (catch IllegalArgumentException e
    (.getMessage e)))

;; Also, if the key appears more than once, the last value will be retained:
(hash-map :a 1 :b 2 :a 3)

;; ### array-map

;; Just like  `hash-map`, `array-map` produces a map:
(hash-map)
(array-map)

;; The difference lies in the specific type returned:
(type (hash-map :a 1 :b 2))
(type (array-map :a 1 :b 2))

;; `PersistentArrayMap` is an implementation that has `O(n)` lookup. Another
;; property of this implementation is that it maintains insertion order during
;; iteration:

;; In fact when we create a small map using the literal syntax `{}` we also get
;; a `PersistentArrayMap`:
(type {:a 1 :b 2 :c 3 :d 4 :e 5 :f 6 :g 7 :h 8})

;; but as soon as we go past 8 entries we get a `PersistentHashMap`:
(type (assoc {:a 1 :b 2 :c 3 :d 4 :e 5 :f 6 :g 7 :h 8} :i 9))

;; ### sorted-map

;; If we want to retain key ordering we can use `sorted-map`:
(sorted-map "c" 3 "a" 42 "d" 4 "b" 7)
(sorted-map 3 "c" 42 "a" 4 "d" 7 "b")

;; ### sorted-map-by

;; `sorted-map` uses the default comparator `compare` to maintain
;; order. `sorted-map-by` can be used to pass a different comparator, for
;; example:
(sorted-map-by > 3 "c" 42 "a" 4 "d" 7 "b")

;; ### zipmap

;; If we have two collections, one for keys, and another for values and we want
;; to make a map out of them we can use `zipmap`:
(zipmap [:a :b :c] [1 2 3])

;; If there are more values than keys the extra values are ignored:
(zipmap [:a :b :c] [1 2 3 4])

;; If there are more keys than values the extra keys are ignored:
(zipmap [:a :b :c :d] [1 2 3])

;; ## Access

;; Once we have a map there are various ways we can retrieve its contents:

;; ### get

;; We can get a value from a map using `get`:
(get {:a 1 :b 2} :a)

;; ### get-in

;; and if the map is nested using `get-in`:
(get-in {:a 1 :b {:c 2}} [:b :c])

;; ### find

;; If you want to get the entire entry, use `find` instead of `get`:
(find {:a 1 :b 2} :a)

;; ### key

;; If we have an entry we can get its key:
(let [e (first {:a 1 :b 2})]
  (key e))

;; ### val

;; or its value:
(let [e (first {:a 1 :b 2})]
  (val e))

;; ### contains?

;; If we just want to check whether a map contains a key:
(contains? {:a 1 :b 2} :a)

;; ### keys

;; If we want all the keys:
(keys {:a 1 :b 2})
 
;; ### vals

;; If we want all the values:
(vals {:a 1 :b 2})

;; ### select-keys

;; Sometimes we only want some of the entries of a map:
(select-keys {:a 1 :b 2 :c 3 :d 4} [:a :c])

;; ## Process

;; ### assoc

;; To add (associate) an entry to a map:
(assoc {:a 1 :b 2} :c 3)

;; ### assoc-in

;; If we have a nested map:
(assoc-in {:a 1 :b {:c 2 :d 3}} [:b :c] 4)

;; ### dissoc

;; If we have a map and we want the same map with a `key` removed (dissociated):
(dissoc {:a 1 :b 2} :a)

;; ### update

(update {:a 1 :b 2} :a inc)

;; ### update-in

;; and if the map is nested:
(update-in {:a 1 :b {:c 2 :d 3}} [:b :c] inc)

;; ### merge

(merge {:a 1 :b 2} {:b 3 :c 4})

;; Note that deep maps are not merged recursively, so in this example `:b` is
;; lost and `:c` is only there because it's in the second map:
(merge {:a {:b 2 :c 3}} {:a {:c 4 :d 5}})

;; If you need to do a deep merge one alternative is to
;; use [deep-merge](https://cljdoc.org/d/medley/medley/1.4.0/api/medley.core#deep-merge)
;; from the [medley](https://cljdoc.org/d/medley/medley/1.4.0/doc/readme)
;; library.

;; ### merge-with

;; We saw in [merge](#merge) that when given two (or more) maps, the entry in
;; the last map is the one retained:
(merge {:a 1 :b 3} {:b 2 :c 3})

;; Notice how the result contains `[:b 2]`. There are cases however where we
;; want to specify what happens when the same key is present in multiple maps.
(merge-with + {:a 1 :b 3} {:b 2 :c 3})

;; So here the values in the entries for key `:b` were combined with `+` so we
;; got `[:b 5]` in the result. If we use `-` as the combining function we'll get
;; `[:b 1]` because `3` - `2` is `1`:
(merge-with - {:a 1 :b 3} {:b 2 :c 3})

;; If the values in the maps were vectors we might use `into` as the combining
;; function:
(merge-with into
            {:a [1 2 3] :b [4 5]}
            {:b [6 7] :c [8 9]})

;; Another commonly used combining function is `merge`:
(merge-with merge {:a {:x 1 :y 2}} {:a {:y 6 :z 7}})

;; Notice how the values are maps and we are saying that if a key occurs more
;; than once we want to combine the values with `merge` so the value for `:a` is
;; obtained by doing:
(merge {:x 1 :y 2} {:y 6 :z 7})

;; ### reduce-kv

(reduce-kv (fn [m k v]
             (assoc m k (inc v))) {} {:a 1 :b 2 :c 3})

;; ### update-vals

;; Update all the values in a map using a function:
(update-vals {:a 1 :b 2 :c 3} inc)

(update-vals {:foo {:a 1 :b 2 :c 3}
              :bar {:a 4 :b 5 :c 6}} #(select-keys % [:a :b]))

;; ### update-keys

;; Update all the keys in a map using a function:
(update-keys {:a 1 :b 2 :c 3} name)

;; Note that this is not recursive. If that's what you want
;; see [stringify-keys](#stringify-keys) below.

;; ## clojure.walk

;; There are two functions in the `clojure.walk` namespace that operate on maps
;; that are quite useful:

;; ### keywordize-keys

;; If we have a map where the type of the keys are strings and we want keywords
;; instead:
(walk/keywordize-keys {"name" "John"
                       "surname" "McCarthy"})

;; Note that this applies to nested maps as well:
^{:nextjournal.clerk/auto-expand-results? true}
(walk/keywordize-keys {"name" "John"
                       "address" {"number"  42
                                  "street"  "La Rambla"
                                  "city"    "Barcelona"
                                  "country" "Spain"}})

;; ### stringify-keys

;; If we have a map where the type of the keys are keywords and we want strings
;; instead:
(walk/stringify-keys {:name "John"
                      :surname "McCarthy"})

;; Like `keywordize-keys` this applies to nested maps as well:
^{:nextjournal.clerk/auto-expand-results? true}
(walk/stringify-keys {:name "John"
                      :address {:number  42
                                :street  "La Rambla"
                                :city    "Barcelona"
                                :country "Spain"}})

;; ## clojure.set

;; There are two functions in the `clojure.set` namespace that operate on maps
;; that are quite useful:

;; ### rename-keys

;; Sometimes we are given a map and we would like to rename its keys:
(set/rename-keys {:name "John"
                  :surname "McCarthy"}
                 {:name :first-name
                  :surname :last-name})

;; ### map-invert

;; Sometimes we want have a map and we want it "inverted" i.e. with the vals
;; mapped to the keys:
(set/map-invert {:name "John"
                 :surname "McCarthy"})

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
