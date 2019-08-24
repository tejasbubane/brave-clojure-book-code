;; 1. Use the str, vector, list, hash-map, hash-set functions
(str "tejas" " " "bubane")
(vector :a 1 "c")
(list 3 :k 7)
(hash-map :a 1 "b" :k)
(hash-set 1 2 1 2 2 2 1)

;; 2. Write function that takes a number & adds 100 to it
(defn add-100 [x] (+ x 100))
(add-100 8)

;; 3. Write a function dec-maker, that works exactly like inc-maker
;; except with subtraction
(defn dec-maker
  [n]
  #(- %1 n))
(def dec9 (dec-maker 9))
(dec9 10)

;; 4. Write a function mapset, that works like map
;; except the return value is a set
(defn mapset
  [f xs]
  (reduce (fn [result x] (conj result (f x))) #{} xs))
(mapset inc [1 1 2 2])

;; 5. Create function similar to symmetrize-body-parts
;; except it has to work with weird space aliens with radial symmetry.
;; Instead of two eyes, arms, lets & so on,m they have five.
(def asym-alien-body-parts
  [{:name "head" :size 3}
   {:name "eye-1" :size 1}
   {:name "ear-1" :size 1}
   {:name "mouth" :size 1}
   {:name "nose" :size 1}
   {:name "neck" :size 2}
   {:name "shoulder-1" :size 3}
   {:name "upper-arm-1" :size 3}
   {:name "chest" :size 10}
   {:name "back" :size 10}
   {:name "forearm-1" :size 3}
   {:name "abdomen" :size 6}
   {:name "kidney-1" :size 1}
   {:name "hand-1" :size 2}
   {:name "knee-1" :size 2}
   {:name "thigh-1" :size 4}
   {:name "lower-leg-1" :size 3}
   {:name "achilles-1" :size 1}
   {:name "foot-1" :size 2}])

(defn all-parts
  [{:keys [name size] :as part}]
  [part
   {:name (clojure.string/replace name #"1$" "2") :size size}
   {:name (clojure.string/replace name #"1$" "3") :size size}
   {:name (clojure.string/replace name #"1$" "4") :size size}
   {:name (clojure.string/replace name #"1$" "5") :size size}])

(defn symmetrize-body-parts
  [alien-parts]
  (reduce (fn [result part]
            (into result (set (all-parts part))))
          [] alien-parts))
(symmetrize-body-parts asym-alien-body-parts)

;; 6. Create function that generalizes exercise 5.
;; Should take number of matching body parts to add
(defn generic-all-parts
  [{:keys [name size] :as part} add-count]
  (reduce (fn [acc i]
            (conj acc {:name (clojure.string/replace name #"1$" (str i)) :size size}))
          [part] (range 2 (+ 2 add-count))))
(generic-all-parts {:name "eye-1" :size 4} 10)

(defn generic-symmetric-body-parts
  [alien-parts add-count]
  (reduce (fn [acc part]
            (into acc (set (generic-all-parts part add-count))))
          [] alien-parts))
(generic-symmetric-body-parts asym-alien-body-parts 5)
