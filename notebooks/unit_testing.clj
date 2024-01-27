;; # Unit Testing
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.unit-testing
  (:require
   [clojure.test :refer [are deftest is run-tests]]
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 This notebook covers how we can use unit testing to verify our code
;; > does what we expect

;; A test is a series of statements (assertions) about what a function *should*
;; do. The purpose of writing a test is to be able to easily verify these
;; statements at a later time by running the test.

;; We typically do this either:
;; 1. while implementing a function (this is known as test driven development or TDD)
;; 1. or later on after we modify a function due to some bugfix or changing requirement

;; This is an example of a test:
^{:nextjournal.clerk/visibility {:result :hide}}
(deftest addition
  (is (= 3 (+ 1 2)))
  (is (= 10 (+ 6 4))))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[250px] top-[-97px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "give the test a name, in this case \"addition\""]
  [:div.border.border-emerald-400.absolute.px-1.rounded.w-96
   {:class "top-[25px]"}
   "and write assertions about +, namely (+ 1 2) should return 3 and (+ 6 4) should return 10"]])

;; The test is written
;; using [clojure.test](https://clojure.github.io/clojure/clojure.test-api.html)
;; which we can use by adding a `require` in the `ns` form like this:
;;
;; ```clj
;; (ns your.ns
;;   (:require [clojure.test :refer [deftest is]]))
;; ```

;; ### is

;; We write assertions using `is`:
(is (= 1 1))

;; If the expression fails:
(is (= 2 1))

;; `is` prints a message:
;; ```clj
;; FAIL in () (NO_SOURCE_FILE:53)
;; expected: (= 2 1)
;;   actual: (not (= 2 1))
;; ```

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[350px] top-[-65px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "we expected 2 to be equal to 1"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "but it wasn't"]])

;; We can also provide a second argument to `is` containing a string describing
;; the assertion:

(is (= 4 (+ 1 2)) "2+1 should return 4")

;; Failures then include the string in the message:
;; ```clj
;; FAIL in () (NO_SOURCE_FILE:75)
;; 2+1 = 4
;; expected: (= 4 (+ 1 2))
;;   actual: (not (= 4 3)))
;; ```

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[330px] top-[-87px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "the message we provided to is can help us understand want went wrong"]])

;; ### deftest

;; So say we need to write a function that calculates the are of a square.

;; We can start by writing a function with the correct name but does nothing:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn area-of-square
  [width])

;; and then write a test containing an assertion with one of the things we
;; expect `area-of-square` to do:
^{:nextjournal.clerk/visibility {:result :hide}}
(deftest area-of-square-test
  (is (= 4 (area-of-square 2))))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[300px] top-[-73px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "the name for a test often matches the function being tested postfixed with -test"]])

;; `deftest` creates a function with the name `area-of-square-test` which we can
;; run:
^{:nextjournal.clerk/visibility {:result :hide}}
(area-of-square-test)

;; When we run this test it fails because we haven't implemented
;; `area-of-square` yet:
;; ```clj
;; fail in (area-of-square-test) (unit_testing.clj:102)
;; expected: (= 4 (area-of-square 2))
;;   actual: (not (= 4 nil))
;; ```

;; If we implement `area-of-square` correctly:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn area-of-square
  [width]
  (* width width))

;; and run the test again, it will pass i.e. we will not see the error message.

;; ### run-tests

;; As we write more tests it can be tedious to run them one at a time. We can
;; run all the tests in a namespace using `run-tests`:

;; ```clj
;; (run-tests 'some.namespace) 
;; ```

;; Or the tests in the current namespace (if we omit the argument):
^{:nextjournal.clerk/visibility {:result :hide}}
(run-tests)

;; For either case you should see a report similar to this:
;; ```clj
;; Ran 2 tests containing 3 assertions.
;; 0 failures, 0 errors.
;; ```

;; ### run-all-tests

;; To run all tests in all namespaces:

;; ```clj
;; (run-all-tests) 
;; ```

;; ### use-fixtures

;; Sometimes we need to run code before (and possibly after) a test runs. To do
;; this we can write a function (sometimes called a fixture):

;; ```clj
;; (defn some-fixture
;;   [t]
;;   (setup)
;;   (t)
;;   (teardown))
;; ```

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[300px] top-[-87px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "perform any setup here"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "evaluate the test"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "perform any teardown here"]])

;; Once we have created a fixture we can use `use-fixtures` to indicate that we
;; want to run it for every test in the current namespace:

;; ```clj
;; (use-fixtures :once some-fixture)
;; ```

;; ### are

;; Another way of writing assertions is using `are` which accepts a
;; ***template*** and an ***expression***:
(are [x y] (= x y)  
  3 (+ 1 2)
  5 (+ 2 3))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[300px] top-[-157px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "we provide a template (in this case [x y]) and an expression to evaluate"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "here x is 3 and y is (+ 1 2) so this assertion will evaluate (= 3 (+ 1 2))"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "here x is 5 and y is (+ 2 3)  so this assertion will evaluate (= 5 (+ 2 3))"]])

;; ## Further reading

;; * [clojure.test](https://github.com/clojure/clojure/blob/master/src/clj/clojure/test.clj#L20)
;; ns docstring

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
