;; # Functions
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.functions
  (:require
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 This notebook will focus on how we can create our own functions

;; ### fn

;; When we want to create a function we need:
;; 1. a parameter list
;; 1. what to evaluate when that function is called

;; One way of doing this is with `fn`:
^{:nextjournal.clerk/visibility {:result :hide}}
(fn [n]
  (and (odd? n) (< n 6)))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[300px] top-[-75px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "make a function that takes one parameter (which we name n)"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "when it is called we will check that n is odd and less than 6"]])

;; For example, if we had to call this function with the argument `9`:
;; 1. `(odd? 9)` => `true`
;; 1. `(< 9 6)` => `false`
;; 1. `(and true false)` => `false`

;; Here's an example where this could be used as a predicate with `filter`:
(filter (fn [n]
          (and (odd? n) (< n 6)))
        [1 2 3 4 5 6 7 8 9 10])

;; That means: filter the numbers `1` to `10` for the ones that are odd and less
;; than 6.

;; ### #()

;; There is also a more concise form for making anonymous functions:
;; ```clj
;; #(and (odd? %) (> 6 %))
;; ```

;; We could then use this similarly to how we did with `fn`:
(filter #(and (odd? %) (< % 6))
        (range 20))

;; Notice how `n` in the `fn` example is replaced by `%`. If we had more than
;; one parameter we could refer to them with `%1`, `%2` etc..:
(map #(and (odd? %1) (< %2 6))
     [5 10 15] [5 6 7])

;; Note that we do not need to unnecessarily wrap a function in an anonymous
;; function:
(map #(inc %) [1 2 3])

;; we can just use the function name directly:
(map inc [1 2 3])

;; ### defn

;; Earlier we have seen how we can use `def` to refer to something by name:
(def x 42)

;; We can also use `def` to be able to refer to a function by name:
^{:nextjournal.clerk/visibility {:result :hide}}
(def f (fn [n]
         (and (odd? n) (< n 6))))

;; We can then call the function using the name:
(f 4)

;; or use the name in places where we previously used the function:
(filter f [1 2 3 4 5 6 7 8 9 10])

;; Instead of this combination of `def` and `fn` we typically use `defn`:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn odd-and-less-than-6?
  [n]
  (and (odd? n) (< n 6)))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[350px] top-[-97px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "the name of the function comes first: odd-and-less-than-6?"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "next is a vector of parameters: [n]"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "and finally what to execute when the function is called"]])

;; The result of the last statement is returned to the caller.

(odd-and-less-than-6? 3)

;; Here we get `true` because `3` is both odd and less than 6.

(odd-and-less-than-6? 4)

;; Here we get `false` because while `4` is less than `6`, it is not odd.

;; Functions can also have a string with documentation right after the function
;; name:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn odd-and-less-than-6?
  "Returns `true` if `n` is odd and less than 6"
  [n]
  (and (odd? n)
       (< n 6)))

;; Functions can also have multiple arities:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn odd-and-less-than-6?
  ([n]
   (and (odd? n)
        (< n 6)))
  ([n1 n2]
   (and (odd-and-less-than-6? n1)
        (odd-and-less-than-6? n2))))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[400px] top-[-170px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "first arity"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[75px]"}
   "second arity"]])

;; With this change we can now either call the function with one argument:
(odd-and-less-than-6? 42)

;; or with two arguments:
(odd-and-less-than-6? 3 5)

;; Finally we have variadic functions to be able to handle any number of
;; arguments:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn odd-and-less-than-6?
  [& xs]
  (every? #(and (odd? %) (< % 6)) xs))

;; The `&` indicates that `xs` will contain a sequence of arguments, so we can
;; now call:
(odd-and-less-than-6? 3 5 42)

;; Note that `apply` is often useful when writing variadic functions. Say we
;; want to write a function that, given a sequence of numbers, returns a message
;; stating what the largest number is:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn largest-number-message
  [& numbers]
  (str "The largest number is " (max numbers)))

