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
  (let [colleagues (-> (first rows)
                       (rest))
        respondents (-> (map first rows)
                        (rest))
        _ (assert (set/subset? (set respondents)
                               (set colleagues)))
        cells (->> (rest rows)
                   (map (fn [[respondent & scores]]
                          (map-indexed (fn [score-i score]
                                         (let [colleague (nth colleagues score-i)]
                                           [{:row respondent
                                             :col colleague}
                                            (Integer/parseInt score)]))
                                       scores)))
                   (apply concat)
                   (into {}))]
    {:cells cells
     :labels colleagues}))

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

