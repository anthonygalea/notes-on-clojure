;; # Control Flow
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.control-flow
  (:require
   [index :as i]
   [nextjournal.clerk :as clerk]))

^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(def mermaid-viewer
  {:transform-fn clerk/mark-presented
   :render-fn
   '(fn [value]
      (when value
        [nextjournal.clerk.render/with-d3-require
         {:package ["mermaid@10.2.1/dist/mermaid.js"]}
         (fn [mermaid]
           [:div
            {:ref
             (fn [el]
               (when el
                 (.then (.render mermaid (str (gensym)) value)
                        (fn [o]
                          (set! (.-innerHTML el) (aget o "svg"))))))}])]))})

;; > 👉 This notebook covers:
;; > 1. [Conditionals](#conditionals)
;; > 1. [Looping](#looping)

;; ## Conditionals

;; Clojure has a notion of "logical true" and "logical false" which stems from
;; the implementation of [if](#if) so we'll cover that first:

;; ### if

;; Let's start by looking at the docstring:

;; ```clj
;; user> (doc if)
;; -------------------------
;; (if test then else?)
;;
;; Evaluates test. If not the singular values nil or false, evaluates and
;; yields then, otherwise, evaluates and yields else. If else is not
;; supplied it defaults to nil.
;; ```

;; Let's break that down with some examples:

(if false
  "executed then"
  "executed else")

(if nil
  "executed then"
  "executed else")

;; So if the `test` evaluates to `false` or `nil`, the `else` clause will be
;; executed.

;; But for any other `test` that doesn't return `nil` or `false` it will
;; **always** execute the `then` clause:

(if (odd? 1)
  "executed then"
  "executed else")

;; Since `1` is an odd number, the call to `odd?` returns `true`, causing `if`
;; to return `"executed then"`.

(if {:name "John"}
  "executed then"
  "executed else")

;; A map is logically true, so is a vector:

(if [1 2 3]
  "executed then"
  "executed else")

;; This approach makes it easier to avoid complexity/bugs in conditionals
;; because we know that we will only ever execute an `else` clause for `nil` and
;; `false`, everything else will execute the `then` clause.

;; We describe `nil` and `false` as being "logically false" and anything else
;; as "logically true".

;; ### if-not

;; Sometimes we need to negate the test in an `if`:
(if (not true)
  "then clause"
  "else clause")

;; This can be rewritten more succinctly using `if-not`:
(if-not true
  "then clause"
  "else clause")

;; ### do

;; Sometimes we will want to execute several expressions. For example, say we
;; want to do several things in a branch of an `if`:
(if true
  (do
    "first this"
    "then this")
  "executed else")

;; > ℹ️ Note that `do` returns the result of the last expression, in this case
;; > `"then this"` which means that any expressions before (in this case
;; > `"first this"`) will be unused unless they perform side-effects.

;; ### when

;; So we covered `if`, why do we need `when`? Sometimes you might want to write
;; an `if` but you don't really have an `else` block:

(if (pos? 42)
  "then clause")

;; We can do this and if the test is false `if` will just return `nil`.

;; The downside of omitting the `else` block is that when you (or someone else)
;; reads that code again in the future, they might wonder whether the omission
;; was intentional or just a mistake. If we use `when` we make it explicit:

(when (pos? 42)
  "then clause")

;; > ℹ️ Note: "logical true" and "logical false" extends to `when` because the
;; > implementation uses `if`. We will learn more about this
;; > in [Macros](#notebooks/macros).

;; ### when-not

;; Sometimes we need to negate the test in a `when`:
(when (not true)
  "then clause")

;; This can be rewritten more succinctly using `when-not`:
(when-not true
  "then clause")

;; ### cond

;; To avoid nesting `if`s we have `cond`:
(let [n (rand)]
  (cond
    (< n 0.5) "less than 0.5"
    (> n 0.7) "greater than 0.7"
    :else     "somewhere between 0.5 and 0.7"))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[450px] top-[-205px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "generate a random number between 0 and 1"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[45px]"}
   "if n is less than 0.5 return \"less than 0.5\""]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[70px]"}
   "if n is less than 0.7 return \"less than 0.7\""]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[95px]"}
   "otherwise return \"somewhere between 0.5 and 0.7\""]])

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/with-viewer mermaid-viewer
  "flowchart LR
    rand --> C0.5{n < 0.5}
    C0.5 -->|Yes| 0.5[\"less than 0.5\"]
    C0.5 -->|No| C0.7{n > 0.7}
    C0.7 -->|Yes| 0.7[\"greater than 0.7\"]
    C0.7 -->|No| else[\"somewhere between 0.5 and 0.7\"]")

;; ### condp

;; If the condition that we want to switch on is always the same, we can use
;; `condp`. For example to determine the grade in an exam:
(let [points 75]
  (condp <= points
    85 "A"
    70 "B"
    50 "C"
    "F"))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/with-viewer mermaid-viewer
  "flowchart LR
    75 --> C{points}
    C -->|85 <= points| a[\"A\"]
    C -->|70 <= points| b[\"B\"]
    C -->|50 <= points| c[\"C\"]
    C -->|else| e[\"F\"]")

;; ### case

;; `case` is similar to [cond](#cond) except that there is no evaluation of
;; expressions. The matching is done with constants known at compile time:
(let [msg "hello"]
  (case msg
    "hello"   "you said hello"
    "goodbye" "you said goodbye"
    "you said something else"))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/with-viewer mermaid-viewer
  "flowchart LR
    hello --> C{msg}
    C -->|hello| h[\"you said hello\"]
    C -->|goodbye| g[\"you said goodbye\"]
    C -->|else| e[\"you said something else\"]")

;; ## Looping

;; ### loop/recur

;; Looping is not common in Clojure because there is very often a better way of
;; achieving the same thing using other constructs. But it is still possible to
;; loop. We'll demonstrate with some examples.

;; ```clj
;; (loop [i 1]
;;   (when (<= i 3)
;;     (println i)
;;     (recur (inc i))))
;; ```

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[250px] top-[-115px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "the first argument to loop is a binding context similar to what we provide to let"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "then we check whether i is less than or equal to 3"]
  [:div.border.border-emerald-400.absolute.px-1.rounded.w-96
   {:class "top-[50px]"}
   "if it is, we print i to standard out and call recur with (inc i) which puts us back at start of the loop with i bound to 2"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[92px]"}
   "this keeps happening until (recur 4), the condition (<= i 3) is false, so the loop terminates"]])

;; Let's look at another example. Say we get a vector with some numbers in it:
^{:nextjournal.clerk/visibility {:result :hide}}
[1 2 3 4 -1 5 6 7]

;; and we want to find out if there is a `-1` in it. We have already seen
;; in [Sequences](/notebooks/sequences) that we would do that using `some`:
(some #(= -1 %) [1 2 3 4 -1 5 6 7])

;; but as a means of demonstrating how `loop` works:
;; ```clj
;; (loop [n [1 2 3 4 -1 5 6 7]]
;;   (if (= -1 (first n))
;;     true
;;     (recur (rest n))))
;; ```

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[300px] top-[-113px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "n is bound to the vector of numbers [1 2 3 4 -1 5 6 7]"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "if the first element in the vector is -1 we return true (exiting the loop)"]
  [:div.border.border-emerald-400.absolute.px-1.rounded.w-96
   {:class "top-[70px]"}
   "if not we call recur with (rest n) putting us back at the start of the loop with n bound to the shorter vector [2 3 4 -1 5 6 7]"]])

;; If we look at the implementation of `some` we can see that it is very
;; similar, in fact it also uses `recur`:
;; ```clj
;; (defn some
;;   [pred coll]
;;   (when-let [s (seq coll)]
;;     (or (pred (first s))
;;         (recur pred (next s)))))
;; ```

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[340px] top-[-63px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "either the predicate is logically true for the first element"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[25px]"}
   "or we recur with the same predicate and a shorter collection"]])

;; Notice, that we have a `recur` but no `loop`. In this case `recur` will jump
;; to the function head.

;; ### dotimes

;; The first example we gave above with `loop` printed `1`, `2` and `3` to
;; standard out. We can do the same thing with `dotimes`:
^{:nextjournal.clerk/visibility {:result :hide}}
(dotimes [i 3]
  (println (inc i)))

;; In fact, the implementation of `dotimes` uses a loop/recur.

;; ### doseq

(doseq [e [1 6 1 8]]
  (println e))

;; ### while

;; If we want to repeatedly execute some code until some condition becomes
;; `false` or `nil`, we can use `while`:
(while (< 10 (rand-nth [2 42 31]))
  (println "looping"))

;; `while` is also implemented using a loop/recur.

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
