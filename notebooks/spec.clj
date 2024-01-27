;; # Spec
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.spec
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as gen]
   [clojure.spec.test.alpha :as stest]
   [clojure.string :as string]
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 Spec can be used for:
;; > 1. [describing the structure of data](#s/def)
;; > 1. [validating data](#s/valid?) (using the spec)
;; > 1. [explaining](#s/explain-data) why data is invalid
;; > 1. [generating](#s/gen) examples of the data
;; > 1. [describing the structure of functions](#s/fdef)
;; > 1. [generative testing](#s/exercise-fn)
;; >

;; We have seen before how we can use predicates to ask questions about
;; data. These questions can be about type, like "is it a number?":
(number? 42)

;; or "is it a string?":
(string? "foobar")

;; but types are not enough. We often want to ask more specific questions such
;; as: "is the number positive?":
(pos? 42)

;; or "is the number positive and also greater than 31?":
(and (pos? 42) (< 31 42))

;; or "is it a string starting with 'foo'?":
(and (string? "foobar")
     (string/starts-with? "foobar" "foo"))

;; We can use these predicates
;; with [clojure.spec](https://github.com/clojure/spec.alpha/blob/master/src/main/clojure/clojure/spec/alpha.clj).

;; > ℹ️ First we need to add a `require` in the `ns` form like
;; this:
;; >
;; > ```clj
;; > (ns your.ns
;; >   (:require [clojure.spec.alpha :as s]))
;; >```

;; ### s/valid?

;; Using `s/valid?` we can ask questions like the ones above:
(s/valid? number? 42)
(s/valid? #(and (pos? %)
                (< 31 %)) 42)

;; ### s/def

;; We can also give names to these predicates using `s/def`. For example, as the
;; old adage goes, a person's age is just a number:
^{:nextjournal.clerk/visibility {:result :hide}
  :nextjournal.clerk/no-cache true}
(s/def :person/age number?)

;; Once we have defined the spec `:person/age`, we can check if `42` is a valid
;; age:
(s/valid? :person/age 42)

;; or if we say a customer's name is a string:
^{:nextjournal.clerk/visibility {:result :hide}
  :nextjournal.clerk/no-cache true}
(s/def :customer/name string?)
;; we can ask if `"Mike"` is a valid customer name:
(s/valid? :customer/name "Mike")

;; Generally, a spec allows us to describe a set of possible values. The values
;; can be primitive, like the ones above, but they can also be more complex such
;; as collections.

;; ### s/keys

;; Say we want to use a map to represent an address like this:
^{:nextjournal.clerk/visibility {:result :hide}}
{:address/number  42
 :address/street  "La Rambla"
 :address/city    "Barcelona"
 :address/country "Spain"}

;; We can describe the shape of this map using `s/keys`:
^{:nextjournal.clerk/visibility {:result :hide}
  :nextjournal.clerk/no-cache true}
(s/def :customer/address (s/keys :req [:address/number
                                       :address/street
                                       :address/city
                                       :address/country]))

;; and then we can check if a particular address is valid:
(s/valid? :customer/address {:address/number  42
                             :address/street  "La Rambla"
                             :address/city    "Barcelona"
                             :address/country "Spain"})

;; If the required keys are not all there:
(s/valid? :customer/address {:address/number 42})

;; If we wanted to make country optional:
^{:nextjournal.clerk/visibility {:result :hide}
  :nextjournal.clerk/no-cache true}
(s/def :customer/address2 (s/keys :req [:address/number
                                        :address/street
                                        :address/city]
                                  :opt [:address/country]))

;; so then an address without a country is still valid:
(s/valid? :customer/address2 {:address/number 42
                              :address/street "La Rambla"
                              :address/city   "Barcelona"})

;; ### s/explain

;; If we want an explanation why `{:address/number 42}` is not a valid address
;; we can use `s/explain`:
^{:nextjournal.clerk/visibility {:result :hide}}
(s/explain :customer/address {:address/number 42})

;; This will print the following to standard out:

;;```clj
;; {:address/number 42} - failed: (contains? % :address/street) spec: :customer/address
;;```

;; which is saying that we're missing the key `:address/street` in the spec
;; `:customer/address`.

;; ### s/and

;; Earlier we gave the example:
(s/valid? #(and (pos? %)
                (< 31 %)) 42)

;; We can write this using `s/and`:
^{:nextjournal.clerk/visibility {:result :hide}
  :nextjournal.clerk/no-cache true}
(s/def :foo/bar (s/and pos? #(< 31 %)))

;; and then we can write:
(s/valid? :foo/bar 42)

;; ### s/coll-of

;; If we have a collection of elements such as a vector of keywords:
^{:nextjournal.clerk/visibility {:result :hide}}
[:a :b :c]

;; we can describe this using `s/coll-of`:
^{:nextjournal.clerk/visibility {:result :hide}
  :nextjournal.clerk/no-cache true}
(s/def ::vector-of-keywords (s/coll-of keyword?))

;; and like before we can validate some input:
(s/valid? ::vector-of-keywords [:a :b "c"])

;; and explain why it is not valid:
^{:nextjournal.clerk/visibility {:result :hide}}
(s/explain ::vector-of-keywords [:a :b "c"])

;; which tells us that `"c"` is not a keyword:
;;```clj
;; "c" - failed: keyword? in: [2] spec: :notebooks.spec/vector-of-keywords
;;```

;; ### s/cat

;; If we have sequential data such as a vector representing `x,y` coordinates:
^{:nextjournal.clerk/visibility {:result :hide}
  :nextjournal.clerk/no-cache true}
(s/def :geometry/coordinate (s/cat :x number? :y number?))

;; and validate like we did before:
(s/valid? :geometry/coordinate [10 20])

;; or explain:
^{:nextjournal.clerk/visibility {:result :hide}}
(s/explain :geometry/coordinate ["10" "20"])

;; which tells us that `"10"` is not a number:
;;```clj
;; "10" - failed: number? in: [0] at: [:x] spec: :geometry/coordinate
;;```

;; ### s/gen

;; If we add a dependency to
;; the [test.check](https://github.com/clojure/test.check) library and add a
;; `require` in the `ns` form like this:
;;
;; ```clj
;; (ns your.ns
;;   (:require [clojure.spec.gen.alpha :as gen]))
;; ```

;; we can get a generator by passing a spec to `s/gen`:
^{:nextjournal.clerk/visibility {:result :hide}}
(s/gen :person/age)

;; ### gen/generate

;; We can then generate values conforming to a spec using `generate`:
^{:nextjournal.clerk/no-cache true}
(gen/generate (s/gen :person/age))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[400px] top-[-110px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "generate takes a generator (which we created using s/gen)"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "and returns a value conforming to the spec"]])


;; ### gen/sample

;; While `generate` returns one value, `sample` returns several values:
^{:nextjournal.clerk/no-cache true}
(gen/sample (s/gen :person/age))

;; Based on this output we might notice that our spec for `:person/age` is
;; wrong, and we could decide to refine the spec to exclude `0`, negative
;; values, floats, and perhaps also numbers greater than `130`:
^{:nextjournal.clerk/visibility {:result :hide}
  :nextjournal.clerk/no-cache true}
(s/def :person/age2 (s/and pos-int? #(< % 130)))

;; When we sample the revised spec it looks better:
(gen/sample (s/gen :person/age2) 20)

;; ### s/fdef

;; We can also spec functions. Say we want to build a function that reverses a
;; string:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn reverse-string
  [s]
  (apply str (reduce conj () s)))

;; This reduces over the string `s` using `conj` and an empty list. Since `conj`
;; adds to a list at the front, this effectively returns the original characters
;; in reversed order:
(reduce conj () "abcd")

;; Some basic tests indicate this function is correct:
(reverse-string "foobar")
(reverse-string "")

;; Let's write a spec for `reverse-string`:
^{:nextjournal.clerk/visibility {:result :hide}}
(s/fdef reverse-string
  :args (s/cat :s string?)
  :ret string?
  :fn #(= (:ret %)
          (string/reverse (-> % :args :s))))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[420px] top-[-120px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "there should be one argument and it should be a string"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "the return should be a string"]
  [:div.border.border-emerald-400.absolute.px-1.rounded.w-80
   {:class "top-[50px]"}
   "when the function runs, the returned value should be the same as that produced by clojure.string/reverse"]])

;; ### stest/instrument

;; Once we spec a function we can leverage functionality in
;; `clojure.spec.test.alpha`.  To do this we need to add a `require` in the `ns`
;; form like this:
;;
;; ```clj
;; (ns your.ns
;;   (:require [clojure.spec.test.alpha :as stest]))
;; ```

;; then, we can request all calls to `reverse-string` to be automatically
;; validate:
^{:nextjournal.clerk/visibility {:result :hide}}
(stest/instrument `reverse-string)

;; So when we call `reverse-string` with a string we're good:
(reverse-string "foobar")

;; but if we accidentally use it with a number, we will get an exception telling
;; us that the call did not conform to the spec:
^{:nextjournal.clerk/auto-expand-results? true}
(try
  (reverse-string 42)
  (catch Exception e
    [(.getMessage e)
     (ex-data e)]))

;; ### stest/check

;; Once we spec a function we can also automatically test it by calling
;; `stest/check`:
^{:nextjournal.clerk/auto-expand-results? true}
(stest/check `reverse-string)

;; This tells us that `1000` calls were made with random strings, and for each
;; call `reverse-string` has returned the same result as
;; `clojure.string/reverse`.

;; ## Further reading

;; https://clojure.org/guides/spec

;; ## What's next?
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
