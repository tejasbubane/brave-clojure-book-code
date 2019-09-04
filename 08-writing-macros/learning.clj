;; when is a macro
(macroexpand '(when (> 1 2)
                (println "foo")
                (println "bar")))
;; macros are integral to clojure - lots of basic operations are defined as macros

;; macros receive arguments in unevaluated form
;; macros almost always return a list that will be evaluated
;; just like functions macros can use argument destructuring & recursion

(defmacro my-print
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result)) ;; quote all symbols - to prevent evaluation in macro definition

(defmacro when
  [test & body]
  (list 'if test (cons 'do body)))
(when (= 1 1)
  (println "this")
  (println "prints"))

(defmacro unless
  "Inverted if"
  [test & branches]
  (conj (reverse branches) test 'if))
(unless (= 1 2)
        (println "foo")
        (println "bar"))

;; Syntax quoting (back-tick) recursively quotes all elements
`(+ 1 (* 2 3))
;; tilde removes the effect of syntax quoting
`(+ 1 ~(inc 2))

(defn criticize-code
  [criticism code]
  `(println ~criticism (quote ~code)))

(defmacro code-critic
  "Good vs bad"
  [bad good]
  `(do ~(criticize-code "This is bad code:" bad)
       ~(criticize-code "This is good code:" good)))
(code-critic (1 + 1) (+ 1 1))

(defmacro code-critic-map
  [bad good]
  `(do ~(map #(apply criticize-code %)
             [["This is bad code:" bad]
              ["This is good code:" good]])))
(code-critic-map (1 + 1) (+ 1 1)) ;; fails because println returns nil and do tries to eval it

`(+ ~(list 1 2 3))
;; Unquote splicing
`(+ ~@(list 1 2 3)) ;; unwraps content and places it in outer parens

(defmacro code-critic-map-correct
  [bad good]
  `(do ~@(map #(apply criticize-code %)
              [["This is bad code:" bad]
               ["This is good code:" good]])))
(code-critic-map-correct (1 + 1) (+ 1 1))

;; syntax quoting raises exception when trying to use let binding within macro
;; such bindings shadow existing bindings - use gensym to create new temp variables on each call
(gensym)
(gensym 'message)

(def message "Bangalore")
(defmacro without-mischief
  [& stuff-to-do]
  (let [macro-message (gensym 'message)]
    `(let [~macro-message "Oh, big deal!"]
       ~@stuff-to-do
       (println "I still need to say: " ~macro-message))))
(without-mischief (println "Heer's how I feel: " message))

;; auto-gensyms
`(blarg# blarg#)
`(let [name# "Larry Potter"] name#)
;; x# resolves to same symbol within same syntax-quoted list

;; auto-gensyms are also used to avoid evaluating twice - assign result to gensymed var using let
(defmacro report
  [to-try]
  `(let [result# ~to-try]
     (if result#
       (println (quote ~to-try) "was successful:" result#)
       (println (quote ~to-try) "was not successful:" result#))))
(report (= 1 1))
(report (= 3 4))

;; macros are hard to compose
