;; 1. Create an atom with init val 0. Use swap! to increment it couple time then derefernce it
(def foo (atom 0))
(swap! foo inc)
(swap! foo inc)
@foo

;; 2. Create function that uses futures to parallelize task of downloading from
;; https://www.braveclojure.com/random-quote
;; futures update an atom that refers to a total word count for all quotes

(defn get-quote [] (slurp "https://www.braveclojure.com/random-quote"))
(def words
  (comp #(map clojure.string/lower-case %)
        #(clojure.string/split % #" ")
        #(clojure.string/replace % #"\." "")
        #(first (clojure.string/split % #".\n--"))))

(words (get-quote))
(frequencies (words (get-quote)))

;; trying out
(def acc (atom {}))
@acc
(take 3 (repeat (future )))
(def foo (future (swap! acc #(merge-with + % (frequencies (words (get-quote)))))))
@foo
(def quote-count 3)

;; putting it together
(defn quote-word-counts
  [quote-count]
  (let [acc (atom {})]
    (doall
     (map deref
          (take quote-count
                (repeat
                 (future
                   (swap! acc #(merge-with + % (frequencies (words (get-quote))))))))))
    @acc))

;; verifying
(time (quote-word-counts 4)) ;; way quicker
(time (doall (take 4 (repeat (words (get-quote)))))) ;; slow - sequential

;; 3. Create representations of two characters in a game
;; first character has hit 15 hit points out of a total of 40
;; second character has a healing potion in his inventory
;; use refs & transactions to model consumption of healing potion & first character healing
(def batman (ref {:hits 15}))
(def robin (ref {:healing 40}))
(defn heal
  [injured healer]
  (dosync
   (alter injured update-in [:hits] + 5)
   (alter healer update-in [:healing] - 5)))
(heal batman robin)
@batman
@robin
