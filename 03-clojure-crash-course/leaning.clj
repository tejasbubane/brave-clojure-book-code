(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "I am batman!"))

(println "Cleanliness is next to godliness")

(defn train
  []
  (println "Choo Choo!"))

;; (map) gives error

(+ 1 (* 2 3) 4)

(str "It was panda" " in the library" " with the dust buster")

(if true
  "By Zeus' hammer!"
  "By Aquaman's trident!")

(if false
  "By Zeus' hammer!"
  "By Aquama's trident!")

(if false "No else here")

;; Do multiple things using `do` operator
(if true
  (do (println "Success!")
      "By Zeus' hammer!")
  (do (println "Failure!")
      "By Aquaman's trident!"))

;; `when` is a combination of `if` & `do` with no `else` branch
(when true
  (println "Success!")
  "abra cadabra")

(nil? 1)
(nil? nil)

;; `nil` & `false` represent logical falsiness, all other values are truthy

(if "bears eat beets"
  "string is truthy")

(if nil
  "this will never result"
  "nil is falsy")

(= 12 12)
(= 10 9)
(= nil nil)

(or false nil "Or returns last value if all previous are false")
(or (= 0 1) (= "yes" "no"))
(or nil)

(and :free_wifi :hot_coffee) ;; these are keywords
(and :returns_first_falsy_value nil false)
(and :returns_first_falsy_value :or_last_truthy_value)

(def failed-names
  ["Larry" "Doren" "Hulk"]) ;; [] is a vector

(defn error-message
  [severity]
  (str "We're "
       (if (= severity :mild)
         "mildly inconvenienced."
         "DOOMED!")))
(error-message :mild)
(error-message :burnt)

;; Clojure strings are always double quoted
;; No string interpolation only concatenation (via `str` function)

;; Maps
{}
(def me
  {:first-name "Tejas"
   :last-name "Bubane"})
(def me-nested {:name {:first "Tejas" :last "Bubane"}})
(hash-map :a 1 :b 2) ;; another way to create map
(get me :first-name)
(get me :fst "default-name")
(get-in me-nested [:name :first])
(get-in me-nested [:name :last])
;; for lookups maps can also be done as functions
(me :first-name)
(:first-name me)

;; Vectors - like arrays
[3 2 1] ;; no spaces
(get [3 2 1] 0)
(get ["a" {:name "Tejas Bubane"} "c"] 1) ;; elements can be of any type
(vector "marvel" "multiverse")
(conj [1 2 3] 4)

;; Lists - linked list - not indexed
'(1 2 3)
(nth '(:a :b :c) 1)
(list 1 "two" :three {4 5})
(conj '(1 2 3) :elements_are_added_to_beginning_of_list)

;; Sets
#{"kurt" 20 :ice-cube "ordering not preserved"}
(hash-set 1 2 2 3 1) ;; duplicates removed
(conj #{:a :b} :b)
(def my-set #{:a "foo" :tejas "sam" 1 2})
(get my-set 1)
(contains? my-set "tejas")
(contains? my-set :tejas)
(:tejas my-set)

;; Functions
(or + -) ;; returns function
((or + -) 1 2 3)
((and (= 1 1) -) 4 2)
(map inc '(1 2 3)) ;; higher order function
;; All arguments are evaluated before executing function
;; if is special form - does not evaluate all arguments
;; special forms & macros cannot be passed to functions as args

(defn function-name
  "docstring with more description of function"
  [parameters]
  (println "function body"))
;; Arity overloading
(defn multi-arity
  ([one]
   (println "single argument"))
  ([one two]
   (println "two arguments"))
  ([one two three]
   (println "three arguments")))
(multi-arity 10)
(multi-arity 20 30)
(multi-arity 13 35 65)

(defn my-first
  [[fst snd]] ;; destructuring vectors
  fst)
(my-first ["foo" "bar"])

(defn print-location
  [{lat :lat lng :lng}] ;; destructuring maps - can also be done using [{:keys [lat lng]}]
  (println "latitude: " lat "longitude: " lng))
(print-location {:lat 129.45 :lng 983.81})

(defn many-expressions
  []
  (+ 1 2)
  20
  "last value is returned")
(many-expressions)

;; Anonymous functions
;; (fn [params-list] function-body)
(map (fn [name] (str "Hi " name)) ["Adam" "Eve"])
((fn [x] (+ 20 x)) 9)
(#(* % 3) 8) ;; % indicates arguments - can be distinguished using %1 %2 %3
(map #(str "Hi " %) ["Adam" "Eve"])

;; loop
(loop [iteration 0]
  (println (str "Iteration " iteration))
  (if (> iteration 3)
    (println "Goodbye!")
    (recur (inc iteration))))

(def asym-hobbit-body-parts
  [{:name "head" :size 3}
   {:name "left-eye" :size 1}
   {:name "left-ear" :size 1}
   {:name "mouth" :size 1}
   {:name "nose" :size 1}
   {:name "neck" :size 2}
   {:name "left-shoulder" :size 3}
   {:name "left-upper-arm" :size 3}
   {:name "chest" :size 10}
   {:name "back" :size 10}
   {:name "left-forearm" :size 3}
   {:name "abdomen" :size 6}
   {:name "left-kidney" :size 1}
   {:name "left-hand" :size 2}
   {:name "left-knee" :size 2}
   {:name "left-thigh" :size 4}
   {:name "left-lower-leg" :size 3}
   {:name "left-achilles" :size 1}
   {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps with :name & :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))
(symmetrize-body-parts asym-hobbit-body-parts)

(defn better-symmetrize
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts
                  (set [part (matching-part part)]))) []  asym-body-parts))
(better-symmetrize asym-hobbit-body-parts)

(defn hit
  [asym-body-parts]
  (let [sym-parts (better-symmetrize asym-hobbit-body-parts)
        body-part-size-num (reduce + (map :size sym-parts))
        target (rand body-part-size-num)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))
(hit asym-hobbit-body-parts)
