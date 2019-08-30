(defmacro backwards
  [form]
  (reverse form))
(backwards (" backwards" " am" "I" str))

(def addition-list (list + 1 2))
(eval addition-list)
(eval (concat addition-list [10]))
(eval (list 'def 'lucky-number (concat addition-list [10])))
lucky-number

;; Reader
(read-string "(+ 1 2)")
(list? (read-string "(+ 1 2)"))
(conj (read-string "(+ 1 2)") :zoom) ;; read text without evaluating
(eval (read-string "(+ 1 2)"))

(read-string "#(+ 1 %)")
(read-string "'(1 2 3)")
(read-string "@var")
(read-string "; ignore!\n(+ 1 2)") ;; single line comment
;; ' # @ are reader macros
;; even semicolon is a reader macro! - it just makes the comment go away from parsed code

;; Evaluator
(read-string "+")
(type (read-string "+")) ;; + is symbol in code, different from actual function
(list (read-string "+") 1 2)
;; this symbol refers to function
;; when evaluated, clojure looks up the function definition corresponding to symbol and evaluates it
(eval ((read-string "+") 1 2))

;; Macros
;; executed between the reader & evaluator
;; way to manipulate lists before clojure evaluates them
(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))
(ignore-last-operand (+ 1 2 10))
(ignore-last-operand (+ 1 2 (println "nothing prints")))

;; macroexpand lets you xee what data structure a macro returns
(macroexpand '(ignore-last-operand (+ 1 2 10)))
(macroexpand '(ignore-last-operand (+ 1 2 (println "look at me!"))))

(defmacro infix
  [infixed]
  (list (second infixed)
        (first infixed)
        (last infixed)))
(infix (1 + 2))

;; -> called threading or stabby macro
(defn read-resource
  [path]
  (-> path
      clojure.java.io/resource
      slurp
      read-string))
