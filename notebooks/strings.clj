;; # Strings
^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/toc true}
(ns notebooks.strings
  (:require
   [clojure.string :as string]
   [index :as i]))

;; > 👉 This notebook covers functions that are useful when using strings to
;; represent text

;; ### str

;; One of the most common needs is to concatenate some strings:
(str "Hello" " " "world")

;; ### subs

;; Another frequent use case is to get a substring of an existing string:
(subs "somestring" 3 6)

;; If we omit the second index we get all the remaining characters:
(subs "somestring" 3)

;; ## Parsing

;; If we need to parse another primitive value from a string:

;; ### parse-boolean
(parse-boolean "true")

;; ### parse-long
(parse-long "123")

;; ### parse-double
(parse-double "1.1618")

;; > ℹ️ There are other string manipulation functions available in
;; the [clojure.string](https://github.com/clojure/clojure/blob/master/src/clj/clojure/string.clj)
;; namespace. To use it we need to add a `require` in the `ns` form like this:
;; >
;; > ```clj
;; > (ns your.ns
;; >   (:require [clojure.string :as string]))
;; >```

;; Functions in `clojure.string` fall into two major categories:
;; 1. functions that return `true` or `false` based on some property of the string
;; 2. functions that return a modified copy of the string

;; You will find examples of both below.

;; ## Predicates

;; ### string/blank?

;; An empty string is blank:
(string/blank? "")

;; A string containing only spaces (or other whitespace) is blank:
(string/blank? "   ")

;; `nil` is blank:
(string/blank? nil)

(string/blank? "hello")

;; > ℹ️ `nil` means "nothing", i.e. it denotes the absence of a value.
;; > It is the same value as Java (or JavaScript) `null`.

;; ### string/starts-with?

;; If we want to check whether a string starts with another string:
(string/starts-with? "foobar" "foo")
(string/starts-with? "foobar" "for")

;; ### string/ends-with?

;; If we want to check whether a string ends with another string:
(string/ends-with? "foobar" "bar")
(string/ends-with? "foobar" "baz")

;; ### string/includes?

;; We can also check whether a string contains another one:

(string/includes? "superkalifragilistikexpialigetisch" "list")

;; ## Transform

;; The functions in this section take a string and return it transformed in some
;; way:

;; ### string/lower-case

;; Convert a string to lowercase:
(string/lower-case "miXed CasE")

;; ### string/upper-case

;; Convert a string to uppercase:
(string/upper-case "miXed CasE")

;; ### string/capitalize

;; If we have a string and want it capitalized:
(string/capitalize "title")

;; Just to be clear `capitalize` will not only affect the first character:
(string/capitalize "titLE")

;; ### string/reverse
(string/reverse "rats")

;; ### string/replace

;; We can replace any matches of a string with another one:
(string/replace "fiddle find fill" "fi" "ri")

;; ### string/replace-first

;; We can also replace just the first match of a string with another one:
(string/replace-first "fiddle find fill" "fi" "ri")

;; ### string/trim
;; Trim leading and trailing whitespace:
(string/trim " some STRING  \t ")

;; ### string/triml
;; Trim only the leading whitespace:
(string/triml " some STRING  \t ")

;; ### string/trimr
;; Trim only the trailing whitespace:
(string/trimr " some STRING  \t ")

;; ### string/trim-newline
;; Trim trailing newlines:
(string/trim-newline " some \n STRING\n\n")

;; ### string/escape
(string/escape "riddle" {\r \p
                         \i \a})

;; ### string/join
(string/join "-" ["Fee" "fi" "fo" "fum"])

;; ## Regexes

;; We make a regex by prefixing it with a `#`:

;; ```clj
;; #"some regex"
;; ```

;; For example, this would match one or more digits:

;; ```clj
;; #"\d+"
;; ```

;; ### re-matches

;; To match an entire string against a regex:
(re-matches #"\d+" "123456")
(re-matches #"\d+" "123db456")

;; We can also use groups in which case we will get a vector with the first
;; element being the whole match and the subsequent elements being the group
;; matches:

(re-matches #"([A-Za-z]+)(\d+)([A-Za-z]+)" "abc123def")

;; ### re-find

;; While `re-matches` matched the whole string, `re-find` will return the first
;; match:
(re-find #"\d+" "123")
(re-find #"\d+" "ab456cd")
(re-find #"\d+" "abcd")

;; ### re-seq

;; `re-find` returns the first match it finds, but sometimes we want *all*
;; matches:
(re-seq #"\d+" "123ab456")

;; ### split

;; We can also split a string using a regex:
(string/split "Fee-fi-fo-fum" #"-")

;; ### split-lines

;; To split on newlines:
(string/split-lines "Fee\nfi\nfo-fum")

;; ## UUIDs

;; ### random-uuid
(random-uuid)

;; ### parse-uuid
(parse-uuid "c599c08b-5293-4d30-b3d6-11827e87e3a6")

;; ### uuid?
(uuid? #uuid "c599c08b-5293-4d30-b3d6-11827e87e3a6")

;; ## What's next?

^{:nextjournal.clerk/visibility {:code :hide}
  :nextjournal.clerk/no-cache true}
(i/whats-next)
