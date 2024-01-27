;; # Logic
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.logic
  (:require
   [index :as i]))

;; > 👉 This notebook will focus on the boolean operators: `not`, `and`, `or`

;; ### not

(not false)
(not nil)

;; For everything else `not` will return `false`:
(not true)
(not "hello")

;; This matches what we introduced in [control-flow](/notebooks/control_flow)
;; and the reason is because `not` is implemented using `if`:

;; ```clj
;; user> (source not)
;; (defn not
;;   "Returns true if x is logical false, false otherwise."
;;   {:tag Boolean
;;    :added "1.0"
;;    :static true}
;;   [x] (if x false true))
;; ```

;; ### and

;; At first glance `and` works exactly like other languages, i.e. returns `true`
;; when all it's arguments are [logically true](/notebooks/control_flow):

(and true true)

;; and everything else returns `false`:
;; ```clj
;; (and false false)
;; (and true false)
;; (and false true)
;; ```

^{:nextjournal.clerk/visibility {:code :hide}}
(and false false)

;; But this is where the similarities end.

;; The first thing to note is that we can pass more than 2 arguments:
(and true true true true)
(and true false true)

;; The next thing is that those arguments don't need to be boolean values, they
;; can be anything:
(and true 42 false)

;; Remember: `true` and `42` are "logically true" but false is "logically false"
;; so we get `false`

(and 42 "John" true)

;; `42` `"John"` and `true` are all "logically true" so we get `true`

;; But note that `and` does not simply return `true` or `false`, it returns the
;; last "logically true" value, so for example:

(and true 42 "John")
(and true "John" 42)

;; Also, `and` will stop evaluating its arguments as soon as it meets the first
;; "logically false" value (and returns it):
(and true 42 nil false)

;; ### or

;; At first glance `or` works exactly like other languages, i.e. returns `false`
;; when all of it's arguments are [logically
;; false](/notebooks/control_flow):

(or false false)

;; and everything else returns `true`:
;; ```clj
;; (or true false)
;; (or false true)
;; (or true true)
;; ```

;; But similarly to `and`, we can pass more than 2 arguments:
(or true true false true)
(or false false false)

;; Note that `or` does not simply return `true` or `false`, it returns the
;; first "logically true" value it encounters (and will stop evaluating its
;; arguments):
(or false 42 "John")
(or false "John" 42)

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
