(ns user
  (:require
   [nextjournal.clerk :as clerk]))

(comment
  (clerk/serve! {:browse? true
                 :port 6677
                 :paths ["notebooks/**"]
                 :index "notebooks/index.clj"
                 :watch-paths ["notebooks"]})

  (clerk/clear-cache!))
