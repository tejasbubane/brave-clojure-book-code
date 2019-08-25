;; `seq` abstraction
(defn titleize
  [topic]
  (str topic " for  the Brave"))
(map titleize ["Jack" "Joe"]) ;; vector
(map titleize '("Jack" "Jill")) ;; list
(map titleize #{"Jack" "Jill"}) ;; set
(map #(titleize (second %)) {:uncomfortable-thing })

;; map
(map inc [1 2 3])
(map str ["a" "b" "c"] ["A" "B" "C"]) ;; map with multiple collections (because str expects 2 args)
(map + [1 2 3] [1 2 3] [1 2 3])

(def human-consumption [8.1 7.3 6.6 5.0])
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data
  [human critter]
  {:human human :critter critter})
(map unify-diet-data human-consumption critter-consumption)
;; map to perform set of calculations on functions
(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))
(stats [1 2 3 4])
(stats [3 4 10])
(stats [12 13 14 15])

(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spiderman" :real "Peter Parker"}
   {:alias "Santa" :real "my mom"}])
(map :real identities)

;; Reduce
(reduce (fn [new-map [key val]]
          (assoc new-map key (inc val)))
        {} {:min 10 :max 30})
;; filter out keys from map - value > 4
(reduce (fn [new-map [key val]]
          (if (> val 4)
            (assoc new-map key val)
            new-map))
        {} {:x 4.1 :y 3.9})

(take 3 [1 2 3 4 5 6])
(drop 3 [1 2 3 4 5 6])

(sort-by count ["askj" "oiqwoqiw" "ask"])
(concat [1 2 3] [4 5 6 7])

;; Lazy sequences
(take 8 (repeat "na"))
(take 3 (repeatedly #(rand-int 10))) ;; repeatedly apply function

;; conj appends elements in vector
(conj [0] 1)
(into [0] [1])
;; but prepends elements in list
(conj '(0) 1)
(into '(0) '(1))

;; apply is like Ruby's splat operator
(apply max [1 2 3 4])

;; partial functions
(def add10 (partial + 10))
(add10 4)

(defn my-partial
  [partialized-fn & args]
  (fn [& more-args]
    (apply partialized-fn (into args more-args))))
(def add20 (my-partial + 20))
(add20 8)

(defn lousy-logger
  [log-level msg]
  (condp = log-level
    :warn (clojure.string/lower-case msg)
    :error (clojure.string/upper-case msg)))
(def warn (partial lousy-logger :warn))
(def error (partial lousy-logger :error))
(warn "add function is deprecated")
(error "expected 3 arguments given 2")

(def is-odd? (complement even?))
(is-odd? 19)
(is-odd? 20)

;; Putting all together
(def filename "suspects.csv")
(slurp filename)
(def data-keys [:name :score])
(defn str->int
  [str]
  (Integer. str))
(def conversions {:name identity :score str->int})
(defn convert
  [key val]
  ((get conversions key) val))
(convert :score "4")
(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))
(parse (slurp filename))
(defn mapify
  [rows]
  (map (fn [[name score]]
         {:name (convert :name name)
          :score (convert :score score)}) rows))
(def data (mapify (parse (slurp filename))))
(defn qualifiers
  [min-score records]
  (filter #(>= (:score %) min-score) records))
(qualifiers 4 data)
