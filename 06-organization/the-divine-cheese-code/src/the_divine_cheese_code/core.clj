(ns the-divine-cheese-code.core
  (:gen-class)
  (:require [the-divine-cheese-code.visualization.svg :as svg]
            [clojure.java.browse :as browse])) ;; multiple requires can be done inside ns

(require 'the-divine-cheese-code.visualization.svg)
(refer 'the-divine-cheese-code.visualization.svg)

(def heists [{:location "Germany"
              :cheese-name "Hildebold",
              :lat 50.95
              :lng 6.97}
             {:location "Switzerland"
              :cheese-name "Emmental"
              :lat 47.37
              :lng 8.55}
             {:location "Vatican"
              :cheese-name "Turin"
              :lat 41.90
              :lng 12.45}])

(defn template
  [contents]
  (str "<style>polyline {fill:nonel stroke:#5881d8; stroke-width:3}</style>"
       contents))

(defn url
  [filename]
  (str "file:///"
       (System/getProperty "user.dir")
       "/"
       filename))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [filename "map.html"]
    (->> heists
         (svg/xml 50 100)
         template
         (spit filename))
    (browse/browse-url (url filename))))
(-main)
