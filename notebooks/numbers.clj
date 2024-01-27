;; # Numbers
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.numbers
  (:require
   [index :as i]))

;; > 👉 This notebook will focus on functions that operate on numbers

;; ## Arithmetic

;; ### +

;; Add numbers together:
(+ 1 2)

;; Note we're not limited to only two arguments:
(+ 3 4 5 6)

;; And we can also call `+` with no arguments:
(+)

;; ### -

;; Subtract two numbers:
(- 7 2)

;; ### inc

;; Increment a number:
(inc 42)

;; This is equivalent to `(+ 42 1)` but it is preferable to use `inc` in this
;; case.

;; We can also increment negative integers:
(inc -42)

;; and doubles:
(inc 42.1)

;; ### dec

;; Decrement a number:
(dec 42)

;; This is equivalent to `(- 42 1)` but it is preferable to use `dec` in this
;; case.

;; ### *

;; Multiply numbers together:
(* 3 2)

;; ### /

;; Divide two numbers:
(/ 18 6)

;; ### quot

;; If we divide `16` by `5` the quotient is `3`:
(quot 16 5)

;; ### rem

;; If we divide `16` by `5` the remainder is `1`:
(rem 16 5)

;; ### max

;; We can also find the greatest number of some numbers:
(max 19 4 42 5)

;; ### min

;; If we want the smallest number:
(min 19 4 42 5)

;; ## Random numbers

;; ### rand

;; `rand` generates a random number of type `double`. With no arguments the
;; random number is between `0` (inclusive) and `1` (exclusive):
(rand)

;; If we pass a number `n` the range is between `0` (inclusive) and
;; `n` (exclusive):
(rand 10)

;; ### rand-int

;; We can also generate a random number of type `int`:
(rand-int 100)

;; Passing an argument `n` is required in this case, and again the range is
;; between `0` (inclusive) and `n` (exclusive).

;; ## Predicates

;; There are also a bunch of functions that return `true` or `false` based on
;; some property of the number. Let's look at a few of them below:

;; ### pos?

;; We can check if a number is greater than zero:
(pos? 42)
(pos? -42)
(pos? 0)

;; ### neg?

;;  We can check if a number is less than zero:
(neg? -42)
(neg? 42)
(neg? 0)

;; ### zero?

;; Another predicate that is often used is `zero?`:

(zero? 0)

(zero? 42)

;; ## Compare

;; Below you will find examples for comparing two (or more) numbers. These can
;; be confusing if you're used to infix notation. It can be useful to think of
;; `<` as an upward slope and `>` as a downward slope:

;; 1. `(< 1 2 3)` is `true` because `1`, `2`, `3` is an ascending collection
;; of values
;; 1. `(> 3 2 1)` is `true` because `3`, `2`, `1` is a descending collection
;; of values

;; > ℹ️ Also, note that we can always express the same comparison using either
;; > `<` or `>`:
;; > ```clj
;; > (< 1 2) is the same as (> 2 1)
;; > ```
;; > If we generally pick `<` we make things easier for the reader.

;; ### <
(< 1 2)
(< 3 2)

;; ### >
(> 3 2)
(> 1 2 3)

;; ### <=
(< 1 1 3)
(<= 1 1 3)

;; ### >=
(> 3 3 2)
(>= 3 3 2)

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
