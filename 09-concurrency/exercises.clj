;; 1. Write a function that takes string as argument, searches for it on Google
;; using `slurp` function, return HTML for first page returned by search

;; Google search does not work with direct calls, so using youtube

(def urls
  {:youtube "https://www.youtube.com/results?search_query="
   :reddit "https://www.reddit.com/search/?q=android"})

(defn youtube [search-term]
  (slurp (str "https://www.youtube.com/results?search_query=" search-term)))
(youtube "apple")

;; 2. Update above function to take second argument with search engines to use
(defn search [search-term engines]
  (map (fn [engine] (future (print (slurp (str (get urls engine) search-term)))))
       engines))
(search "mango" [:reddit :youtube])
