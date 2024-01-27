;; # Namespaces
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.namespaces
  (:require
   [index :as i]
   [nextjournal.clerk :as clerk]))

;; > 👉 Namespaces allow us to manage complexity by grouping together related
;; > functions and data, keeping related things together and unrelated things
;; > apart

;; ## Creating a namespace

;; ### ns

;; We typically create a namespace by using `ns` at the top of a file:

(ns my-project.my-file
  "documentation goes here")

;; This would land in a file called `my_project/my_file.clj`. Note that the dots
;; become slashes and the dashes become underscores.

;; Every time we use `def` or `defn` inside this file a **var** is created
;; *inside* the namespace:
(def foo 42)

;; Within the same namespace we can refer to any other var directly:
(def bar (inc foo))

^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(ns my-project.my-file
  (:require
   [nextjournal.clerk :as clerk]))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[250px] top-[-110px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "we can refer to foo directly"]])

(defn my-inc [n]
  (inc n))

;; Notice the full name for `my-inc` is `my-project.my-file/my-inc`. The first
;; part (before the `/`) is the name of the namespace. This means that we can
;; define another function called `my-inc` in another namespace, and be able to
;; refer to either one using it's full name.

;; ### ns-publics

;; We can list the vars in a namespace:
(ns-publics 'my-project.my-file)

^{:nextjournal.clerk/auto-expand-results? true}
(ns-publics 'clojure.set)

^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(ns notebooks.namespaces)

;; ## Requiring namespaces

;; If we need functions from namespaces other than `clojure.core`, we can say so
;; in the ns form like this:

;; ```clj
;; (ns my-project.my-file
;;   "documentation goes here"
;;   (:require
;;    [clojure.string]))
;;
;; (clojure.string/starts-with? "foo" "bar")
;; ```

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[400px] top-[-90px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded
   "the namespace we are requiring is clojure.string"]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[50px]"}
   "call the starts-with? function in the namespace clojure.string"]])

;; ## Aliases

;; Writing out the full namespace name can get annoying, but we can use aliases
;; instead:

;; ```clj
;; (ns my-project.my-file
;;   "documentation goes here"
;;   (:require
;;    [clojure.string :as string]))
;;
;; (string/starts-with? "foo" "bar")
;; ```

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
 [:div.absolute
  {:class "left-[340px] top-[-88px] w-full text-xs font-sans"}
  [:div.border.border-emerald-400.absolute.px-1.rounded.w-96
   "the namespace we are requiring is clojure.string and the alias we are going to use to refer to it is \"string\""]
  [:div.border.border-emerald-400.absolute.px-1.rounded
   {:class "top-[47px]"}
   "use the alias \"string\" to call the starts-with? function in clojure.string"]])

;; ## Multiple requires

;; When we require multiple namespaces it is nice to sort them alphabetically so
;; we can find one easily:

;; ```clj
;; (ns my-project.my-file
;;   "documentation goes here"
;;   (:require
;;    [clojure.java.io :as io]
;;    [clojure.set :as set]
;;    [clojure.string :as string]
;;    [clojure.walk :as walk]))
;; ```

;; ## How to pick an alias

;; Generally I like the straightforward guideline of removing the leading parts
;; like we did above i.e. we drop "clojure.java."  from `clojure.java.io` and
;; what remains "io" is the alias.

;; When this fails because there are multiple namespaces that end with the same
;; part, just keep enough of the leading parts to make them unique:

;; ```clj
;; (ns my-project.my-file
;;   "documentation goes here"
;;   (:require
;;    [foo.first.baz :as first.baz]
;;    [foo.second.baz :as second.baz]
;; ```

;; I deviate from this guideline for namespaces that have an established
;; convention such as `[clojure.spec :as s]`. I think it is also justified to
;; use a short alias for namespaces that are very frequently used.

;; ## Private functions

;; ### defn-

;; If you want a function to be private you can use `defn-`:

^{:nextjournal.clerk/visibility {:result :hide}}
(defn- less-than-6?
  [n]
  (< n 6))

;; This makes the function local to the current namespace.

;; ## Namespaced keywords

;; Information systems often use properties with the same name for different
;; data. For instance, we could have a map with someone's twitter username and
;; id:
(def twitter-account {:username "twitter-username"
                      :id 42})

;; and another map for the same user's github account:
(def github-account {:username "github-username"
                     :id 56})

;; If we wanted a map with all this information, and try to merge the two maps,
;; values from the second map would overwrite values in the first map:
(merge twitter-account github-account)

;; Namespaced keywords allow us to use the same name for the key and still be
;; able to distinguish between the two:
^{:nextjournal.clerk/auto-expand-results? true}
(merge {:twitter/username "twitter-username"
        :twitter/id 42}
       {:github/username "github-username"
        :github/id 56})

;; ## Further reading

;; https://clojure.org/reference/namespaces

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
