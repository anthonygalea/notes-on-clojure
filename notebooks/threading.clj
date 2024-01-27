;; # Threading
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.threading
  (:require
   [clojure.string :as string]
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 This notebook will focus on threading

;; So far we have typically called functions by wrapping their names in
;; parentheses like this:

(str "foo" "bar" "baz")

;; Below we show some additional ways of calling functions.

;; ### ->

;; Sometimes we need to perform multiple operations in succession on some
;; data. For example, say we want to multiply a number by `2`, subtract `5` from
;; the result, and finally increment it:
(inc (- (* 10 2) 5))

;; This nested form can become hard to read as the number of calls increase.

;; Using `->` (known as thread-first) we can write the expression such
;; that we can read the code top down in the same order it is executed:
(-> (* 10 2)
    (- 5)
    (inc))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[200px] top-[-160px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "(* 10 2) => 20"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "(- 20 5) => 15"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "(inc 15) => 16"]])

;; ### ->>

;; Sometimes we want to thread as the *last* parameter to the functions (instead
;; of the first):
(reverse (sort [1 -4 4 3 5 -1 7 6 2]))

;; We can do this using `->>` (known as thread-last):
(->> (sort [1 -4 4 3 5 -1 7 6 2])
     (reverse))

;; This is often the case when we are working with collections.

;; Let's look at one more example. We've
;; encountered [capitalize](/notebooks/strings#capitalize) before, but if
;; what we actually want is to capitalize each word:
(->> (string/split "each word should be capitalized" #"\b")
     (map string/capitalize)
     (string/join))

;; ### some->

;; Sometimes we want to run a series of functions but stop if one of them
;; returns `nil`. For example, say we want to check whether a string doesn't
;; start with `"foo"` and we start by writing this:

(-> "some string"
    (string/starts-with? "foo")
    (not))

;; This works, until we are passed a `nil`, because in that case `starts-with?`
;; throws a `NullPointerException`:
(try
  (-> nil
      (string/starts-with? "foo")
      (not))
  (catch NullPointerException npe
    (.getMessage npe)))

;; Using `some->`:
(some-> "some string"
        (string/starts-with? "foo")
        (not))

;; and if we get a `nil`:
(some-> nil
        (string/starts-with? "foo")
        (not))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[350px] top-[-155px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded.w-96
   "since we have a nil some-> does not invoke string/starts-with? so no exception is thrown"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[45px]"}
   "and yes, some-> does not invoke not either"]])

;; ### some->>

;; `some->>` is like `some->` but threads the data through the functions as the
;; last parameter instead.
(some->> (first [3 2 1])
         (- 2))

(try
  (->> (first nil)
       (inc))
  (catch NullPointerException npe
    (.getMessage npe)))

(some->> (first nil)
         (- 2))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[250px] top-[-135px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "(first nil) returns nil"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "so inc is not invoked"]])

;; ### cond->

;; Sometimes we have some data and want to *conditionally* apply a series of
;; functions to it:
(cond-> 45
  true    (- 5)
  (= 1 2) (* 2))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[250px] top-[-160px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "start with 45"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "the first condition is true so we subtract 5 from 45"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "the next condition (= 1 2) is false, so we don't multiply 40 by 2"]])

;; Note that `cond->` does *not* shortcircuit:
(cond-> 45
  true    (- 5)
  (= 1 2) (* 2)
  true    (* 3))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[250px] top-[-110px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "even though the previous condition was false we still multiply by 3"]])

;; ### cond->>

;; `cond->>` is like `cond->` but threads the data through the functions as the
;; *last* parameter instead:

(cond->> [1 2 3]
  true    (map inc)
  (= 1 2) (take 2))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[250px] top-[-160px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "start with the vector [1 2 3]"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "the first condition is true, so we evaluate (map inc [1 2 3]) giving [2 3 4]"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "the next condition is false, so (take 2 [2 3 4]) is not evaluated"]])

;; Like `cond->`, `cond->>` does *not* shortcircuit.

;; A common pattern is to use `cond->>` with an `opts` map:

^{:nextjournal.clerk/visibility {:result :hide}}
(defn process
  [numbers {:keys [just-odds?
                   increment?] :as _opts}]
  (cond->> numbers
    just-odds? (filter odd?)
    increment? (map inc)))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[430px] top-[-170px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "define a function called process"]
  [:div.border.border-emerald-400.absolute.px-1.rounded.w-80
   {:class "top-[25px]"}
   "which takes a sequence of numbers and a map with two options: just-odds? and increment?"]
  [:div.border.border-emerald-400.absolute.px-1.rounded.w80
   {:class "top-[100px]"}
   "if just-odds? is true apply (filter odd? numbers)"]
  [:div.border.border-emerald-400.absolute.px-1.rounded.w80
   {:class "top-[125px]"}
   "if increment? is true (map inc) on the previous step"]])

;; If we run this with `just-odds?` set to `true`:
(process [1 42 6 21] {:just-odds? true})

;; If we only want `increment?`:
(process [1 42 6 21] {:increment? true})

;; If we ask for both `just-odds?` and `increment?`:
(process [1 42 6 21] {:just-odds? true
                      :increment? true})

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[430px] top-[-131px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded.w-80
   "(filter odds) and then (map inc) because that is the order used in the cond->>"]])

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
