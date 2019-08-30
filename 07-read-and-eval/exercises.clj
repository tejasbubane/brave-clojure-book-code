;; 1. Use list, quoting & read-string to create list of first name and sci-fi movie
(eval (list 'println (read-string "'(Tejas Inception)")))

;; 2. Create infix function that takes a list like (1 + 3 * 4 - 5) and
;; transofrms into prefix that clojure can evaluate
(defn infix
  ([a] a)
  ([first second & rest]
   (list second
         first
         (apply infix rest))))
(eval (infix 1 + 2 * 3))
(eval (infix 1 + 3 * 4 - 5)) ;; does not follow precendence, but basic works
