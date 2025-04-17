;; # Keywords
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.keywords
  (:require
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 Keywords:
;; > * start with a leading colon
;; > * are often used as keys in maps
;; > * are like a symbol but always evaluate to themselves
;; > * have a high-performance equality check

;; The most frequent use of keywords is as keys in maps:
^{:nextjournal.clerk/visibility {:result :hide}}
{:language "Clojure"
 :family "Lisp"
 :designed-by "Rich Hickey"
 :first-appeared 2007}

;; For this map the keys are:
^{:nextjournal.clerk/visibility {:code :hide}}
(keys {:language "Clojure"
       :family "Lisp"
       :designed-by "Rich Hickey"
       :first-appeared 2007})

;; Note that they all start with a leading colon.

;; ### keyword?

(keyword? :foo)
(keyword? 42)

;; ### keyword

;; Sometimes we have a string and want to turn it into a keyword:
(keyword "foo")

;; ### name

;; Sometimes we need to turn a keyword into a string:
(name :foo)

;; ℹ️ Keywords can also be namespaced ex: `:foo/bar` but we will
;; cover [Namespaced keywords](/notebooks/namespaces#namespaced-keywords) in the
;; [Namespaces](/notebooks/namespaces) notebook.

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true
  :nextjournal.clerk/width :wide}
(i/whats-next)
