(ns index
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
   [nextjournal.clerk :as clerk]
   [clojure.string :as string]))

^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(def notebooks
  [[{:title "🚀 Getting Started"
     :path  "notebooks/getting_started"}
    {:title "🪢 Strings"
     :path  "notebooks/strings"}
    {:title "🧮 Numbers"
     :path  "notebooks/numbers"}
    {:title "🗝 Keywords"
     :path  "notebooks/keywords"}]

   [{:title "📦 Collections"
     :path "notebooks/collections"}
    {:title "🔢 Sequences"
     :path "notebooks/sequences"}
    {:title "🗂 Vectors"
     :path "notebooks/vectors"}
    {:title "🗺 Maps"
     :path "notebooks/maps"}
    {:title "🤹 Sets"
     :path "notebooks/sets"}]

   [{:title "🚦 Control Flow"
     :path "notebooks/control_flow"}
    {:title "🧠 Logic"
     :path "notebooks/logic"}
    {:title "💎 Functions"
     :path "notebooks/functions"}
    {:title "🧩 Destructuring"
     :path "notebooks/destructuring"}
    {:title "🪡 Threading"
     :path "notebooks/threading"}
    {:title "🗄 Namespaces"
     :path "notebooks/namespaces"}
    {:title "📘 Spec"
     :path "notebooks/spec"}
    {:title "🧪 Unit Testing"
     :path "notebooks/unit_testing"}]])

^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(def later
  [[{:title "📚 Books"
     :path "notebooks/books"}
    {:title "📺 Talks"
     :path "notebooks/talks"}]])

^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(defn card
  [{:keys [path title]}]
  (let [current-path? (= path
                         (-> (str *ns*)
                             (string/replace "." "/")
                             (string/replace "-" "_")))]
    [:a (cond-> {:class "inline-flex w-full px-4 py-2 text-sm border-b border-inherit hover:bg-gray-100 dark:hover:bg-slate-900 dark:hover:text-white"
                 :style {:text-decoration-line "none"}}
          current-path?
          (update :class #(str % " bg-gray-100 dark:bg-slate-900"))

          (not path)
          (update :class #(str % " opacity-25"))

          (and path (not current-path?))
          (assoc :href (clerk/doc-url path)))
     [:h4 {:style {:margin-bottom 0}} title]]))

^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(defn group
  [notebooks]
  [:div
   [:div {:class "overflow-hidden text-gray-900 bg-white border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700 dark:text-white"}
    (map card notebooks)]])

^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(defn groups
  []
  [:div.grid.grid-cols-4.gap-2
   (map group notebooks)
   [:div.space-y-2
    (map group later)]])

^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(defn whats-next
  []
  (clerk/html {:nextjournal.clerk/width :wide}
   (groups)))

^{:nextjournal.clerk/width :wide}
(clerk/html
 [:<>
  [:h1 "Notes on Clojure"]
  (groups)])
