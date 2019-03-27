(ns locator.sheet
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.set :as set]))


(defn csv-rows->maps [rows]
  (map zipmap
       (->> (first rows)
            (repeat))
       (rest rows)))

(defn parse-tm-tm-rows [rows]
  (let [rows (vec rows)
        colleagues (-> (first rows)
                       (rest))
        respondents (-> (map first rows)
                        (rest))]
    (assert (set/subset? (set respondents)
                         (set colleagues)))
    {:cols colleagues
     :rows respondents}))

(comment
  (println "ab")

  (-> (csv/read-csv "a,b,c\n1,2,3\n4,5,6")
      (csv-rows->maps))

  (-> (let [path (io/resource "responses/Jak skutecznie miejsca (Responses) - TM_TM.csv")]
        (with-open [reader (io/reader path)]
          (-> (csv/read-csv reader)
              (parse-tm-tm-rows))))
      (clojure.pprint/pprint))


  )

