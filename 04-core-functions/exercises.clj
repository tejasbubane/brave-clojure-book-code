;; Implement `map` in terms of `reduce`
(defn my-map
  [f xs]
  (reduce (fn [acc x]
            (conj acc (f x)))
          [] xs))
(my-map inc [1 2 3 4])

;; Implement `filter` in terms of `reduce`
(defn my-filter
  [predicate xs]
  (reduce (fn [acc x]
            (if (predicate x)
              (conj acc x)
              acc))
          [] xs))
(my-filter even? [1 2 3 4 5 6 7 8 9 10])

;; Same as `putting it all together` from learnings
(def filename "suspects.csv")
(def data-keys [:name :score])
(defn str->int
  [str]
  (Integer. str))
(def conversions {:name identity :score str->int})
(defn convert
  [key val]
  ((get conversions key) val))
(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))
(defn mapify
  [rows]
  (map (fn [[name score]]
         {:name (convert :name name)
          :score (convert :score score)}) rows))
(def data (mapify (parse (slurp filename))))
(defn qualifier-fu
  [min-score records]
  (filter #(>= (:score %) min-score) records))
(def qualifiers (into '() (qualifier-fu 4 data)))
;; 1. Turn result of qualifiers into list of names
(map :name qualifiers)

;; 2. Write `append` which appends new suspect to list of suspects
(defn my-append
  [new-suspect]
  (concat data (list new-suspect)))
(my-append {:name "Joe" :score 200})

;; 3. Write `validate` which will check that `:name`  & `:score` are present
;; when you `append`.
(defn validate
  [predicates record]
  (every? (fn [[k predicate]]
            (predicate (k record)))
          predicates))
(validate {:name string? :score number?} {:name "tejas" :score 10})
(validate {:name string? :score number?} {:name "tejas" :score "10"})
(map (partial validate {:name string? :score number?}) data)

;; 4. Write function that will take your list of maps & convert back to CSV string
(defn to-csv
  [data]
  (clojure.string/join "\n" (map #(clojure.string/join "," (vals %)) data)))
(to-csv data)
