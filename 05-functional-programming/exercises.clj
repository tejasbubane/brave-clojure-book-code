;; 1. We used (comp :intelligence :attributes) to create function
;; Create new `attr` function (attr :intelligence) that does the same thing
(def character {:attributes {:intelligence 4}})
(def old-attr (comp :intelligence :attributes))
(old-attr character)

(def attr #(comp % :attributes))
((attr :intelligence) character)

;; 2. Implement `comp` function that accepts any number of arguments
(defn n-comp
  [& fs]
  (fn [& args]
    (reduce (fn [acc f]
              (f acc))
            (apply (last fs) args)
            (rest (reverse fs)))))
((n-comp inc inc inc *) 4 5)

;; 3. Implement `assoc-in` function.
(defn my-assoc-in
  ([m [k & ks] v]
   (if (empty? ks)
     (assoc m k v)
     (assoc m k (my-assoc-in (get m k) ks v)))))
(my-assoc-in character [:attributes :height] 25)

;; 4. Lookup & use`update-in` function
(update-in {:user {:email "Tejas.Bubane@Example.com"}} [:user :email]
           clojure.string/lower-case)

;; 5. Implement `update-in`
(defn my-update-in
  [m [k & ks] f]
  (if (empty? ks)
    (assoc m k (f (get m k)))
    (assoc m k (my-update-in (get m k) ks f))))
(my-update-in {:user {:email "Tejas.Bubane@Example.com"}} [:user :email]
              clojure.string/lower-case)
