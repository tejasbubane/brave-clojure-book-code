;; Function composition
((comp inc *) 2 3)

(def character
  {:name "Smith"
   :attributes {:intelligence 100
                :evil true}})
(def c-int (comp :intelligence :attributes))
(def c-evil (comp :evil :attributes))

(c-int character)
(c-evil character)

(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2))))
(spell-slots character)
;; wrap in anonymous function if number of args don't match
(def spell-slots-comp (comp int inc #(/ % 2) c-int))
(spell-slots-comp character)

(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))
((two-comp inc *) 4 5)

(def cookie-monster (assoc-in {} [:cookie :monster :vocals] "Finntroll"))
(get-in cookie-monster [:cookie :monster :vocals])
(assoc-in {} [1 :connections 4] 2)

;; reducing is another way to compose functions
;; apply function - apply next over result of previous
(require '[clojure.string :as s])
(defn clean
  [text]
  (reduce (fn [string string-fn] (string-fn string))
          text
          [s/trim #(s/replace % #"lol" "LOL")]))
(clean "Foo lol foo lol f")