;; We would then invoke it like this:
(largest-number-message 42 5 7 33)

;; That is not what we wanted because `max` does not accept a sequence. We need
;; to use `apply` to address this:
^{:nextjournal.clerk/visibility {:result :hide}}
(defn largest-number-message2
  [& numbers]
  (str "The largest number is " (apply max numbers)))

(largest-number-message2 42 5 7 33)

;; ### identity

;; Sometimes we want a function that does nothing else except return it's
;; argument:
(map identity [1 2 3 4])

;; ### constantly

;; If we want a function that will constantly return the same data, no matter
;; how it's called:
^{:nextjournal.clerk/visibility {:result :hide}}
(def stubborn (constantly 42))

;; without arguments:
(stubborn)

;; with arguments:
(stubborn "any" "arguments")

;; ### fnil

;; Sometimes we know we want to evaluate a function but if for whatever reason
;; the function were to receive a `nil` as its first argument we want to replace
;; it with some other default.

;; A typical use for this is with `conj`. Say we have a map in which (among
;; other things) in one of the keys we keep track of the set of numbers a user
;; has selected:

^{:nextjournal.clerk/visibility {:result :hide}}
{:selected #{2 3 5}}

;; In this case we might want to perform a `conj` when the user selects another
;; number:

(conj #{2 3 5} 7)

;; We could do that update like so:

(update {:selected #{2 3 5}} :selected conj 7)

;; but if the user hasn't yet selected anything and our map is empty:

^{:nextjournal.clerk/visibility {:result :hide}}
{}

;; when we execute the same call:
(update {} :selected conj 7)

;; This worked, but note that `:selected` is now a list not a set. This is
;; because `conj` returns a list when the collection is `nil`:
(conj nil 7)

;; To fix this we can replace `conj` with:
^{:nextjournal.clerk/visibility {:result :hide}}
(fnil conj #{})

;; This will return a function that will replace any `nil` first argument with
;; the empty set `#{}`:
(update {} :selected (fnil conj #{}) 7)

;; ### juxt

;; Sometimes we want to invoke a number of functions on the same argument(s) and
;; return each result in a vector:
[(pos? 42) (inc 42) (dec 42)]

;; `juxt` will create a function that does the same:
(let [f (juxt pos? inc dec)]
  (f 42))

;; One use case where `juxt` is handy is when combined with `group-by`. Say we
;; have some maps:
^{:nextjournal.clerk/visibility {:result :hide}}
(def people [{:name "Olivia" :surname "Brown" :country "UK"}
             {:name "Liam"   :surname "Brown" :country "France"}
             {:name "Amelia" :surname "Smith" :country "UK"}
             {:name "Oliver" :surname "Brown" :country "UK"}])

;; and we want to group them by `:country`:
^{:nextjournal.clerk/auto-expand-results? true}
(group-by :country people)

;; If we wanted to group first by `:country` and then by `:surname`:
^{:nextjournal.clerk/auto-expand-results? true}
(group-by (juxt :country :surname) people)

;; ### memoize

;; Sometimes we want a function that we know we will call several times with the
;; same input, and each subsequent call (with the same input) will return the
;; same output. In this case we might want to cache the return value of the
;; first call, so that we don't have to recompute it for subsequent calls.

;; We can demonstrate this with `rand`. Every time we call `rand` with the same
;; argument, each invocation will cause the body to run:
(rand 10)
^{:nextjournal.clerk/no-cache true}
(rand 10)
^{:nextjournal.clerk/no-cache true}
(rand 10)

;; so in this case we got a different random number for the second and third
;; calls. But if we `(memoize rand)`:
^{:nextjournal.clerk/visibility {:result :hide}}
(def rand-once (memoize rand))

;; when we call `rand-once` with `10`, the first time it will generate a random
;; number between `0` and `10`, store it in a cache, and return that to the
;; caller.
(rand-once 10)

;; But any further invocations with `10` will just retrieve the value from the
;; cache:
(rand-once 10)
(rand-once 10)

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
